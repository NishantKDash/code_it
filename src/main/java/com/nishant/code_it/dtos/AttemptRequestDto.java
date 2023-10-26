package com.nishant.code_it.dtos;

import java.time.LocalDateTime;

import com.nishant.code_it.enums.AttemptStatus;
import com.nishant.code_it.enums.Language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttemptRequestDto {
	
	private Long id;
	private AttemptStatus status;
	private String code;
	private Language language;
	private LocalDateTime timeStamp;

}
