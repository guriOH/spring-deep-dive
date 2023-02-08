package com.example.springbatch.job.chunk.writer;

import com.example.springbatch.job.chunk.dto.SampleDTO;
import com.example.springbatch.persistance.entities.SampleData;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomItemWriter {

    private final EntityManagerFactory entityManagerFactory;

    public FlatFileItemWriter<SampleDTO> readCSVFileWriter() throws Exception {

        //csv파일에 작성할 데이터를 추출하기 위해서 fieldExtractor 객체가 필요
        BeanWrapperFieldExtractor<SampleDTO> extractor = new BeanWrapperFieldExtractor();
        extractor.setNames(new String[] {"id","firstName","lastName"}); //필드명 설정

        //line 구분값 설정
        DelimitedLineAggregator<SampleDTO> lineAggreator = new DelimitedLineAggregator<>();
        lineAggreator.setDelimiter(",");
        lineAggreator.setFieldExtractor(extractor);

        FlatFileItemWriter<SampleDTO> writer = new FlatFileItemWriterBuilder<SampleDTO>()
                .name("csvItemWriter")
                .encoding("UTF-8")
                .resource(new FileSystemResource("output/test.csv")) //경로 지정
                .lineAggregator(lineAggreator)
                .headerCallback(writer1 -> writer1.write("id,name,address")) //header설정
                .footerCallback(writer1 -> writer1.write("---------------\n")) //footer설정
                .build();

        writer.afterPropertiesSet();

        return writer;
    }


    public ItemWriter<SampleData> jpaItemWriter(){
        return new JpaItemWriterBuilder<SampleData>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
