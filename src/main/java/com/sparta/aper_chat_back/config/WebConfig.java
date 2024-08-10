//package com.sparta.aper_chat_back.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("http://localhost:8081")  // Specify the allowed origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Specify allowed HTTP methods
//                .allowedHeaders("*")  // Allow all headers
//                .allowCredentials(true);  // Allow credentials
//    }
//}