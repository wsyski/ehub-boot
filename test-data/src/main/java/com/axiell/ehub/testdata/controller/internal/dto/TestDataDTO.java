package com.axiell.ehub.testdata.controller.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestDataDTO {
    private Long ehubConsumerId;
    private String ehubConsumerSecretKey;
    private long ehubLoanId;
    private String patronId;
    private String libraryCard;
    private String pin;
    private String email;
    private String name;
    private LocalDate birthDate;


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}


