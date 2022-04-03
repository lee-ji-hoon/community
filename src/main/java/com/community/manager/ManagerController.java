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

    @GetMapping("/manager/main")
    public String managerMain(Model model) {
        return "manager/m_main";
    }

    @GetMapping("/manager/board")
    public String managerBoard(Model model) {
        return "manager/m_board";
    }

    @GetMapping("/manager/setting")
    public String managerSetting(Model model) {
        return "manager/settings";
    }

    @GetMapping("/manager/account")
    public String managerTables(Model model) {
        return "manager/tables";
    }

    @GetMapping("/manager/maps")
    public String managerMaps(Model model) {
        return "manager/maps";
    }
}
