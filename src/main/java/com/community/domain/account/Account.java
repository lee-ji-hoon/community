package com.community.domain.account;

import com.community.service.BoardService;
import com.community.domain.likes.Likes;
import com.community.domain.market.Market;
import com.community.domain.tag.Tag;
import com.community.domain.study.Study;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j

@NamedEntityGraph(name = "Account.withTags", attributeNodes = {
        @NamedAttributeNode("tags")
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String studentId;

    private String username;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private String major;

    private String grade;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Market> marketsList = new ArrayList<>();

    private String profileImage;

    private String profileImageKey;

    // 임시 알림 설정
    private boolean studyCreatedByEmail = false;

    private boolean studyCreatedByWeb = true;

    private boolean replyByMeetings = true;

    private boolean replyByPost = true;

    private boolean replyByMarket = true;

    private boolean likesByPost = true;

    private boolean replyCreateByWeb = true;
    // 알림 설정 끝

    // 태그
    @ManyToMany
    @Column(name = "account_tags")
    private Set<Tag> tags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    // 이메일 체크 토큰 랜덤 생성 및 시간 체크
    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    // 이메일인증까지 완료한 회원
    public void completeEmailCheck() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    // 이메일 체크 토큰 확인
    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    // 이메일 1시간마다만 보내지게끔
    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return passwordEncoder.matches(checkPassword, getPassword());
    }

    public boolean isManager(Study byPath) {
        return byPath.getManagers().contains(this);
    }

    public boolean isMemeber(Study byPath) {
        return byPath.getMembers().contains(this);
    }

}
