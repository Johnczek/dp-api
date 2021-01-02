package cz.johnczek.dpapi.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello";
    }
}
