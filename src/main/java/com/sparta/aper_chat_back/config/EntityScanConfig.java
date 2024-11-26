package com.sparta.aper_chat_back.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.aperlibrary")
public class EntityScanConfig {
}
