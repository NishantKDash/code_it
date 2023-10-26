package com.nishant.code_it.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.code_it.dtos.AttemptDto;
import com.nishant.code_it.dtos.AttemptRequestDto;
import com.nishant.code_it.models.Attempt;
import com.nishant.code_it.services.AttemptService;

@RestController
@RequestMapping("/api/attempt")
public class AttemptController {
	
	@Autowired
	private AttemptService attemptService;
	
	
   @PostMapping("/submit")
   @PreAuthorize("hasAuthority('STUDENT')")
   public ResponseEntity<String> submit(@RequestBody AttemptDto dto) throws Exception
   {
	
	   attemptService.submit(dto.getQid(), dto.getUsername(), dto.getCode() , dto.getLanguage());
	   return ResponseEntity.ok("The code has been submitted and is being processed");
   }
   
   @GetMapping("/all")
   @PreAuthorize("hasAuthority('STUDENT')")
   public ResponseEntity<List<AttemptRequestDto>> getAttempts(Principal principal)
   {
	   List<Attempt> attempts = attemptService.getAttempts(principal.getName());
	   List<AttemptRequestDto> attemptDtos = attempts.stream().map((attempt)->{
		  AttemptRequestDto requestDto = new AttemptRequestDto();
		  requestDto.setCode(attempt.getCode());
		  requestDto.setId(attempt.getId());
		  requestDto.setLanguage(attempt.getLanguage());
		  requestDto.setStatus(attempt.getAttemptStatus());
		  requestDto.setTimeStamp(attempt.getTimeStamp());
		  return requestDto;
	   }).collect(Collectors.toList());
	   return ResponseEntity.ok(attemptDtos);
   }

}
