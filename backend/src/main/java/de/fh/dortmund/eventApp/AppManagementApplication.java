package de.fh.dortmund.eventApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppManagementApplication.class, args);
    }

}
