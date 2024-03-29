package ru.nsu.ccfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CHManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CHManagerApplication.class, args);
    }

}
