package com.nishant.code_it.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDto {
  
	@NotNull
	private String username;
	@NotNull
	private String name;
    @NotNull
	private String password;
} 
