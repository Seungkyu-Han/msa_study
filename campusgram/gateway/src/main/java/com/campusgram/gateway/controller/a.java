package com.campusgram.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class a{
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}