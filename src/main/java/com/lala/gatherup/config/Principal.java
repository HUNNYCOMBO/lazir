package com.lala.gatherup.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

// principal 애노테이션 정의
//anonymous 유저면 null을 리턴, 리턴하는 객체는 userPrincipal객체다. annoymous가 아니면 property account를 꺼낸다.
@Retention(RetentionPolicy.RUNTIME) // 런타임시 까지 유지합니다.
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface Principal {
}
