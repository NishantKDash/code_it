package com.nishant.code_it;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.nishant.code_it.controllers.UserController;
import com.nishant.code_it.dtos.RegisterRequestDto;

@SpringBootApplication
public class CodeItApplication {
	
	


	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(CodeItApplication.class, args);
		UserController userController = context.getBean(UserController.class);
		try {
	    userController.registerAdmin(new RegisterRequestDto("nishant" , "nishant" , "1234"));}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	
	}
	
	
	@Bean
	public ModelMapper mapper()
	{
		return new ModelMapper();
	}


}
