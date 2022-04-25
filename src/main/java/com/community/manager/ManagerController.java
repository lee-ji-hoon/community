package com.community.manager;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/manager/main")
    public String managerMain(@CurrentUser Account account, Model model) {
        model.addAttribute(managerService);
        model.addAttribute(account);
        return "manager/m_main";
    }

    @GetMapping("/manager/board")
    public String managerBoard(@CurrentUser Account account, Model model) {

        model.addAttribute(managerService);
        model.addAttribute(account);
        return "manager/m_board";
    }

    @GetMapping("/manager/setting")
    public String managerSetting(@CurrentUser Account account, Model model) {
        model.addAttribute(managerService);
        model.addAttribute(account);
        return "manager/tables";
    }

    @GetMapping("/manager/account")
    public String managerTables(@CurrentUser Account account, Model model) {
        //TODO byAccount 기능 만들기
        model.addAttribute(managerService);
        model.addAttribute(account);
        return "manager/m_account";
    }

    @GetMapping("/manager/maps")
    public String managerMaps(@CurrentUser Account account, Model model) {

        model.addAttribute(managerService);
        model.addAttribute(account);
        return "manager/maps";
    }
}
