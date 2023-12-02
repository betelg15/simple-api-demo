package com.example.demo.batch.job;

import com.example.demo.persistence.repository.PaymentSettlementRepository;
import com.example.demo.persistence.repository.entity.PaymentSettlementEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SampleJobConfig {

    public static final String JOB_NAME = "sampleJob";
    public static final String STEP_NAME = "sampleStep";
    public static final String READER_NAME = "sampleReader";
    public static final String PROCESSOR_NAME = "sampleProcessor";
    public static final String WRITER_NAME = "sampleWriter";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    SampleJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean(JOB_NAME)
    public Job job(@Qualifier(STEP_NAME) Step step) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .preventRestart()
                .start(step)
                .build();
    }

    @Bean(STEP_NAME)
    public Step step(
            @Qualifier(READER_NAME) ItemReader<Integer> reader,
            @Qualifier(PROCESSOR_NAME) ItemProcessor<Integer, PaymentSettlementEntity> processor,
            @Qualifier(WRITER_NAME) ItemWriter<PaymentSettlementEntity> writer
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Integer, PaymentSettlementEntity>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @StepScope
    @Bean(READER_NAME)
    public JdbcCursorItemReader<Integer> reader(
            DataSource dataSource,
            @Value("#{jobParameters['execAt']}") String execAtParam
    ) {
        String sql = """
            SELECT
                SUM(amount) AS total_amount
            FROM
                payment
            WHERE
                created_at >= :start
                AND created_at < :end
        """;

        Map<String, Object> paramMap = new HashMap<>();

        if (execAtParam != null) {
            LocalDateTime execAt = LocalDateTime.parse(execAtParam);

            paramMap.put("start", execAt.minusDays(1));
            paramMap.put("end", execAt);
        }

        String parameterizedSql = NamedParameterUtils.substituteNamedParameters(
                sql,
                new MapSqlParameterSource(paramMap)
        );

        JdbcCursorItemReader<Integer> cursorItemReader = new JdbcCursorItemReader<>();
        cursorItemReader.setDataSource(dataSource);
        cursorItemReader.setSql(parameterizedSql);
        cursorItemReader.setPreparedStatementSetter(
                new ArgumentPreparedStatementSetter(
                        NamedParameterUtils.buildValueArray(sql, paramMap)
                )
        );
        cursorItemReader.setVerifyCursorPosition(false);
        cursorItemReader.setFetchSize(Integer.MIN_VALUE); // rows are read one by one
        cursorItemReader.setRowMapper((rs, rowNum) ->
                rs.getInt("total_amount")
        );

        return cursorItemReader;
    }

    @StepScope
    @Bean(PROCESSOR_NAME)
    public ItemProcessor<Integer, PaymentSettlementEntity> processor(
            @Value("#{jobParameters['execAt']}") String execAtParam
    ) {
        LocalDateTime execAt = LocalDateTime.parse(execAtParam);
        LocalDateTime now = LocalDateTime.now();
        return item -> new PaymentSettlementEntity(
                null,
                execAt.minusDays(1),
                BigDecimal.valueOf(item),
                now
        );
    }

    @Bean(WRITER_NAME)
    public ItemWriter<PaymentSettlementEntity> writer(
            PaymentSettlementRepository paymentSettlementRepository
    ) {
        // PK로 AUTO_INCREMENT를 사용한다면 여기에서는 JPA로 벌크 INSERT를 할 수 없으니
        // 대량 데이터라면 네이티브 쿼리나 SQLQueryFactory로 벌크 INSERT 하도록 한다.
        return chunk -> chunk.forEach(paymentSettlementRepository::save);
    }
}
