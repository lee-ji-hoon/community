package com.community.mail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {

    private String to; // 누구에게

    private String subject; // 주제목

    private String message; // 내용

}
