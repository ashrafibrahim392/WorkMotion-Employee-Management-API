package com.workmotion.task.exception;



import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FunctionalException extends Exception {

	private HttpStatus httpStatus ; 
	
	private String msg;
	
	
}
