package com.nishant.code_it.code_exec_engine;

import java.io.IOException;

public interface CodeGenerator {
  
	public String prepareDirectory(Long attemptId , Long testCaseId);
	public void deleteDirectory(String folderPath) throws IOException;
	public String prepareCode(String questionTemplate, String code);
	public String createCodeFile(String folderPath , String code);
	public void deleteFile(String path) throws Exception;
	public String createDockerfile(String folderPath , Long attemptId , Long testCaseId);
	public String buildImage(String dockerfilePath , String contextPath);
	public void removeImage(String imageId);
	public String createContainer(String imageName);
	public void startContainer(String containerId);
	public void stopContainer(String containerId);
	public void removeContainer(String containerId);
	public String getOutput(String containerId);
}
