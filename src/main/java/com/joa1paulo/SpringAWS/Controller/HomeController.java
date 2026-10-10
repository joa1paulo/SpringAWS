package com.joa1paulo.SpringAWS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// @Controller (e não @RestController): o retorno "index" é o nome do
// template em src/main/resources/templates/index.html
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}