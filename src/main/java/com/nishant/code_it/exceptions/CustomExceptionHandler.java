package com.nishant.code_it.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
 
	@ExceptionHandler(BadCredentialsException.class)	
	public final ResponseEntity<Object> handleBadCredentialException(Exception ex , WebRequest request)
	{
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage() , request.getDescription(false));
		return new ResponseEntity(error , HttpStatus.UNAUTHORIZED);
		
	}
	
	
	@ExceptionHandler(UserAlreadyExists.class)	
	public final ResponseEntity<Object> handleUserAlreadyExistsException(Exception ex , WebRequest request)
	{
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage() , request.getDescription(false));
		return new ResponseEntity(error , HttpStatus.FORBIDDEN);
		
	}
	
	@ExceptionHandler(UserDoesNotExist.class)	
	public final ResponseEntity<Object> handleUserDoesNotExistsException(Exception ex , WebRequest request)
	{
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage() , request.getDescription(false));
		return new ResponseEntity(error , HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(WrongPassword.class)	
	public final ResponseEntity<Object> handlewrongpassword(Exception ex , WebRequest request)
	{
		ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage() , request.getDescription(false));
		return new ResponseEntity(error , HttpStatus.FORBIDDEN);
		
	}

}
