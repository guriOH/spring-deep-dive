//package com.example.springbatch.job.chunk.reader;
//
//
//import com.example.springbatch.job.chunk.dto.SampleDTO;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.core.io.FileSystemResource;
//
//import java.nio.charset.StandardCharsets;
//
//public class CustomItemReader {
//
//
//
//    public ItemReader<SampleDTO> getFlatFileItemReader(){
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setStrict(true); // default
//        lineTokenizer.setNames(new String[]{"name", "number"});
//        lineTokenizer.setIncludedFields(new int[]{0,1});
//
//        BeanWrapperFieldSetMapper<SampleDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setStrict(true); // default
//        fieldSetMapper.setTargetType(SampleDTO.class);
//
//
//        DefaultLineMapper<SampleDTO> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        lineMapper.setLineTokenizer(lineTokenizer);
//
//
//        FlatFileItemReader<SampleDTO> flatFileItemReader = new FlatFileItemReader<>();
//        flatFileItemReader.setEncoding(StandardCharsets.UTF_8.name());
//        flatFileItemReader.setResource(getFileSystemResource(csvFilePath));
//        flatFileItemReader.setLineMapper(lineMapper);
//    }
//
//}
