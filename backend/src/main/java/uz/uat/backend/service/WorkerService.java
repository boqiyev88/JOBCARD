package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.controller.LoginController;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.LoginResponse;
import uz.uat.backend.dto.RespJob;
import uz.uat.backend.dto.ResultCode;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.service.serviceIMPL.WorkerServiceIM;
import uz.uat.backend.service.utils.UtilsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService implements WorkerServiceIM {

    private final JobCarRepository jobCarRepository;
    private final UtilsService utilsService;

    @Override
    public RespJob showTasks(int status) {
        if (status >= 2 && status <= 4) {
            switch (status) {
                case 2 -> {
                    return getJobsByStatus(Status.PENDING);
                }
                case 3 -> {
                    return getJobsByStatus(Status.IN_PROCESS);
                }
                case 4 -> {
                    return getJobsByStatus(Status.COMPLETED);
                }
                default -> {
                    ///  home page qaytishi kerak
                    return null;
                }

            }
        } else
            return new RespJob(new ResultCode(401, "CONFLICT"), "invalid status", new ArrayList<>());
    }

    @Override
    public LoginResponse login(LoginController.LoginRequest loginRequest) {
        ///  username va parol tekshirilishi kerak
        return null;
    }

    @Override
    public RespJob getById(String jobId) {
        if (jobId.isBlank())
            return new RespJob(new ResultCode(401, "CONFLICT"), "jobId must not be empty", new ArrayList<>());
        JobCard jobCard = jobCarRepository.findByJobCardId(jobId);
        if (jobCard == null)
            return new RespJob(new ResultCode(404, "NOT FOUND"), "jobId  not found by this id: " + jobId, new ArrayList<>());

        return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY", utilsService.getJobWithService(jobCard.getId()));
    }


    @Override
    public RespJob getService(String serviceId) {
        if (serviceId.isBlank())
            throw new MyNotFoundException("service id must not be empty");

        return null;
    }

    private RespJob getJobsByStatus(Status status) {
        Page<JobCard> page = jobCarRepository.findBySTATUS(status, PageRequest.of(0, 20));
        if (page.getContent().isEmpty())
            return new RespJob(new ResultCode(404, "NOT FOUND"), "job not found by this status", new ArrayList<>());
        List<JobCard> jobCards = page.getContent();
        return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY", utilsService.getJobCards(jobCards));
    }

}
