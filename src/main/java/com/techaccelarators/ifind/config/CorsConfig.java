//package com.techaccelarators.ifind.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public WebMvcConfiguration getCorsConfiguration(){
//        return new WebMvcConfiguration(){
//            @Override
//            public void addCorsMappings(CorsRegistry registry){
//                registry.addMapping("/**")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//                        .allowedOrigins("*")
//                        .allowedHeaders("*");
//            }
//        };
//    }
//}
