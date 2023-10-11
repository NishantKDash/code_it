package com.nishant.code_it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.code_it.dtos.AttemptDto;
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

}
