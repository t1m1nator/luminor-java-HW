package com.example.luminorjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LuminorJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuminorJavaApplication.class, args);
    }

}
