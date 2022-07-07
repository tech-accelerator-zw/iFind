package com.techaccelarators.ifind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = {"com.techaccelarators.ifind"})
public class IFindApplication {

    public static void main(String[] args) {
        SpringApplication.run(IFindApplication.class, args);
    }

}
