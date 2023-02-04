package com.workmotion.task.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(FunctionalException.class)
	public ResponseEntity<ErrorResponse> functionalExceptionHandler(FunctionalException ex) {
		final ErrorResponse error = new ErrorResponse(new Date(), ex.getHttpStatus(), ex.getMsg());
		return new ResponseEntity<>(error, ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> generalExceptionHandler(Exception ex) {
		final ErrorResponse error = new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR,
				"INTERNAL_SERVER_ERROR");
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		   List<String> errors = ex.getBindingResult().getFieldErrors()
	                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
		final ErrorResponse error = new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST, String.join( "," , errors));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
