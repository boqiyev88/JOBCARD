package uz.uat.backend.config.executorConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        int poolSize = Runtime.getRuntime().availableProcessors();
        int maxQueueSize = 100;
        return new ThreadPoolExecutor(
                poolSize,
                poolSize * 2,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(maxQueueSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}

