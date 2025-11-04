package com.deliveryoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
//@ImportResource("classpath:applicationContext.xml")
public class DeliveryOptimizerApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(DeliveryOptimizerApplication.class, args);
    }
}

