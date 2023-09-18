package com.nishant.code_it.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/about")
public class AboutController {

	@GetMapping("")
	public ResponseEntity<String> about()
	{
		return ResponseEntity.ok("This is the backend of Code_it");
	}
	
}
