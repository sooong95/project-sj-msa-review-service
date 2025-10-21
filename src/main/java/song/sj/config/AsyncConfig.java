package song.sj.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 개수
        executor.setMaxPoolSize(50); // 최대 스레드 개수
        executor.setQueueCapacity(1000); // 대기 큐 크기
        executor.setThreadNamePrefix("Async-");
        executor.initialize();

        return executor;
    }
}
