package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.controller.LoginController;
import uz.uat.backend.dto.ResponseLogin;
import uz.uat.backend.dto.ResultCode;
import uz.uat.backend.model.User;
import uz.uat.backend.service.utils.UtilsService;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final UtilsService utilsService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;


    public ResponseLogin login(LoginController.LoginRequest loginRequest) {
        User user =(User) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        System.err.println("in user :"+user.getUsername());
        System.err.println("in loginRequest :"+loginRequest.getUsername());

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        );

        String token = utilsService.generateJwtToken(user);
        if (!passwordMatches) {
            return ResponseLogin.builder()
                    .resultCode(ResultCode.builder()
                            .code(404)
                            .resultMessage("Invalid password")
                            .build())
                    .User(null)
                    .token(null)
                    .build();
        }

        return  ResponseLogin.builder()
                .resultCode(ResultCode.builder()
                        .code(200)
                        .resultMessage("Successfully logged")
                        .build())
                .User(user)
                .token(token)
                .build();
    }
}
