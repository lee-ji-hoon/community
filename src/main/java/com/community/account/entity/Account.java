package com.community.account.entity;

import com.community.alarm.Alarm;
import com.community.board.service.BoardService;
import com.community.like.Likes;
import com.community.market.Market;
import com.community.tag.Tag;
import com.community.study.entity.Study;
import lombok.*;
import org.hibernate.annotations.Type;
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
@NoArgsConstructor
//@ToString
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

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.EAGER)
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Market> marketsList = new ArrayList<>();

    @Lob
    @Type(type = "text")
    private String profileImage;

    // 임시 알림 설정
    private boolean studyCreatedByEmail = false;

    private boolean studyCreatedByWeb = true;

    private boolean replyByMeetings = true;

    private boolean replyByPost = true;

    private boolean likesByPost = true;
    
    private boolean replyCreateByWeb = true;

    // TODO
    // 알림 추가

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

    public boolean isMemeber(Study byPath) {
        return byPath.getMembers().contains(this);
    }

    public String dateTime(LocalDateTime localDateTime){
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < BoardService.SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= BoardService.SEC) < BoardService.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= BoardService.MIN) < BoardService.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= BoardService.HOUR) < BoardService.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= BoardService.DAY) < BoardService.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
}
