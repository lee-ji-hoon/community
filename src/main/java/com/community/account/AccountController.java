package com.community.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    @GetMapping("/")
    public String main() {
        return "index";
    }
}
