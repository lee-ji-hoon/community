package com.community.study.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    private String meetingTitle;

    private String meetingDescription;

    @ManyToOne(targetEntity = Study.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    private LocalDateTime localDateTime;

}
