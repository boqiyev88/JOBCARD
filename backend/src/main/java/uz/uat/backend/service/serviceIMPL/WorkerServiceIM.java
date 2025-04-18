package uz.uat.backend.service.serviceIMPL;

import uz.uat.backend.controller.LoginController;
import uz.uat.backend.dto.LoginResponse;

public interface WorkerServiceIM {
    void showTasks(int status);

    LoginResponse login(LoginController.LoginRequest loginRequest);
}
