package com.app.bestiepanti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<html><body><h1>Server is running!</h1></body></html>";
    }
}
