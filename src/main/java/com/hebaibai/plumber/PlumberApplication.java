package com.hebaibai.plumber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class PlumberApplication {

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(16);
    }

    public static void main(String[] args) {
        SpringApplication.run(PlumberApplication.class, args);
    }

}
