package com.example.springbatch.job.chunk;


import com.example.springbatch.job.chunk.dto.SampleDTO;
import com.example.springbatch.job.chunk.writer.CustomItemWriter;
import com.example.springbatch.persistance.entities.SampleData;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlatFileJobConfiguration {

    private final EntityManagerFactory entityManagerFactory;

    private final CustomItemWriter customItemWriter;

    @Bean
    public Job readCSVFileJob(JobRepository jobRepository) throws Exception {
        return new JobBuilder("readCSVFileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(readCSVFileStep(null,null))
                .build();
    }


    @Bean
    public Step readCSVFileStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("readCSVFileStep",jobRepository)
                .<SampleDTO, SampleData>chunk(5, platformTransactionManager)
                .reader(readCSVFileReader())
                .processor(readCSVFileProcessor())
//                .writer(readCSVFileWriter())
                .writer(customItemWriter.jpaItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<? super SampleDTO, ? extends SampleData> readCSVFileProcessor() {
        return (item -> {
            SampleData sampleData = new SampleData();
            sampleData.setFirstName(item.getFirstName());
            sampleData.setLastName(item.getLastName());
            return sampleData;
        });
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<SampleDTO> readCSVFileReader()
    {
        //Create reader instance
        FlatFileItemReader<SampleDTO> reader = new FlatFileItemReader<SampleDTO>();

        //Set input file location
        reader.setResource(new FileSystemResource("input/sourceData.csv"));

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        reader.setStrict(false);
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "id", "firstName", "lastName" });
                    }
                });
                //Set values in SampleDTO class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<SampleDTO>() {
                    {
                        setTargetType(SampleDTO.class);
                    }
                });
            }
        });
        return reader;
    }
//
//    @Bean
//    public FlatFileItemWriter<SampleDTO> readCSVFileWriter() throws Exception {
//
//        //csv파일에 작성할 데이터를 추출하기 위해서 fieldExtractor 객체가 필요
//        BeanWrapperFieldExtractor<SampleDTO> extractor = new BeanWrapperFieldExtractor();
//        extractor.setNames(new String[] {"id","firstName","lastName"}); //필드명 설정
//
//        //line 구분값 설정
//        DelimitedLineAggregator<SampleDTO> lineAggreator = new DelimitedLineAggregator<>();
//        lineAggreator.setDelimiter(",");
//        lineAggreator.setFieldExtractor(extractor);
//
//        FlatFileItemWriter<SampleDTO> writer = new FlatFileItemWriterBuilder<SampleDTO>()
//            .name("csvItemWriter")
//            .encoding("UTF-8")
//            .resource(new FileSystemResource("output/test.csv")) //경로 지정
//            .lineAggregator(lineAggreator)
//            .headerCallback(writer1 -> writer1.write("id,name,address")) //header설정
//            .footerCallback(writer1 -> writer1.write("---------------\n")) //footer설정
//            .build();
//
//        writer.afterPropertiesSet();
//
//        return writer;
//    }
//
//
//
//    @Bean
//    public ItemWriter<SampleData> jpaItemWriter(){
//        return new JpaItemWriterBuilder<SampleData>()
//                .entityManagerFactory(entityManagerFactory)
//                .build();
//    }
}



