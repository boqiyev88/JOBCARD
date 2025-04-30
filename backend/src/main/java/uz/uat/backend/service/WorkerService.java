package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.controller.LoginController;
import uz.uat.backend.dto.*;
import uz.uat.backend.mapper.JobCardMapper;
import uz.uat.backend.model.*;
import uz.uat.backend.model.enums.RoleName;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.UserRepository;
import uz.uat.backend.repository.WorkRepository;
import uz.uat.backend.service.serviceIMPL.WorkerServiceIM;
import uz.uat.backend.service.utils.UtilsService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService implements WorkerServiceIM {

    private final JobCarRepository jobCarRepository;
    private final UtilsService utilsService;
    private final WorkRepository workRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JobCardMapper jobCardMapper;

    @Override
    public RespJob showTasks(int status) {
        if (status >= 1 && status <= 4) {
            switch (status) {
                case 1 -> {
                    return getJobsByStatus(Status.NEW, 1);
                }
                case 2 -> {
                    return getJobsByStatus(Status.IN_PROCESS, 2);
                }
                case 3 -> {
                    return getJobsByStatus(Status.CONFIRMED, 3);
                }
                case 4 -> {
                    return getJobsByStatus(Status.REJECTED, 4);
                }
                default -> {
                    return new RespJob(new ResultCode(409, "CONFLICT"), "invalid status code",
                            utilsService.getJobCards(new ArrayList<>()));
                }
            }
        } else
            return new RespJob(new ResultCode(409, "CONFLICT"), "invalid status", new ArrayList<>());
    }

    @Override
    public LoginResponse login(LoginController.LoginRequest loginRequest) {
        User user = (User) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return LoginResponse.builder()
                    .resultCode(ResultCode.builder()
                            .code(409)
                            .resultMessage("INCORRECT PASSWORD")
                            .build())
                    .resultNote("incorrect password")
                    .build();
        }
        ResponseDto count = utilsService.getWorkStatusCount();
        System.err.println("-------------------------------------------------------------------");
        Set<Role> roles = user.getRoles();
        return LoginResponse.builder()
                .resultCode(ResultCode.builder()
                        .code(200)
                        .resultMessage("SUCCESSFULLY")
                        .build())
                .resultNote("SUCCESSFULLY")
                .name(user.getFirstName())
                .roleName(roles.stream().map(Role::getName).toList().toString())
                .newTaskCount(count.New())
                .inProcessTaskCount(count.New())
                .closedTaskCount(count.New())
                .messageCount(null)
                .token(utilsService.generateJwtToken(user))
                .build();
    }


    @Override
    public RespJob getById(String jobId) {
        if (jobId.isBlank())
            return new RespJob(new ResultCode(409, "CONFLICT"), "jobId must not be empty", new ArrayList<>());
        JobCard jobCard = jobCarRepository.findByJobCardId(jobId);
        if (jobCard == null)
            return new RespJob(new ResultCode(404, "NOT FOUND"), "jobId  not found by this id: " + jobId, new ArrayList<>());

        return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY", utilsService.getJobWithService(jobCard.getId()));
    }


    @Override
    public RespJob getService(RequestJob requestJob) {
        if (requestJob.jobId().isBlank() || requestJob.serviceId().isBlank()) {
            return new RespJob(
                    ResultCode.builder()
                            .code(409)
                            .resultMessage("INCORRECT JOB OR SERIVCE ID")
                            .build(),
                    "Ids must not be empty",
                    new ArrayList<>());
        }

        Optional<Work> optionalWork = workRepository.findByJobcardAndSeviceId(requestJob.jobId(), requestJob.serviceId());
        if (optionalWork.isEmpty()) {
            return new RespJob(
                    ResultCode.builder()
                            .code(404)
                            .resultMessage("NO WORK FOUND")
                            .build(),
                    "with these ids not found",
                    new ArrayList<>());
        }
        Work work = optionalWork.get();
        ResponseJobCardDto respJob = utilsService.getJobCard(work.getJobcard_id(), new Message());
        ResponseWorkDto workDto = utilsService.getWork(work);
        ResultJob resultJob = jobCardMapper.fromDto(respJob);
        ResponseServiceDto serviceDto = utilsService.fromEntityService(work.getService_id());
        resultJob.setWork(workDto);
        resultJob.setServices(serviceDto);
        return new RespJob(
                ResultCode.builder()
                        .code(200)
                        .resultMessage("OK")
                        .build(),
                "SUCCESFULLY",
                resultJob);
    }

    private RespJob getJobsByStatus(Status status, int statusCode) {
        Page<JobCard> page = jobCarRepository.findBySTATUS(status, PageRequest.of(0, 20));
        if (page.getContent().isEmpty())
            return new RespJob(new ResultCode(404, "NOT FOUND"), "job not found by this status", new ArrayList<>());
        List<JobCard> jobCards = page.getContent();

        switch (statusCode) {
            case 1 -> {
                return findNewWorksByJobIds(Status.NEW, jobCards);
            }
            case 2 -> {
                return findNewWorksByJobIds(Status.IN_PROCESS, jobCards);
            }
            case 3 -> {
                return findNewWorksByJobIds(Status.CONFIRMED, jobCards);
            }
            case 4 -> {
                return findNewWorksByJobIds(Status.REJECTED, jobCards);
            }
            default -> {
                return new RespJob(new ResultCode(
                        409, "CONFLICT"),
                        "invalid status code",
                        utilsService.getJobCards(new ArrayList<>()));
            }
        }
    }

    private RespJob findNewWorksByJobIds(Status status, List<JobCard> jobCards) {
        List<Work> works = workRepository
                .findNewWorksByJobIds(
                        status, jobCards.stream().map(JobCard::getId).collect(Collectors.toSet()));

        if (works.isEmpty())
            return new RespJob(new ResultCode(404, "NOT FOUND"), "works not found by this status: " + status,
                    utilsService.getJobCards(works.stream().map(Work::getJobcard_id).toList()));

        return new RespJob(new ResultCode(200, "OK"), "SUCCESFULLY",
                utilsService.getJobCards(works.stream().map(Work::getJobcard_id).toList()));

    }

}
