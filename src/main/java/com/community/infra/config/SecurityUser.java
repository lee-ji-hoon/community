package com.community.infra.config;

import com.community.domain.account.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SecurityUser extends User {
    private final Long id;
    private final String username;
    private final String nickname;
    private final String email;
    private final LocalDateTime joinedAt;

    public SecurityUser(Account account, List<GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.id = account.getId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
        this.joinedAt = account.getJoinedAt();
    }

    public Account getAccount() {
        return Account.builder()
                .id(id)
                .joinedAt(joinedAt)
                .username(username)
                .nickname(nickname)
                .email(email)
                .build();
    }
}
