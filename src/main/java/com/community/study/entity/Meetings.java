package com.community.study.entity;

import com.community.account.entity.Account;
import com.community.board.entity.Reply;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "meetingsId")
public class Meetings {

    @Id
    @GeneratedValue
    @Column(name = "meetings_id")
    private Long meetingsId;

    private String meetingTitle;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String meetingDescription;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Account writer;

    @OneToMany(mappedBy = "meetings")
    private List<Reply> replyList = new ArrayList<>();

    private String meetingMethod;

    private String meetingPlaces;

    private LocalDateTime uploadTime;
}
