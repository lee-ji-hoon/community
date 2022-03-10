package com.community.account.entity;

import com.community.like.Likes;
import com.community.market.Market;
import com.community.tag.Tag;
import com.community.study.entity.Study;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "community", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1,
        allocationSize = 1)

@NamedEntityGraph(name = "Account.withTags", attributeNodes = {
        @NamedAttributeNode("tags")
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "MEMBER_SEQ_GENERATOR")
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

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String bannerImage;

    public String getBannerImage() {
        return bannerImage != null ? bannerImage : "/images/study-banner.jpeg";
    }

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Market> markets = new ArrayList<>();

    @Lob
    @Type(type = "text")
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    // 임시 알림 설정
    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;
    // TODO
    // 이메일 등 장터로 바꿔야
    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    // 알림 설정 끝

    // 태그
    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

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
}
