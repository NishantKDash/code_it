package com.nishant.code_it.code_exec_engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
import com.github.dockerjava.core.command.LogContainerResultCallback;

@Service
public class JavaCodeGenerator implements CodeGenerator{


	@Value("${filesystem.code.path}")
	private String baseFilePath;
	
//	@Value("${filesystem.image.path}")
//	private String baseImagePath;
	
	@Autowired
	private DockerClient dockerClient;
	
	
	@Override
	public String prepareDirectory(Long attemptId, Long testCaseId) {
		String folderPath = baseFilePath + attemptId + "_" + testCaseId;
		Path path = Path.of(folderPath);
		 if (!Files.exists(path)) {
	            // If it doesn't exist, create the directory
	            try {
	                Files.createDirectories(path);
	            } catch (IOException e) {
	                System.err.println("Failed to create directory: " + e.getMessage());
	            }
	        }
		return folderPath;
	}
	
	@Override
	public void deleteDirectory(String folderPath) throws IOException {
		Files.delete(Path.of(folderPath));
		
	}

	@Override
	public String prepareCode(String questionTemplate, String code) {
		StringBuilder finalCode = new StringBuilder();
		finalCode.append(questionTemplate)
		         .append("\n")
		         .append(code);
        return finalCode.toString();
	}
	
	@Override
	public String createCodeFile(String folderPath , String code) {
		String filepath = folderPath + "\\Main.java";
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
	public String createDockerfile(String folderPath , Long attemptId, Long testCaseId) {
	    String filepath = folderPath + "\\" + attemptId + "_" + testCaseId;
	    Path path = Path.of(filepath);
	    
	    
	    StringBuilder content = new StringBuilder();
	      content.append("FROM openjdk:11\n")
	             .append("WORKDIR /app\n")
	             .append("COPY ")
	             .append("Main.java")
	             .append(" /app\n")
	             .append("RUN javac Main.java" + "\n");
	            
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
	public String buildImage(String dockerfilePath , String contextPath)
	{
		
	String imageid = dockerClient.buildImageCmd()
		            .withDockerfile(new File(dockerfilePath))
		            .withBaseDirectory(new File(contextPath))  // this is for the context- if dockerfile references files in other directories like codefiles etc.
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
		final List<Frame> logs = new ArrayList<>();
	     dockerClient.logContainerCmd(containerId).withStdOut(true)
	                                              .withStdErr(true)
	                                              .withFollowStream(true)
	                 .exec(new ResultCallback.Adapter<>() {
	                	 @Override
	                	 public void onNext(Frame frame)
	                	 {
	                         logs.add(frame);
	                	 }
	                 });
	     
	     System.out.println("Output" + logs.size());
	     
	     return "done";
	}

	@Override
	public void removeContainer(String containerId) {
		dockerClient.removeContainerCmd(containerId).exec();
		
	}



	





}
