package com.nishant.code_it.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.code_it.dtos.LogOutDto;
import com.nishant.code_it.dtos.LoginRequestDto;
import com.nishant.code_it.dtos.LoginResponseDto;
import com.nishant.code_it.dtos.RegisterRequestDto;
import com.nishant.code_it.dtos.RegisterResponseDto;
import com.nishant.code_it.enums.Role;
import com.nishant.code_it.exceptions.UserAlreadyExists;
import com.nishant.code_it.exceptions.UserDoesNotExist;
import com.nishant.code_it.exceptions.WrongPassword;
import com.nishant.code_it.models.UserEntity;
import com.nishant.code_it.security.JwtService;
import com.nishant.code_it.services.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private JwtService jwtService;
	
  
	@PostMapping("/register/student")
	public ResponseEntity<RegisterResponseDto> registerStudent(@RequestBody RegisterRequestDto dto) throws UserAlreadyExists
	{
		 RegisterResponseDto response = new RegisterResponseDto();
		 UserEntity student = userService.register(dto.getUsername(), dto.getName(), dto.getPassword(), Role.STUDENT);
		 mapper.map(student, response);
		 return ResponseEntity.ok(response);
	}
	
	@PostMapping("/register/admin")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<RegisterResponseDto> registerAdmin(@RequestBody RegisterRequestDto dto) throws UserAlreadyExists
	{
		RegisterResponseDto response = new RegisterResponseDto();
		 UserEntity student = userService.register(dto.getUsername(), dto.getName(), dto.getPassword(), Role.ADMIN);
		 mapper.map(student, response);
		 return ResponseEntity.ok(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) throws UserDoesNotExist, WrongPassword
	{
		UserEntity user = userService.verify(dto.getUsername(), dto.getPassword());
		LoginResponseDto response = new LoginResponseDto();
		response.setUsername(user.getUsername());
		String jwt = jwtService.createToken(user.getUsername() , user.getRole());
		response.setToken(jwt);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(@RequestBody LogOutDto dto)
	{
	    jwtService.logout(dto.getToken());
		return ResponseEntity.ok("Successfully logged out");
	}
	
	@GetMapping("/student")
	@PreAuthorize("hasAuthority('STUDENT')")
	public ResponseEntity<String> detailS()
	{
		return ResponseEntity.ok("This is a student");
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> detailA()
	{
		return ResponseEntity.ok("This is a admin");
	}
}
