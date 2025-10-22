package song.sj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "feignExecutor")
    public Executor feignExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);        // 평상시 병렬 처리량
        executor.setMaxPoolSize(60);         // 트래픽 급증 시 허용 최대
        executor.setQueueCapacity(500);      // 대기 큐 (너무 크면 응답지연 발생)
        executor.setThreadNamePrefix("Feign-Async-");
        // 포화 시 호출 스레드가 직접 처리해서 실패율 폭발 방지
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
