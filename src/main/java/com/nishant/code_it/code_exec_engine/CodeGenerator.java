package com.nishant.code_it.code_exec_engine;

public interface CodeGenerator {
  
	public String prepareCode(String questionTemplate, String code);
	public String createCodeFile(Long attemptId ,Long testCaseId, String code);
	public void deleteFile(String path) throws Exception;
	public String createDockerfile(Long attemptId, Long testCaseId);
	public String buildImage(String imagePath);
	public void removeImage(String imageId);
	public String createContainer(String imageName);
	public void startContainer(String containerId);
	public void stopContainer(String containerId);
	public void removeContainer(String containerId);
	public String getOutput(String containerId);
}
