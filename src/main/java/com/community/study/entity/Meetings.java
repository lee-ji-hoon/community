package com.community.study.entity;

import com.community.account.entity.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Meetings {

    @Id
    @GeneratedValue
    @Column(name = "meetings_id")
    private Long id;

    private String meetingDivision;

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

    private String meetingMethod;

    private String meetingPlaces;

    private LocalDateTime uploadTime;

}
