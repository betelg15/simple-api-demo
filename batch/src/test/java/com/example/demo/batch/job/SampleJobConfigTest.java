package com.example.demo.batch.job;

import com.example.demo.persistence.repository.PaymentRepository;
import com.example.demo.persistence.repository.PaymentSettlementRepository;
import com.example.demo.persistence.repository.entity.PaymentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@SpringBootTest
public class SampleJobConfigTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier(SampleJobConfig.JOB_NAME)
    @Autowired
    private Job job;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentSettlementRepository paymentSettlementRepository;

    @BeforeEach
    void beforeEach() {
        paymentRepository.deleteAllInBatch();
        paymentSettlementRepository.deleteAllInBatch();
    }

    @DisplayName("결제 마감을 생성할 수 있어야 한다")
    @Test
    void sampleJob() throws Exception {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);

        paymentRepository.saveAll(
                new ArrayList<>() {{
                    add(
                            new PaymentEntity(
                                    null,
                                    1L,
                                    1L,
                                    "CARD",
                                    "SUCCESS",
                                    new BigDecimal("1000"),
                                    LocalDateTime.parse("2023-12-01T00:00:00")
                            )
                    );
                    add(
                            new PaymentEntity(
                                    null,
                                    1L,
                                    2L,
                                    "CARD",
                                    "SUCCESS",
                                    new BigDecimal("500"),
                                    LocalDateTime.parse("2023-12-01T01:00:00")
                            )
                    );
                }}
        );

        jobLauncherTestUtils.setJob(job);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                new JobParameters(
                        new HashMap<>() {{
                            put("uuid", new JobParameter<>(UUID.randomUUID().toString(), String.class));
                            put("execAt", new JobParameter<>("2023-12-01T15:00:00", String.class));
                        }}
                )
        );

        Assertions.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
    }
}
