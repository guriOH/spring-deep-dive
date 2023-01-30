package com.example.springbatch.job.chunk.listener;


import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@StepScope
@Component
@Getter
@Setter
public class CompleteStepListener implements StepExecutionListener {

    private int count;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        System.out.println("count : " + count);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        this.setCount(count+1);
        return StepExecutionListener.super.afterStep(stepExecution);
    }
}
