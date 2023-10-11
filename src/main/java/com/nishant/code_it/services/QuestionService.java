package com.nishant.code_it.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nishant.code_it.dtos.QuestionAdditionDto;
import com.nishant.code_it.models.Example;
import com.nishant.code_it.models.Question;
import com.nishant.code_it.models.TestCase;
import com.nishant.code_it.repositories.QuestionRepository;

@Service
public class QuestionService {
  
	
	@Autowired
	private QuestionRepository questionRepository;
	
	public Question create(QuestionAdditionDto questionDto)
	{ 
		
		Question question  = new Question();
		question.setTitle(questionDto.getTitle());
		question.setDescription(questionDto.getDescription());
		List<Example> examples = questionDto.getExamples().stream().map(exampledto->{Example example = new Example();
		example.setExplanation(exampledto.getExplanation());
		example.setInput(exampledto.getInput());
		example.setOutput(exampledto.getOutput());
		example.setQuestion(question);
		return example;}).collect(Collectors.toList());
		
		
		List<TestCase> testCases = questionDto.getTestCases().stream().map(testcasedto->{TestCase testCase = new TestCase();
		testCase.setOutput(testcasedto.getOutput());
		testCase.setQuestion(question);
		testCase.setQuestionTemplate(testcasedto.getQuestionTemplate());
		return testCase;}).collect(Collectors.toList());
		
		question.setTestCases(testCases);
		question.setExamples(examples);
		return questionRepository.save(question);
		
	}
}
