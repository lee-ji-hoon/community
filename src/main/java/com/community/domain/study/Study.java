package com.community.domain.study;

import com.community.domain.account.UserAccount;
import com.community.domain.account.Account;
import com.community.domain.tag.Tag;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@NamedEntityGraph(name = "Study.withAll", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")
})

@NamedEntityGraph(name = "Study.withTagsAndManagers", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("managers")
})

@NamedEntityGraph(name = "Study.withTagsAndMembers", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("members")
})

@NamedEntityGraph(name = "Study.withManagers", attributeNodes = {
        @NamedAttributeNode("managers")
})

@NamedEntityGraph(name = "Study.withMembers", attributeNodes = {
        @NamedAttributeNode("members")
})

@NamedEntityGraph(name = "Study.withTags", attributeNodes = {
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "study_id")
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @ManyToMany
    private Set<Account> blockMembers = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    private String managerEmail;

    private String studyMethod;

    private String studyPlaces;

    private String image;

    private String imageKey;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meetings> meetingsList = new ArrayList<>();

    private LocalDate limitStudyDate;

    private LocalDate startStudyDate;

    private LocalDate limitMemberDate;

    private LocalDateTime publishedDateTime;

    private LocalDateTime recentAlarmDateTime;

    private int memberCount;

    private int limitMember;

    public void addManager(Account account) {
        this.publishedDateTime = LocalDateTime.now();
        this.managers.add(account);
        this.memberCount++;
    }

    public void addBlockMembers(Account account) {
        this.blockMembers.add(account);
    }

    public boolean checkAlarmDateTime() {
        return this.recentAlarmDateTime.isBefore(LocalDateTime.now().minusHours(24));
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

    public boolean openStudy() {

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

    public void addMember(Account account) {
        this.getMembers().add(account);
        this.memberCount++;
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
        this.memberCount--;
    }

    public String getEncodePath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }
}
