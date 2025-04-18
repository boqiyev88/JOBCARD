package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.ResponseDto;
import uz.uat.backend.dto.StatusCountDto;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.*;
import uz.uat.backend.service.utils.UtilsService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobCarRepository jobCardRepository;
    private final WorkRepository workRepository;
    private final ServicesRepository servicesRepository;
    private final HistoryService historyService;
    private final CityRepository cityRepository;
    private final PDFfileRepository fileRepository;
    private final UtilsService utilsService;


    public ResponseDto getByStatusNum(int status, int page, String search) {
        int validPage = page <= 0 ? 0 : page - 1;
        boolean isValidStatus = status > 0 && status < 7;
        boolean hasSearch = (search != null && !search.trim().isEmpty());

        if (!isValidStatus && !hasSearch) {
            System.err.println(Instant.now() + ": Invalid search parameter");
            return getAll(validPage);
        }
        if (!isValidStatus) {
            return getBySearch(search, validPage);
        }
        Status selectedStatus = switch (status) {
            case 1 -> Status.NEW;
            case 2 -> Status.PENDING;
            case 3 -> Status.IN_PROCESS;
            case 4 -> Status.CONFIRMED;
            case 5 -> Status.COMPLETED;
            case 6 -> Status.REJECTED;
            default -> null;
        };
        if (hasSearch) {
            return getByStatusAndSearch(selectedStatus, search, validPage);
        }
        return getByStatus(selectedStatus, validPage);
    }

    public PdfFile getPdfFromJob(String jobId) {
        JobCard jobCardId = jobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found");
        return getFile(jobCardId);
    }

    public ResponseDto getBySearch(String search, int page) {
        Page<JobCard> jobCards = jobCardRepository.findBySearch(search, PageRequest.of(page , 10));
        if (jobCards.isEmpty()) {
            return getStatusCount(ResponseDto.builder()
                    .page(1)
                    .total(jobCards.getTotalElements())
                    .data(new ArrayList<>())
                    .build());
        }
        return getStatusCount(ResponseDto.builder()
                .page(page)
                .total(jobCards.getTotalElements())
                .data(utilsService.getJobCards(jobCards.getContent()))
                .build());
    }

    public ResponseDto getByStatusAndSearch(Status selectedStatus, String search, int page) {
        Page<JobCard> jobCards = jobCardRepository.findByStatusAndSearch(selectedStatus, search, PageRequest.of(page, 10));
        if (jobCards.isEmpty()) {
            return getStatusCount(ResponseDto.builder()
                    .page(1)
                    .total(jobCards.getTotalElements())
                    .data(new ArrayList<>())
                    .build());
        }
        return getStatusCount(ResponseDto.builder()
                .page(page + 1)
                .total(jobCards.getTotalElements())
                .data(utilsService.getJobCards(jobCards.getContent()))
                .build());
    }

    public ResponseDto getAll(int page) {
        Page<JobCard> all = jobCardRepository.getAll(PageRequest.of(page, 10));
        if (all.isEmpty())
            getStatusCount(ResponseDto.builder()
                    .page(page + 1)
                    .total(all.getTotalElements())
                    .data(utilsService.getJobCards(all.getContent()))
                    .build());

        return getStatusCount(ResponseDto.builder()
                .page(page + 1)
                .total(all.getTotalElements())
                .data(utilsService.getJobCards(all.getContent()))
                .build());
    }

    public ResponseDto getByStatus(Status status, int page) {
        Page<JobCard> jobCards = jobCardRepository.findBySTATUS(status, PageRequest.of(page, 10));
        if (jobCards.isEmpty()) {
            return getStatusCount(ResponseDto.builder()
                    .page(1)
                    .total(jobCards.getTotalElements())
                    .data(new ArrayList<>())
                    .build());
        }
        return getStatusCount(ResponseDto.builder()
                .page(page + 1)
                .total(jobCards.getTotalElements())
                .data(utilsService.getJobCards(jobCards.getContent()))
                .build());

    }

    public PdfFile getFile(JobCard jobCard) {
        if (jobCard.getMainPlan() == null)
            return new PdfFile();
        Optional<PdfFile> optional = fileRepository.findById(String.valueOf(jobCard.getMainPlan().getId()));
        if (optional.isEmpty())
            throw new MyNotFoundException("file not found");
        return optional.get();
    }

    private ResponseDto getStatusCount(ResponseDto responseDto) {
        List<StatusCountDto> dto = jobCardRepository.getByStatusCount();
        Long newCount = dto.stream().filter(d -> "NEW".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long pendingCount = dto.stream().filter(d -> "PENDING".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long inProcessCount = dto.stream().filter(d -> "IN_PROCESS".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long confirmedCount = dto.stream().filter(d -> "CONFIRMED".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long completedCount = dto.stream().filter(d -> "COMPLETED".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long rejectedCount = dto.stream().filter(d -> "REJECTED".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        return ResponseDto.builder()
                .page(responseDto.page())
                .total(responseDto.total())
                .data(responseDto.data())
                .all(newCount + pendingCount + inProcessCount + confirmedCount + completedCount + rejectedCount)
                .New(newCount)
                .Pending(pendingCount)
                .In_process(inProcessCount)
                .Confirmed(confirmedCount)
                .Completed(completedCount)
                .Rejected(rejectedCount)
                .build();
    }

}
