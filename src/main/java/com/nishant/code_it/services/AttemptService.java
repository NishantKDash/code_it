package com.nishant.code_it.services;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.AttemptStatus;
import com.nishant.code_it.enums.Language;
import com.nishant.code_it.exceptions.UserDoesNotExist;
import com.nishant.code_it.models.Attempt;
import com.nishant.code_it.models.Question;
import com.nishant.code_it.models.UserEntity;
import com.nishant.code_it.rabbitmq.AttemptQueueData;
import com.nishant.code_it.repositories.AttemptRepository;
import com.nishant.code_it.repositories.QuestionRepository;
import com.nishant.code_it.repositories.UserRepository;

@Service
public class AttemptService {

	@Autowired
	private AttemptRepository attemptRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired 
	private RabbitTemplate template;
	
	@Autowired
	private LanguageService languageService;
	
	
	@Value("${attemptqueue.key}")
	private String attemptQueueKey;
	@Value("${notificationqueue.key}")
	private String notificationQueueKey;
	@Value("${exchange.name}")
	private String exchangeName;
	
	
	public Attempt submit(Long qid , String username , String code , String language) throws UserDoesNotExist
	{
		UserEntity user = userRepository.findByusername(username);
		if(user == null)
			throw new UserDoesNotExist("The specified user does not exist");
		
		Question question = questionRepository.findById(qid).get();
		
		Attempt attempt = new Attempt();
		attempt.setUser(user);
		attempt.setCode(code);
		attempt.setQid(qid);
		attempt.setAttemptStatus(AttemptStatus.CHECKING);
		attempt.setTimeStamp(LocalDateTime.now());
		Language usedLang = languageService.getLanguage(language);
		attempt.setLanguage(usedLang);
		
		
		AttemptQueueData queueData = new AttemptQueueData();
		queueData.setAttemptId(attempt.getId());
		queueData.setCode(attempt.getCode());
		queueData.setLanguage(usedLang);
		queueData.setQid(qid);
		queueData.setUsername(username);
		template.convertAndSend(exchangeName , attemptQueueKey , queueData);
		
		return attemptRepository.save(attempt);
	}
}
