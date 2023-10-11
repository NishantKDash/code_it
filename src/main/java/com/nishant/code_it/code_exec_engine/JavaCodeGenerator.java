package com.nishant.code_it.code_exec_engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.lang.model.element.Modifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@Service
public class JavaCodeGenerator implements CodeGenerator{


	@Value("${filesystem.code.path}")
	private String baseFilePath;
	
	@Value("${filesystem.image.path}")
	private String baseImagePath;
	
	@Autowired
	private DockerClient dockerClient;

	@Override
	public String prepareCode(String questionTemplate, String code) {
		StringBuilder finalCode = new StringBuilder();
		finalCode.append(questionTemplate)
		         .append("\n")
		         .append(code);
        return finalCode.toString();
	}
	
	@Override
	public String createCodeFile(Long attemptId,Long testCaseId, String code) {
		String filepath = baseFilePath + attemptId + "_" + testCaseId + ".java";
		Path path = Path.of(filepath);
		try {
			Files.write(path, code.getBytes(), StandardOpenOption.CREATE);
			
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
		
		return filepath;
	}

	@Override
	public void deleteFile(String path) throws IOException {
	    Files.delete(Path.of(path));
	}

	@Override
	public String createDockerfile(Long attemptId, Long testCaseId) {
	    String filepath = baseImagePath + attemptId + "_" + testCaseId;
	    Path path = Path.of(filepath);
	    
	    String fileName = attemptId + "_" + testCaseId + ".java";
	    
	    StringBuilder content = new StringBuilder();
	      content.append("FROM openjdk:11\n")
	             .append("WORKDIR /app\n")
	             .append("COPY ")
	             .append(fileName)
	             .append(" /app\n")
	             .append("RUN javac "+fileName+"\n");
	            
		try {
			Files.write(path, content.toString().getBytes(), StandardOpenOption.CREATE);
			
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
		return filepath;
	}
	
	
	@Override
	public String buildImage(String imagePath)
	{
	String imageid = dockerClient.buildImageCmd()
		            .withDockerfile(new File(imagePath))
		            .withBaseDirectory(new File(baseFilePath))  // this is for the context- if dockerfile references files in other directories like codefiles etc.
		            .exec(new BuildImageResultCallback())
		            .awaitImageId();
     return imageid;
		
	}

	@Override
	public void removeImage(String imageId) {
		dockerClient.removeImageCmd(imageId).exec();
		
	}

	@Override
	public String createContainer(String imageId) {
         
		String args [] = new String[2];
		args[0] = "java";
		args[1] = "Main";
		CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
				                                        .withCmd(args)
				                                        .withHostConfig(HostConfig.newHostConfig()
				                                        		                  .withMemory(256L*1024L*1024L))
				                                        .exec();
		
		return container.getId();
		
	}
	
	@Override
	public void startContainer(String containerId) {
		dockerClient.startContainerCmd(containerId).exec();
		
	}
	
	
	@Override
	public void stopContainer(String containerId)
	{
		InspectContainerResponse containerInspect = dockerClient.inspectContainerCmd(containerId).exec();
		if(containerInspect.getState().getRunning())
		dockerClient.stopContainerCmd(containerId).exec();
	}

	@Override
	public String getOutput(String containerId) {
		
		StringBuilder output = new StringBuilder();
	     dockerClient.logContainerCmd(containerId).withStdOut(true)
	                                              .withStdErr(true)
	                 .exec(new ResultCallback.Adapter<>() {
	                	 @Override
	                	 public void onNext(Frame frame)
	                	 {
	                		 output.append(frame);
	                	 }
	                 });
	     
	     return output.toString();
	}

	@Override
	public void removeContainer(String containerId) {
		dockerClient.removeContainerCmd(containerId).exec();
		
	}





}
