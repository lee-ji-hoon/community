package com.community.domain.study;

import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meetings_id")
    private Long meetingsId;

    private String meetingTitle;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String meetingDescription;

    @ManyToOne(targetEntity = Study.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Account writer;

    @OneToMany(mappedBy = "meetings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    private String meetingMethod;

    private String meetingPlaces;

    private LocalDateTime uploadTime;
}
