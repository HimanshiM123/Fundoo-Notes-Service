package com.bridgelabz.fundoonotesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FundooNotesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FundooNotesServiceApplication.class, args);
    }

}
