package com.dsas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.method.HandlerMethod;

import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = "com.dsas")
public class DsasApplication {

    private static final Logger logger = LoggerFactory.getLogger(DsasApplication.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(DsasApplication.class, args);

        // 诊断：检查注册的控制器
        String[] beanNames = context.getBeanNamesForType(org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class);
        logger.info("Found RequestMappingHandlerMapping beans: {}", (Object)beanNames);

        // 检查所有控制器Bean
        String[] controllerBeans = context.getBeanNamesForAnnotation(org.springframework.web.bind.annotation.RestController.class);
        logger.info("Found RestController beans: {}", (Object)controllerBeans);

        // 详细检查所有注册的映射
        RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<org.springframework.web.servlet.mvc.method.RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        logger.info("=== Registered Mappings ===");
        for (Map.Entry<org.springframework.web.servlet.mvc.method.RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            logger.info("Mapping: {} -> {}", entry.getKey(), entry.getValue().getMethod());
        }
        logger.info("=== End of Registered Mappings ===");
    }
}