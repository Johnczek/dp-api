package cz.johnczek.dpapi.hello.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "/hello/unauthorized")
    public String helloUnauthorized() {
        return "Hello unauthorized";
    }

    @Operation(summary = "Hello endpoint requiring authorization", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/hello/authorized")
    public String helloAuthorized() {
        return "Hello authorized";
    }
}
