package com.lazir.lazir.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// 설정을 정의하는 설정정보 클래스
@Configuration
@SpringBootApplication  // 내장 was를 사용합니다.
public class AppConfig {
    
    //securityconfig에 설정했더니 순환참조가 일어남.
    //springbootapplication에서 설정하니 일어나지 않음. 여기부터 설정을 읽어나가기 떄문에.
    // 비밀번호 인코딩
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
