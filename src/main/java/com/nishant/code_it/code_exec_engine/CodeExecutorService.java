package com.nishant.code_it.code_exec_engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.AttemptStatus;
import com.nishant.code_it.models.Question;
import com.nishant.code_it.models.TestCase;
import com.nishant.code_it.rabbitmq.AttemptQueueData;
import com.nishant.code_it.rabbitmq.NotificationQueueData;
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
	

	
	@RabbitListener(queues = "${attemptqueue.name}")
	public void executeCode(AttemptQueueData queueData) throws InterruptedException
	{
		CodeGenerator codeGenerator = codeGeneratorFactory.getCodeGenerator(queueData.getLanguage());
		
		Question question = questionRepository.findById(queueData.getQid()).get();
		
		List<TestCase> testCases = question.getTestCases();
		List<String> codeFiles = new ArrayList<>();
		List<String> dockerFiles = new ArrayList<>();
		List<String> directories = new ArrayList<>();
		List<String> images = new ArrayList<>();
		List<String> containerIds = new ArrayList<>();
		
		for(TestCase testCase : testCases)
		{
		   String questionTemplate = testCase.getQuestionTemplate();
		   String finalCode = codeGenerator.prepareCode(questionTemplate, queueData.getCode());
		   String folder = codeGenerator.prepareDirectory(queueData.getAttemptId(), testCase.getId());
		   String codeFilePath = codeGenerator.createCodeFile(folder , finalCode);
		   String dockerFilePath = codeGenerator.createDockerfile(folder ,queueData.getAttemptId(), testCase.getId());
		   String imageId = codeGenerator.buildImage(dockerFilePath , folder);
		   String containerId = codeGenerator.createContainer(imageId);
		   
		   codeFiles.add(codeFilePath);
		   dockerFiles.add(dockerFilePath);
		   directories.add(folder);
		   images.add(imageId);
		   containerIds.add(containerId);
		   
		   codeGenerator.startContainer(containerId);
		   
		   
		}
		
		
		Thread.sleep(1000);
		
		List<String> result = containerIds.stream().map(containerId ->{codeGenerator.stopContainer(containerId);
		                                         String output = codeGenerator.getOutput(containerId);
		                                         System.out.println(output + " " + "here I am here I am");
		                                         codeGenerator.removeContainer(containerId);
		                                         return output;}).collect(Collectors.toList());
		
		
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
		
		
		
		NotificationQueueData notificationData = new NotificationQueueData();
		notificationData.setAttemptId(queueData.getAttemptId());
		notificationData.setAttemptStatus(AttemptStatus.SUCCESS);
		notificationData.setResults(result);
		notificationData.setUsername(queueData.getUsername());
		notificationData.setQid(queueData.getQid());
	
		
		rabbitTemplate.convertAndSend(exchangeName , notificationQueueKey , notificationData);
		
		
		
		
	}

}
