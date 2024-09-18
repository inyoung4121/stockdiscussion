package in.stockcrawlingservice.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobExecution;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@EnableScheduling
public class StockDataFetcherJob {

    private final JobRepository jobRepository;

    private final JobLauncher jobLauncher;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job fetchStockDataJob() {
        return new JobBuilder("fetchStockDataJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fetchStockDataStep())
                .build();
    }

    @Bean
    public Step fetchStockDataStep() {
        return new StepBuilder("fetchStockDataStep", jobRepository)
                .<String, StockData>chunk(1, transactionManager)
                .reader(stockSymbolReader())
                .processor(stockDataProcessor())
                .writer(stockDataWriter())
                .build();
    }

    @Bean
    public ListItemReader<String> stockSymbolReader() {
        List<String> symbols = Arrays.asList("005930", "000660", "035720");
        return new ListItemReader<>(symbols);
    }

    @Bean
    public ItemProcessor<String, StockData> stockDataProcessor() {
        return new StockDataProcessor();
    }

    @Bean
    public ItemWriter<StockData> stockDataWriter() {
        return new StockDataWriter();
    }

    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(fetchStockDataJob(), jobParameters);
        System.out.println("Job Status : " + execution.getStatus());
    }
}
