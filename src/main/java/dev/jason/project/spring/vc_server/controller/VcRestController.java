package dev.jason.project.spring.vc_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VcRestController {

    @GetMapping
    public String home() {
        return "Hello, World!";
    }
}
