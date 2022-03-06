package com.community.forum;

import com.community.account.entity.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "fr_id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForumReply {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long fr_id;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "answerer_id")
    private Account postAnswerer;

    private LocalDateTime answerUploadTime;

    @Column(columnDefinition = "TEXT")
    private String answerContent;
}
