package uz.uat.backend.config.executorConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig {

    @Bean(autowireCandidate = false)
    public ExecutorService executorService() {
        int cpuCores = Runtime.getRuntime().availableProcessors(); // Kompyuterning CPU yadrolari soni

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cpuCores); // Asosiy threadlar soni — yadrolar soniga teng
        executor.setMaxPoolSize(cpuCores * 2); // Maksimal threadlar soni — ikki barobar ko‘p
        executor.setQueueCapacity(500); // Navbat hajmi — ko‘proq yukni qo‘llab-quvvatlash uchun
        executor.setThreadNamePrefix("AppExecutor-"); // Thread nomi prefiksi
        executor.initialize();

        return executor.getThreadPoolExecutor();
    }
}

