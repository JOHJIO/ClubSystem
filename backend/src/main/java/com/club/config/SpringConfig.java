package com.club.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.club.service", "com.club.exception"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class SpringConfig {
}