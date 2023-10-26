package com.nishant.code_it.code_exec_engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.AttemptStatus;
import com.nishant.code_it.models.Attempt;
import com.nishant.code_it.models.Question;
import com.nishant.code_it.models.TestCase;
import com.nishant.code_it.rabbitmq.AttemptQueueData;
import com.nishant.code_it.rabbitmq.NotificationQueueData;
import com.nishant.code_it.repositories.AttemptRepository;
import com.nishant.code_it.repositories.QuestionRepository;

@Service
public class CodeExecutorService {
	
	
	@Autowired
	private CodeGeneratorFactory codeGeneratorFactory;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${attemptqueue.key}")
	private String attemptQueueKey;
	@Value("${notificationqueue.key}")
	private String notificationQueueKey;
	@Value("${exchange.name}")
	private String exchangeName;
	
	@Autowired
	private AttemptRepository attemptRepository;
	

	
	@RabbitListener(queues = "${attemptqueue.name}")
	public void executeCode(AttemptQueueData queueData)
	{
		CodeGenerator codeGenerator = codeGeneratorFactory.getCodeGenerator(queueData.getLanguage());
		
		Question question = questionRepository.findById(queueData.getQid()).get();
		
		List<TestCase> testCases = question.getTestCases();
		List<String> codeFiles = new ArrayList<>();
		List<String> dockerFiles = new ArrayList<>();
		List<String> directories = new ArrayList<>();
		List<String> images = new ArrayList<>();
		List<String> containerIds = new ArrayList<>();
		List<String> expectedOutput = new ArrayList<>();
		
		for(TestCase testCase : testCases)
		{
		   String questionTemplate = testCase.getQuestionTemplate();
		   String finalCode = codeGenerator.prepareCode(questionTemplate, queueData.getCode());
		   String folder = codeGenerator.prepareDirectory(queueData.getAttemptId(), testCase.getId());
		   String codeFilePath = codeGenerator.createCodeFile(folder , finalCode);
		   String dockerFilePath = codeGenerator.createDockerfile(folder ,queueData.getAttemptId(), testCase.getId());
		   String imageId = "";
		   String containerId = "";
		try {
			imageId = codeGenerator.buildImage(dockerFilePath , folder);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Attempt attempt = attemptRepository.findById(queueData.getAttemptId()).get();
			attempt.setAttemptStatus(AttemptStatus.FAILURE);
			attemptRepository.save(attempt);
			cleanup(codeGenerator,codeFiles ,dockerFiles ,directories ,images);
			try {
				codeGenerator.deleteFile(dockerFilePath);
				codeGenerator.deleteFile(codeFilePath);
				codeGenerator.deleteDirectory(folder);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			sendNotification(queueData.getAttemptId(), AttemptStatus.FAILURE, "Error exists in code", queueData.getUsername() , queueData.getQid());
			return;
		}
		
		try {
			System.out.println("WrongImage" +  imageId);
			containerId = codeGenerator.createContainer(imageId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		    
		   
		   codeFiles.add(codeFilePath);
		   dockerFiles.add(dockerFilePath);
		   directories.add(folder);
		   images.add(imageId);
		   containerIds.add(containerId);
		   expectedOutput.add(testCase.getOutput());
		   
		   codeGenerator.startContainer(containerId);
		   
		   
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<String> result = containerIds.stream().map(containerId ->{codeGenerator.stopContainer(containerId);
		                                         String output = "";
												try {
													output = codeGenerator.getOutput(containerId);
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											
											
		                                         codeGenerator.removeContainer(containerId);
		                                         return output;}).collect(Collectors.toList());
		
		
		cleanup(codeGenerator,codeFiles ,dockerFiles ,directories ,images);
		
		
		if(examineOutput(expectedOutput , result))
		{
		Attempt attempt = attemptRepository.findById(queueData.getAttemptId()).get();
		attempt.setAttemptStatus(AttemptStatus.SUCCESS);
		attemptRepository.save(attempt);
		sendNotification(queueData.getAttemptId(), AttemptStatus.SUCCESS, "Answer is correct", queueData.getUsername() , queueData.getQid());
		}
		else
		{
			Attempt attempt = attemptRepository.findById(queueData.getAttemptId()).get();
			attempt.setAttemptStatus(AttemptStatus.FAILURE);
			attemptRepository.save(attempt);
		sendNotification(queueData.getAttemptId(), AttemptStatus.FAILURE, "Answer is wrong", queueData.getUsername() , queueData.getQid());
		}
		
		}
	
	
	public void cleanup(CodeGenerator codeGenerator , List<String> codeFiles,List<String> dockerFiles,List<String> directories,List<String> images)
	{
		for(int i = 0;i<codeFiles.size();i++)
		{
			try {
				codeGenerator.deleteFile(codeFiles.get(i));
				codeGenerator.deleteFile(dockerFiles.get(i));
				codeGenerator.removeImage(images.get(i));
				codeGenerator.deleteDirectory(directories.get(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean examineOutput(List<String> expectedOutput , List<String> output)
	{
		return true;
	}
	
	public void sendNotification(Long attemptId , AttemptStatus attemptStatus, String message , String username, Long qid)
	{
		NotificationQueueData notificationData = new NotificationQueueData();
		notificationData.setAttemptId(attemptId);
		notificationData.setAttemptStatus(attemptStatus);
		notificationData.setMessage(message);
		notificationData.setUsername(username);
		notificationData.setQid(qid);
	
		
		rabbitTemplate.convertAndSend(exchangeName , notificationQueueKey , notificationData);
	}

}
