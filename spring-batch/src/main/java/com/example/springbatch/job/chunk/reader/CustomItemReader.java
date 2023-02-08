package com.example.springbatch.job.chunk.reader;


import com.example.springbatch.job.chunk.dto.SampleDTO;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;


public class CustomItemReader {


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

}
