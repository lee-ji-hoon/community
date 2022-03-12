package com.community.council;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class CouncilForm {

    @NotBlank
    @Length(max = 50)
    private String postTitle;

    private String postTarget;

    private String postLink;

    private String contactNum;

    private String postSort;

    @NotBlank
    private String postContent;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventEndDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyPeriodStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyPeriodEndDate;
}
