package com.nishant.code_it.exceptions;

public class UserAlreadyExists extends Exception{

	private static final long serialVersionUID = 1L;
   public UserAlreadyExists(String message)
   {
	   super(message);
   }
}
