package com.nishant.code_it.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.code_it.dtos.ExampleDto;
import com.nishant.code_it.dtos.GetAllQuestionsResponseDto;
import com.nishant.code_it.dtos.QuestionAdditionDto;
import com.nishant.code_it.dtos.QuestionResponseDto;
import com.nishant.code_it.models.Question;
import com.nishant.code_it.services.QuestionService;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
	
	@Autowired
    private	QuestionService questionService;
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> createQuestion(@RequestBody QuestionAdditionDto questionDto)
	{
		Question question = questionService.create(questionDto);
		return ResponseEntity.ok("Question has been created");
	}
	
	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<GetAllQuestionsResponseDto>> getAllQuestions()
	{
		List<Question> questions= questionService.getAllQuestions();
		List<GetAllQuestionsResponseDto> dtoList =  questions.stream().map(question->{GetAllQuestionsResponseDto dto = new GetAllQuestionsResponseDto();
		                                  dto.setQid(question.getId());
		                                  dto.setTitle(question.getTitle());
		                                  return dto;}).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);
	}
	
	@GetMapping("/student")
	@PreAuthorize("hasAuthority('STUDENT')")
	public ResponseEntity<List<GetAllQuestionsResponseDto>> getAllQuestionForStudent()
	{
		List<Question> questions= questionService.getAllQuestions();
		List<GetAllQuestionsResponseDto> dtoList =  questions.stream().map(question->{GetAllQuestionsResponseDto dto = new GetAllQuestionsResponseDto();
		                                  dto.setQid(question.getId());
		                                  dto.setTitle(question.getTitle());
		                                  return dto;}).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteQuestion(@PathVariable Long id)
	{
		questionService.delete(id);
		return ResponseEntity.ok("Question with ID-> " + id + " has been deleted");
	}
	
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('STUDENT')")
	public ResponseEntity<QuestionResponseDto> getSingleQuestion(@PathVariable Long id)
	{
		Question question  = questionService.getQuestion(id);
		QuestionResponseDto questionResponse = new QuestionResponseDto();
		 questionResponse.setQid(id);
		 questionResponse.setTitle(question.getTitle());
		 questionResponse.setDescription(question.getDescription());
		 
		List<ExampleDto> examples = question.getExamples().stream().map(example->{
			  ExampleDto exampleDto = new ExampleDto();
			  exampleDto.setExplanation(example.getExplanation());
			  exampleDto.setInput(example.getInput());
			  exampleDto.setOutput(example.getOutput());
			  return exampleDto;
		 }).collect(Collectors.toList());
		questionResponse.setExamples(examples);
		questionResponse.setSolutionTemplate(question.getSolutionTemplate());
		 
		return ResponseEntity.ok(questionResponse);
	}
	
	
 
	
}
