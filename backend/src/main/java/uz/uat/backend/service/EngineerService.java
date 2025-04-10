package uz.uat.backend.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyConflictException;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.*;
import uz.uat.backend.mapper.ServiceNameMapper;
import uz.uat.backend.mapper.TaskMapper;
import uz.uat.backend.model.*;
import uz.uat.backend.model.enums.OperationStatus;
import uz.uat.backend.model.enums.TableName;
import uz.uat.backend.repository.*;
import uz.uat.backend.service.serviceIMPL.EngineerServiceIM;
import uz.uat.backend.service.utils.UtilsService;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
@Service
@RequiredArgsConstructor
public class EngineerService implements EngineerServiceIM {

    private final ServicesRepository servicesRepository;
    private final ServiceNameRepository serviceNameRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final Notifier notifier;
    private final HistoryService historyService;
    private final ServiceNameMapper serviceNameMapper;
    private final UtilsService utilsService;

    public Resource generateCsvFile(@NotBlank String fileName) {
        try {
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Number,Description\n");
            for (int i = 1; i <= 1000; i++) {
                csvContent.append(i).append(".TASKNUM,Description ").append(i).append("\n");
            }

            // Faylni vaqtinchalik yaratish
            Path filePath = Files.createTempFile("service", ".csv");
            Files.write(filePath, csvContent.toString().getBytes());

            // Faylni yuklash uchun resurs yaratish
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<TaskDto> uploadfile(@NotBlank MultipartFile file) {
        String contentType = file.getContentType();
        List<TaskDto> tasks;
        assert contentType != null;
        if (contentType.equals("application/pdf")) {
            tasks = uploadPDF(file);
        } else if (contentType.equals("text/csv")) {
            tasks = uploadCSV(file);
        } else
            throw new MyConflictException("Unsupported content type file: " + contentType);

        return tasks;
    }

    @Transactional
    @Override
    public ResponsesDtos addNewService(ServiceDto workListDto) {
        if (workListDto == null)
            throw new MyNotFoundException("workList is null");

        Optional<ServiceName> optional = serviceNameRepository.findByName(workListDto.serviceName_id());
        if (optional.isEmpty())
            throw new MyNotFoundException("service name not found");

        ServiceName serviceName = optional.get();
        List<Task> tasks = taskMapper.toEntitys(workListDto.tasks());
        List<Task> saved = taskRepository.saveAll(tasks);
        Services saveService = servicesRepository.save(
                Services.builder()
                        .serviceType(workListDto.serviceType())
                        .serviceName(serviceName)
                        .revisionTime(LocalDate.now())
                        .revisionNumber(workListDto.revisionNumber())
                        .tasks(saved)
                        .build()
        );
        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.services.name())
                .tableID(saveService.getId())
                .OS(OperationStatus.CREATED.name())
                .oldValue(" ")
                .newValue(saveService.toString())
                .updatedBy(saveService.getUpdUser())
                .updTime(saveService.getUpdTime())
                .build());
        notifier.EngineerNotifier(saveService);
        return getPage(0);
    }

    @Override
    public ResponsesDtos getMainManu(LocalDate from, LocalDate to, String search, int page) {
        boolean isSearchEmpty = (search == null || search.isEmpty());
        boolean isDateEmpty = (from == null && to == null);
        int validPage = (page <= 0) ? 0 : (page - 1);

        if (isSearchEmpty && isDateEmpty) {
            Page<Services> services = servicesRepository.getByPage(PageRequest.of(validPage, 10));
            if (services.isEmpty()) {
                return ResponsesDtos.builder()
                        .page(validPage + 1)
                        .total(services.getTotalElements())
                        .data(utilsService.fromEntityService(services.getContent()))
                        .build();
            }
            return ResponsesDtos.builder()
                    .page(validPage + 1)
                    .total(services.getTotalElements())
                    .data(utilsService.fromEntityService(services.getContent()))
                    .build();
        }
        if (!isSearchEmpty && isDateEmpty) {
            return getBySearch(search, validPage);
        }
        if (from != null && to == null) {
            return getByDate(from, LocalDate.now(), validPage);
        }
        if (from == null && to != null) {
            return getByToDate(to, validPage);
        }

        return getByDate(from, to, validPage);
    }

    @Override
    public List<ServiceNameDto> getServiceName() {
        Optional<List<ServiceName>> optionalList = serviceNameRepository.getServiceName();
        if (optionalList.isEmpty() || optionalList.get().isEmpty())
            throw new MyNotFoundException("serviceName is null");
        return serviceNameMapper.fromEntity(optionalList.get());
    }

