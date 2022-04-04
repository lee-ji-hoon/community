package com.community.manager;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "ms_id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ManagerSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ms_id;

    @Column(unique = true)
    private LocalDateTime saveTime;

    private int boardIncrease;

    private int studyIncrease;

    private int accountIncrease;

    private int replyIncrease;

    private int marketIncrease;
}
