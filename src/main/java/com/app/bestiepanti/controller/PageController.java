package com.app.bestiepanti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PageController {
    
    @GetMapping("/")
    public String welcome(){
        return "welcome";
    }
}
