package com.nishant.code_it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 
	
}
