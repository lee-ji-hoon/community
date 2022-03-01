package com.community.study;

import com.community.account.UserAccount;
import com.community.account.entity.Account;
import com.community.tag.Tag;
import com.community.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(name = "Study.withAll", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")
})

@NamedEntityGraph(name = "Study.withTagsAndManagers", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("managers")
})

@NamedEntityGraph(name = "Study.withZonesAndManagers", attributeNodes = {
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers")
})

@NamedEntityGraph(name = "Study.withManagers", attributeNodes = {
        @NamedAttributeNode("managers")
})

@NamedEntityGraph(name = "Study.withMembers", attributeNodes = {
        @NamedAttributeNode("members")
})

@NamedEntityGraph(name = "Study.withZonesTags", attributeNodes = {
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("tags")
})

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDate limitStudyDate;

    private LocalDate startStudyDate;

    private LocalDate limitMemberDate;

    private LocalDateTime publishedDateTime;

    private boolean useBanner;

    private int memberCount;

    private int limitMember;

    public void addManager(Account account) {
        this.publishedDateTime = LocalDateTime.now();
        this.managers.add(account);
        this.memberCount++;
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.recruiting() && !this.members.contains(account) && !this.managers.contains(account);

    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public String getImage() {
        return image != null ? image : "/images/study-banner.jpeg";
    }

    public boolean openStudy(){

        return isNotOpenAndClosed();
    }

    public boolean endStudy() {
        return isClosed();
    }

    public boolean recruiting() {
        return isRecruiting();
    }

    public boolean isLimitMember() {
        return limitMemberEquals();
    }

    private boolean limitMemberEquals() {
        return this.memberCount >= limitMember;
    }

    private boolean isRecruiting() {
        return this.limitMemberDate.isAfter(LocalDate.now());
    }

    private boolean isClosed() {
        return this.limitStudyDate.isBefore(LocalDate.now());
    }

    private boolean isNotOpenAndClosed() {
        return this.startStudyDate.isBefore(LocalDate.now()) && limitStudyDate.isAfter(LocalDate.now());
    }

    public void recruitClose() {
        if (this.recruiting()) {
            this.limitMemberDate = LocalDate.now();
        }else {
            throw new RuntimeException("스터디원 모집을 종료 할 수 없습니다. 이미 종료됐거나 시작하지 않은 스터디입니다. 다시 확인해주세요.");
        }
    }

    public void addMember(Account account) {
        this.getMembers().add(account);
        this.memberCount++;
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
        this.memberCount--;
    }
}
