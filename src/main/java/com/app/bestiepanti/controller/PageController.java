package com.app.bestiepanti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String welcome() {
        return "beranda";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/panti")
    public String panti() {
        return "panti";
    }

    @GetMapping("/panti-detail")
    public String pantiDetail() {
        return "panti-detail";
    }

    @GetMapping("/cara-donasi")
    public String caraDonasi() {
        return "cara-donasi";
    }

    @GetMapping("/tentang-kami")
    public String tentangKami() {
        return "tentang-kami";
    }

    @GetMapping("/donatur/test")
    public String getMethodName() {
        return "user-list";
    }

}
