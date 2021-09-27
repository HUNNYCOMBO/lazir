package com.lazir.lazir.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

//anonymous 유저면 null을 리턴, 리턴하는 객체는 userPrincipal객체다. annoymous가 아니면 property account를 꺼낸다.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface Principal {
}
