package com.nishant.code_it.exceptions;

public class UserDoesNotExist extends Exception{


	private static final long serialVersionUID = 1L;
	public UserDoesNotExist(String message)
	{
		super(message);
	}

}
