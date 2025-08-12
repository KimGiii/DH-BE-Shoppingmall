package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchSchedulerService {

    private final JobLauncher jobLauncher;
    private final Job dailySalesJob;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void runDailySalesJob() {
        try {
            jobLauncher.run(
                    dailySalesJob,
                    new JobParametersBuilder()
                            .addString("runDate", LocalDateTime.now().toString())
                            .toJobParameters()
            );
        } catch (Exception e) {
            log.error("Failed to run dailySalesJob", e);
        }
    }
}
