package com.example.springbasic.repeat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RepeatStepExampleDomain {
    private String id;
    private String data;
}
