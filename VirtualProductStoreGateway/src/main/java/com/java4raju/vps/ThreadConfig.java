package com.java4raju.vps;

import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * This config is required for the Spring-boot version running below 3.2
 * commented as this running on 3.2+
 * 
 * Place the property below at config(yaml/prop) file to enable/disable to use virtual/platform thread
 * spring.thread-executor.virtual=true 
 */

/*
@EnableAsync
@Configuration
@ConditionalOnProperty(
  value = "spring.thread-executor.virtual",
  havingValue = "true"
)
public class ThreadConfig {

    @Bean
    TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}
*/