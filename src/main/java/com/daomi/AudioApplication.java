package com.daomi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AudioApplication {
    public static void main(String[] args) {
        SpringApplication.run(AudioApplication.class, args);
        log.info("服务已启动");
    }
}
