package ru.nsu.ccfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CHWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CHWorkerApplication.class, args);
    }

}
