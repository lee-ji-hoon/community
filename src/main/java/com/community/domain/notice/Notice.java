package com.community.domain.notice;

import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import com.community.domain.bookmark.Bookmark;
import com.community.infra.aws.S3;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "notice_id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue
    @Column(name = "notice_id")
    private Long notice_id;

    private LocalDateTime uploadTime;

    private String noticeTitle;

    @Lob
    @Type(type = "text")
    @Basic(fetch = FetchType.EAGER)
    private String noticeContent;

    private Boolean topSetting;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3> imageList = new ArrayList<>();
}
