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
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.WorkRepository;
import uz.uat.backend.service.serviceIMPL.WorkerServiceIM;
import uz.uat.backend.service.utils.UtilsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService implements WorkerServiceIM {

    private final JobCarRepository jobCarRepository;
    private final UtilsService utilsService;
    private final WorkRepository workRepository;

    @Override
    public RespJob showTasks(int status) {
        if (status >= 2 && status <= 4) {
            switch (status) {
                case 2 -> {
                    return getJobsByStatus(Status.IN_PROCESS, 2);
                }
                case 3 -> {
                    return getJobsByStatus(Status.IN_PROCESS, 3);
                }
                case 4 -> {
                    return getJobsByStatus(Status.CONFIRMED, 4);
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
        /// newTask,InProcess, closed, message larni counti qaytishi kerak
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

    private RespJob getJobsByStatus(Status status, int statusCode) {
        Page<JobCard> page = jobCarRepository.findBySTATUS(status, PageRequest.of(0, 20));
        if (page.getContent().isEmpty())
            return new RespJob(new ResultCode(404, "NOT FOUND"), "job not found by this status", new ArrayList<>());
        List<JobCard> jobCards = page.getContent();

        switch (statusCode) {

            case 2 -> {
                List<Work> works = workRepository
                        .findNewWorksByJobIds(
                                Status.NEW, jobCards.stream().map(JobCard::getId).collect(Collectors.toSet()));

                return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY",
                        utilsService.getJobCards(works.stream().map(Work::getJobcard_id).toList()));
            }
            case 3 -> {
                List<Work> works = workRepository
                        .findNewWorksByJobIds(
                                Status.IN_PROCESS, jobCards.stream().map(JobCard::getId).collect(Collectors.toSet()));

                return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY",
                        utilsService.getJobCards(works.stream().map(Work::getJobcard_id).toList()));
            }
            case 4 -> {
                List<Work> works = workRepository
                        .findNewWorksByJobIds(
                                Status.CONFIRMED, jobCards.stream().map(JobCard::getId).collect(Collectors.toSet()));

                return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY",
                        utilsService.getJobCards(works.stream().map(Work::getJobcard_id).toList()));
            }
            default -> {
                return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY",
                        utilsService.getJobCards(new ArrayList<>()));
            }
        }
    }

}
