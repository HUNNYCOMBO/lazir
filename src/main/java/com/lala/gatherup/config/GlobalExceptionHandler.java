package com.lala.gatherup.config;

import com.lala.gatherup.domain.ResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice  //어디라도 Exception 일어나면 이쪽으로 올수있게
@RestController
public class GlobalExceptionHandler {

	@ExceptionHandler(value=Exception.class)  // Exception 의 종류 설정가능
	public ResponseDto<String> handleArgumentException(Exception e) {
		
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
		
	}
}
