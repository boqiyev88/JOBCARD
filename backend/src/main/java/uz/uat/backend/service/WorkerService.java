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
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.controller.LoginController;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.LoginResponse;
import uz.uat.backend.dto.RespJob;
import uz.uat.backend.dto.ResultCode;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.User;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.WorkRepository;
import uz.uat.backend.service.serviceIMPL.WorkerServiceIM;
import uz.uat.backend.service.utils.UtilsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService implements WorkerServiceIM {

    private final JobCarRepository jobCarRepository;
    private final UtilsService utilsService;
    private final WorkRepository workRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

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
                    return new RespJob(new ResultCode(401, "CONFLICT"), "invalid status code",
                            utilsService.getJobCards(new ArrayList<>()));
                }
            }
        } else
            return new RespJob(new ResultCode(401, "CONFLICT"), "invalid status", new ArrayList<>());
    }

    @Override
    public LoginResponse login(LoginController.LoginRequest loginRequest) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Spring Security tomonidan yaratilgan foydalanuvchini olish
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Agar UserDetails'dan haqiqiy User obyektini olish kerak bo‘lsa
            User user = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());

            // JWT token generatsiya qilish
            String token = utilsService.generateJwtToken(user);

            return ResponseEntity.ok(Map.of(
                    "jwtToken", token,
                    "user", user,
                    "redirect", "/"
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        ///  username va parol tekshirilishi kerak
        /// newTask,InProcess, closed, message larni counti qaytishi kerak
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
                        401, "CONFLICT"),
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
