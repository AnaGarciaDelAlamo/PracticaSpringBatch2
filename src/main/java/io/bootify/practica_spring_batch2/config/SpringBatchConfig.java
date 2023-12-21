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
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

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
        itemReader.setResource(new FileSystemResource("src/main/resources/transacciones.csv"));
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

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CustomFieldSetMapper());
        return lineMapper;
    }


    public class CustomFieldSetMapper implements FieldSetMapper<Transaccion> {
        @Override
        public Transaccion mapFieldSet(FieldSet fieldSet) {
            Transaccion transaccion = new Transaccion();
            Long idValue = readLongOrNull(fieldSet, "id");
            transaccion.setId(idValue != null ? idValue : 0L);

            Date fechaValue = fieldSet.readDate("fecha", "yyyy-MM-dd");
            transaccion.setFecha(fechaValue != null ? fechaValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);

            Double montoValue = readDoubleOrNull(fieldSet, "monto");
            transaccion.setMonto(montoValue != null ? montoValue : 0.0);

            transaccion.setTipoTransaccion(fieldSet.readString("tipoTransaccion"));
            transaccion.setCuentaOrigen(fieldSet.readString("cuentaOrigen"));
            transaccion.setCuentaDestino(fieldSet.readString("cuentaDestino"));
            transaccion.setDateCreated(OffsetDateTime.now());
            transaccion.setLastUpdated(OffsetDateTime.now());

            return transaccion;
        }

        private Long readLongOrNull(FieldSet fieldSet, String fieldName) {
            try {
                return fieldSet.readLong(fieldName);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private Double readDoubleOrNull(FieldSet fieldSet, String fieldName) {
            try {
                return fieldSet.readDouble(fieldName);
            } catch (NumberFormatException e) {
                return null;
            }
        }
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
                .<Transaccion, Transaccion>chunk(5000, transactionManager)
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