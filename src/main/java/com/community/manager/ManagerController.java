package com.community.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManagerController {

    @GetMapping("/manager/main")
    public String managerMain() {
        return "manager/m_main";
    }

    @GetMapping("/manager/setting")
    public String managerSetting() {
        return "manager/settings";
    }

    @GetMapping("/manager/account")
    public String managerTables() {
        return "manager/account";
    }

    @GetMapping("/manager/maps")
    public String managerMaps() {
        return "manager/maps";
    }
}
