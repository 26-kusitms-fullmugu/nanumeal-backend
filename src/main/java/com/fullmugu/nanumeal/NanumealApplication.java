package com.fullmugu.nanumeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.fullmugu.nanumeal.api.entity.user"})
public class NanumealApplication {


    public static void main(String[] args) {
        SpringApplication.run(NanumealApplication.class, args);
    }

}
