package com.sdc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class greeterController {
    @GetMapping("/")
    public String greet(){
        return "your backend server is up and running !";
    }
}
