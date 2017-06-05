package com.pard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by wawe on 17/6/3.
 */
@ServletComponentScan
@SpringBootApplication
@EnableCaching
public class PardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PardApplication.class, args);
    }

}
