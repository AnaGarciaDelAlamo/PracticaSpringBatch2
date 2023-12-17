package io.bootify.practica_spring_batch2.config;

import io.bootify.practica_spring_batch2.domain.Transaccion;
import io.bootify.practica_spring_batch2.repos.TransaccionRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {


    private JobRepository jobRepository;


    private PlatformTransactionManager transactionManager;

    private TransaccionRepository transaccionRepository;

    @Bean
    public FlatFileItemReader<Transaccion> reader() {
        FlatFileItemReader<Transaccion> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("transacciones.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Transaccion> lineMapper() {
        DefaultLineMapper<Transaccion> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "fecha", "monto", "tipoTransaccion", "cuentaOrigen", "cuentaDestino");

        BeanWrapperFieldSetMapper<Transaccion> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Transaccion.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public TransaccionProcessor processor() {
        return new TransaccionProcessor();
    }

    @Bean
    public RepositoryItemWriter<Transaccion> writer() {
        RepositoryItemWriter<Transaccion> writer = new RepositoryItemWriter<>();
        writer.setRepository(transaccionRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(ItemReader<Transaccion> reader, ItemProcessor<Transaccion, Transaccion> processor, ItemWriter<Transaccion> writer) {
       return new StepBuilder("csv-step", jobRepository)
               .<Transaccion, Transaccion>chunk(10, transactionManager)
               .reader(reader)
               .processor(processor)
               .writer(writer)
               .taskExecutor(taskExecutor())
               .build();
    }

    @Bean
    public Job runJob(Step step1) {
        return new JobBuilder("importTransacciones", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
