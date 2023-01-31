package com.example.springbatch.job.partitioning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SamplePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {



        return null;
    }
}
