package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Applicaion에 적용 시 테스트 코드에도 적용
@EnableJpaAuditing
@Configuration
public class JPAAuditingConfig {

}