    @Override
    public ResponsesDtos getServices(String serviceId) {
        Optional<Services> optional = servicesRepository.findById(serviceId);
        if (optional.isEmpty())
            throw new MyNotFoundException("serviceId not found by this service jobid: " + serviceId);
        Services services = optional.get();
        List<TaskDto> taskDtos = taskMapper.list(services.getTasks());
        return ResponsesDtos.builder()
                .data(taskDtos)
                .build();
    }

    @Override
    public ResponsesDtos editTask(@NotBlank String id, @NotBlank ServiceDto workListDto) {
        if (workListDto == null || id == null)
            throw new MyNotFoundException("workList or jobid is null");

        Optional<Services> optional1 = servicesRepository.findById(id);
        Optional<ServiceName> optional = serviceNameRepository.findByName(workListDto.serviceName_id());
        if (optional.isEmpty() || optional1.isEmpty())
            throw new MyNotFoundException("service name or Service not found");

        ServiceName serviceName = optional.get();
        Services services = optional1.get();
        List<Task> tasks = taskMapper.toEntitys(workListDto.tasks());
        List<Task> saved = taskRepository.saveAll(tasks);
        services.setServiceName(serviceName);
        services.setTasks(saved);
        services.setRevisionNumber(workListDto.revisionNumber());
        services.setServiceType(workListDto.serviceType());
        Services saveService = servicesRepository.save(services);

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.services.name())
                .tableID(services.getId())
                .OS(OperationStatus.UPDATED.name())
                .oldValue(services.toString())
                .newValue(saveService.toString())
                .updatedBy(saveService.getUpdUser())
                .updTime(saveService.getUpdTime())
                .build());
        notifier.EngineerNotifier(saveService);

        return getPage(1);
    }

    @Override
    public ResponsesDtos getDeleteTask(@NotBlank String id) {
        Optional<Services> optional = servicesRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("services not found by jobid " + id);
        Services service = optional.get();
        service.setIsDeleted(1);
        Services save = servicesRepository.save(service);
        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.services.name())
                .tableID(service.getId())
                .OS(OperationStatus.DELETED.name())
                .oldValue(service.toString())
                .newValue(save.toString())
                .updatedBy(save.getUpdUser())
                .updTime(save.getUpdTime())
                .build());
        notifier.EngineerNotifier(save);
        return getPage(1);
    }


    private ResponsesDtos getByToDate(LocalDate from, int page) {
        Page<Services> services = servicesRepository.getByToDate(from, PageRequest.of(page, 10));
        if (services.isEmpty())
            throw new MyNotFoundException("services is empty");
        return ResponsesDtos.builder()
                .page(page + 1)
                .total(services.getTotalElements())
                .data(utilsService.fromEntityService(services.getContent()))
                .build();
    }

    private ResponsesDtos getByDate(LocalDate startDate, LocalDate endDate, int page) {
        Page<Services> services = servicesRepository.searchServicesByDate(startDate, endDate, PageRequest.of(page, 10));
        if (services.isEmpty())
            throw new MyNotFoundException("services is empty");
        return ResponsesDtos.builder()
                .page(page + 1)
                .total(services.getTotalElements())
                .data(utilsService.fromEntityService(services.getContent()))
                .build();
    }

    private ResponsesDtos getBySearch(String search, int page) {
        Page<Services> services = servicesRepository.searchByNameOrType(search, PageRequest.of(page - 1, 10));
        if (services.isEmpty())
            getPage(page);
        return ResponsesDtos.builder()
                .page(page + 1)
                .total(services.getTotalElements())
                .data(utilsService.fromEntityService(services.getContent()))
                .build();
    }

    private ResponsesDtos getPage(@NotBlank int page) {
        Page<Services> services = servicesRepository.getByPage(PageRequest.of(page, 10));
        if (services.isEmpty())
            throw new MyNotFoundException("services is empty");
        return ResponsesDtos.builder()
                .page(page + 1)
                .total(services.getTotalElements())
                .data(utilsService.fromEntityService(services.getContent()))
                .build();
    }

    private List<TaskDto> uploadPDF(MultipartFile file) {
        List<Task> taskList = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            String pdfText = new PDFTextStripper().getText(document);
            Pattern pattern = Pattern.compile("(\\d+)\\.\\s*(.+)");
            Matcher matcher = pattern.matcher(pdfText);
            while (matcher.find()) {
                String number = matcher.group(1);
                String text = matcher.group(2);
                taskList.add(
                        Task.builder()
                                .number(String.valueOf(number))
                                .description(text)
                                .build()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskMapper.list(taskList);
    }

    private List<TaskDto> uploadCSV(MultipartFile file) {
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
        List<TaskDto> tasksDto = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    tasks.add(
                            Task.builder()
                                    .number(data[0])
                                    .description(data[1])
                                    .build()

                    );
                }
            }
            taskRepository.saveAll(tasks);
            tasksDto = taskMapper.list(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasksDto;
    }


}
