package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.mapper.Specialist_JobCardMapper;
import uz.uat.backend.mapper.Technician_JobCardMapper;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.Specialist_JobCard;
import uz.uat.backend.model.Technician_JobCard;
import uz.uat.backend.model.enums.Status;


import uz.uat.backend.repository.Specialist_JobCardRepository;
import uz.uat.backend.repository.Technician_JobCardRepository;
import uz.uat.backend.service.serviceIMPL.SpecialistServiceIM;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {


    private final Technician_JobCardMapper technicianJobCardMapper;
    private final Specialist_JobCardMapper specialistJobCardMapper;
    private final Technician_JobCardRepository technicianJobCardRepository;
    private final Specialist_JobCardRepository specialistJobCardRepository;
    private final Notifier notifier;

    @Override
    public void addJobCard(JobCardDto jobCardDto, MultipartFile file) {
        if (jobCardDto == null || file == null || file.isEmpty()) {
            throw new MultipartException("Invalid data file or input object");
        }
        try {
//            JobCard jobCard = jobCardMapper.toEntity(jobCardDto);
//            PdfFile file1 = pdffileRepository.save(PdfFile.builder()
//                    .fileName(file.getName())
//                    .data(file.getBytes())
//                    .build());
//            jobCard.setMainPlan(file1);
//            jobCardRepository.save(jobCard);
            Specialist_JobCard specialist_jobCard = specialistJobCardMapper.toEntity(jobCardDto);
            specialist_jobCard.setMainPlan(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build());
            specialist_jobCard.setSTATUS(Status.NEW);
            Specialist_JobCard saveSpecialist = specialistJobCardRepository.save(specialist_jobCard);
            notifier.SpecialistNotifier(saveSpecialist);
            notifier.SpecialistMassageNotifier("New JobCard added");

            Technician_JobCard technician_jobCard = technicianJobCardMapper.toEntity(jobCardDto);
            technician_jobCard.setSTATUS(Status.NEW);
            technician_jobCard.setMainPlan(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build());
            Technician_JobCard saveTechnician = technicianJobCardRepository.save(technician_jobCard);
            notifier.TechnicianNotifier(saveTechnician);
            notifier.TechnicianMassageNotifier("New JobCard added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }    /// real time da ishlayapti

    @Override
    public void returned(RequestDto requestDto) {
        if (requestDto.id() == null || requestDto.massage() == null)
            throw new UsernameNotFoundException("invalid request id or massage is null");

        try {
            Specialist_JobCard jobCardId = getById(requestDto.id());
            Technician_JobCard tj = getTechnician(jobCardId.getWorkOrderNumber());

            tj.setSTATUS(Status.REJECTED);
            Technician_JobCard technician_jobCard = technicianJobCardRepository.save(tj);
            notifier.TechnicianNotifier(technician_jobCard);               /// real timeda texnik tablega jo'natildi

            jobCardId.setSTATUS(Status.REJECTED);
            Specialist_JobCard specialist_jobCard = specialistJobCardRepository.save(jobCardId);
            notifier.SpecialistNotifier(specialist_jobCard);                /// real timeda specialist tablega jo'natildi

            notifier.TechnicianMassageNotifier("Sizda Tasdiqdan o'tmagan ish mavjud");    /// uvidemleniyaga ish reject bo'lgani haqida habar jo'natildi
        } catch (Exception e) {
            e.printStackTrace();
        }

    } /// real time da ishlayapti

    @Override
    public void confirmed(String jobId) {
        Specialist_JobCard jobCard = getById(jobId);
        jobCard.setSTATUS(Status.COMPLETED);
        notifier.SpecialistNotifier(jobCard);

        Technician_JobCard technician = getTechnician(jobCard.getWorkOrderNumber());
        technician.setSTATUS(Status.COMPLETED);
        notifier.TechnicianNotifier(technician);
        notifier.TechnicianMassageNotifier(jobCard.getWorkOrderNumber() + "'s Job Completed by Specialist");
    }  /// real time da ishlayapti

    @Override
    public void statusInProcess(String jobId) {
        /// job card va work bir biriga qanday bog'langanini bilish kerak
        try {

            Specialist_JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
            if (jobCardId == null)
                throw new UsernameNotFoundException("job card not found, may be Invalid job card id");

            Optional<Technician_JobCard> optional =
                    technicianJobCardRepository.findByWorkOrderNumber(jobCardId.getWorkOrderNumber());

            if (optional.isEmpty())
                throw new UsernameNotFoundException("technician job card not found, may be Invalid job card id");
            Technician_JobCard tj = optional.get();

            tj.setSTATUS(Status.IN_PROCESS);
            Technician_JobCard saveTechnician = technicianJobCardRepository.save(tj);
            notifier.TechnicianNotifier(saveTechnician);
            notifier.TechnicianMassageNotifier("JobCard updated");

            jobCardId.setSTATUS(Status.IN_PROCESS);
            Specialist_JobCard saveSpecialist = specialistJobCardRepository.save(jobCardId);
            notifier.SpecialistNotifier(saveSpecialist);
            notifier.TechnicianMassageNotifier("JobCard updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }    /// real time da ishlayapti kamchiliklari bor hali

    @Override
    public PdfFile getPdfFromJob(String jobId) {
        Specialist_JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found");
        return jobCardId.getMainPlan();
    }

    @Override
    public Specialist_JobCard getById(String id) {
        Specialist_JobCard jobCardId = specialistJobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    @Override
    public Technician_JobCard getTechnician(String workOrderNumber) {

        Optional<Technician_JobCard> optional =
                technicianJobCardRepository.findByWorkOrderNumber(workOrderNumber);
        if (optional.isEmpty())
            throw new UsernameNotFoundException("technician job card not found, may be Invalid job card id");

        return optional.get();
    }
}
