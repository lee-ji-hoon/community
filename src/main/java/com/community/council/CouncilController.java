package com.community.council;

import com.community.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CouncilController {

    @GetMapping("council/student-council")
    public String council(Account account, Model model) {
        model.addAttribute(account);

        return "council/student-council";
    }
}
