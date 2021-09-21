package com.lazir.lazir.config;

import com.lazir.lazir.service.PrincipalDetailService;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration  //xml설정 bean등록
@EnableWebSecurity  //시큐리티 필터설정을 직접 함.
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 주소러 접근하면 권한 및 인증을 미리 함.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    public PasswordEncoder passwordeEncoder;
    private final PrincipalDetailService detailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailService).passwordEncoder(passwordeEncoder);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .mvcMatchers("/","/login","/sign-up","/check-email","/check-email",
                        "/email-login","/check-email-login","/login-link","*")
            .permitAll()  //인증없이 허용
            .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll() //get요청일때만 허용
            .anyRequest()
            .authenticated()      //나머지는 인증 필요
            .and()  //나머지 인증은 로그인으로 넘어가게함.
            .formLogin()
            //.loginPage("/login")    //로그인 페이지 설정
            .loginProcessingUrl("/")
            .defaultSuccessUrl("/");
        
    }

   
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .mvcMatchers("/node_modules/**")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    
}
