package com.community.infra.config;

import com.community.domain.account.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Slf4j
@Getter @Setter
public class SecurityUser extends User {
    private Account account;

    public SecurityUser(Account account) {
        super(account.getEmail(), account.getPassword(), AuthorityUtils.createAuthorityList(account.getAccountType().toString()));

        log.info("SecurityUser member.username = {}", account.getUsername());
        log.info("SecurityUser member.password = {}", account.getPassword());
        log.info("SecurityUser member.accountType = {}", account.getAccountType().toString());

        this.account = account;
    }
}
