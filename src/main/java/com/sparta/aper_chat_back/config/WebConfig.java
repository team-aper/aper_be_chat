//package com.sparta.aper_chat_back.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Bean(name = "mvcTaskExecutor")
//    public AsyncTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);  // 기본적으로 유지되는 스레드의 수
//        executor.setMaxPoolSize(50);   // 최대 생성될 수 있는 스레드의 수
//        executor.setQueueCapacity(100); // 스레드 풀에서 처리할 수 있는 대기 요청 수
//        executor.setThreadNamePrefix("MvcAsync-"); // 생성되는 스레드의 이름 접두사
//        executor.initialize();
//        return executor;
//    }
//
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        configurer.setTaskExecutor(taskExecutor());
//        configurer.setDefaultTimeout(30000); // 비동기 요청의 기본 타임아웃 (30초)
//    }
//}