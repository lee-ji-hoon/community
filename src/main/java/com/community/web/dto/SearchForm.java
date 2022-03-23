package com.community.web.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class SearchForm {

    private String searchType;

    private String keyword;

}
