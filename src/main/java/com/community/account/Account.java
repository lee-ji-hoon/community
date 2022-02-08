package com.community.account;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @GeneratedValue
    private Long id; // 아이디

    @NotNull
    @Column(unique = true)
    private String email; // 이메일

    @NotNull
    @Column(unique = true)
    private String studentId; // 학번

    @NotNull
    @Column(unique = true)
    private String nickname; // 회원 닉네임

    @NotNull
    private String password; // 비밀번호

    private String profileImage; // 회원 이미지

}
