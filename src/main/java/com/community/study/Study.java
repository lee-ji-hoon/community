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

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    private int memberCount;

    private int limitMember;

    public void addManager(Account account) {
        this.managers.add(account);
        this.memberCount++;
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting()
                && !this.members.contains(account) && !this.managers.contains(account);

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

    public void publish() {
        if (!this.closed && !this.published) {
            this.published = true;
            this.publishedDateTime = LocalDateTime.now();
        }else{
            throw new RuntimeException("스터디를 공개 할 수 없는 상태입니다. 이미 종료됐거나 공개된 스터디인지 다시 확인해주세요");
        }
    }

    public void close() {
        if (this.published) {
            this.published = false;
            this.closedDateTime = LocalDateTime.now();
        }else{
            throw new RuntimeException("스터디를 종료 할 수 없습니다. 존재하지 않거나 이미 종료된 스터디입니다. 다시 확인해주세요.");
        }
    }

    public void recruitPublish() {
        if (!this.recruiting && !this.closed && this.published){
            this.recruiting = true;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        }else {
            throw new RuntimeException("스터디원 모집을 시작 할 수 없습니다. 종료됐거나 공개되지 않은 스터디입니다. 다시 확인해주세요.");
        }
    }

    public void recruitClose() {
        if (this.recruiting && !this.closed && this.published) {
            this.recruiting = false;
            this.recruitingUpdatedDateTime = LocalDateTime.now();
        }else {
            throw new RuntimeException("스터디원 모집을 종료 할 수 없습니다. 이미 종료됐거나 시작하지 않은 스터디입니다. 다시 확인해주세요.");
        }
    }

    public boolean isRemovable() {
        return !this.published;
    }

    public void addMember(Account account) {
        this.getMembers().add(account);
        this.memberCount++;
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
        this.memberCount--;
    }

    public boolean UpdateRecruiting() {
        return this.published && this.recruitingUpdatedDateTime == null || this.recruitingUpdatedDateTime.isBefore(LocalDateTime.now().minusMinutes(30));
    }
}
