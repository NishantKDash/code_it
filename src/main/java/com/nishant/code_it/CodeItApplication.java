package com.nishant.code_it;

import java.io.File;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.nishant.code_it.controllers.UserController;
import com.nishant.code_it.dtos.RegisterRequestDto;

@SpringBootApplication
public class CodeItApplication {
	
	


	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(CodeItApplication.class, args);
		UserController userController = context.getBean(UserController.class);
//		DockerClient client = context.getBean(DockerClient.class);
//		String imageid = client.buildImageCmd()
//	            .withDockerfile(new File("C:\\Users\\nisha\\Desktop\\Eclipse_Projects\\code_it\\imageFiles\\102_102"))
//	            .withBaseDirectory(new File("C:/Users/nisha/Desktop/Eclipse_Projects/code_it/bufferFiles"))  // this is for the context- if dockerfile references files in other directories like codefiles etc.
//	            .exec(new BuildImageResultCallback())
//	            .awaitImageId();
//		
//	      System.out.println(imageid);
//	      client.removeImageCmd(imageid).exec();
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
