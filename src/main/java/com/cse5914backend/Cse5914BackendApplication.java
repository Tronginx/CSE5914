package com.cse5914backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Cse5914BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Cse5914BackendApplication.class, args);
    }

}
