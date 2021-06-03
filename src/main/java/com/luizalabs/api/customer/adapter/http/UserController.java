package com.luizalabs.api.customer.adapter.http;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/principal")
public class UserController {

    @GetMapping
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
}
