package uz.uat.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@SpringBootApplication
@EnableScheduling
public class BackendApplication {
    public final static String MODEL_PACKAGE = "uz.uat.backend.model";
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
