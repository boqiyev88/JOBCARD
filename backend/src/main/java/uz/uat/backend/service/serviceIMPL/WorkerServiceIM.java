package uz.uat.backend.service.serviceIMPL;

import uz.uat.backend.controller.LoginController;
import uz.uat.backend.dto.LoginResponse;
import uz.uat.backend.dto.RequestJob;
import uz.uat.backend.dto.RespJob;

public interface WorkerServiceIM {
    RespJob showTasks(int status);

    LoginResponse login(LoginController.LoginRequest loginRequest);

    RespJob getById(String  jobId);

    RespJob getService(RequestJob requestJob);
}
