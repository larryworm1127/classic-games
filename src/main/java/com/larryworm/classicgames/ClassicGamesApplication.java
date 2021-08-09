package com.larryworm.classicgames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication
@Controller
public class ClassicGamesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassicGamesApplication.class, args);
    }

    @GetMapping(value = "/{path:[^.]*}")
    public String redirect(@PathVariable String path) {
        return "forward:/";
    }
}
