package com.nishant.code_it.models;

import java.time.LocalDateTime;

import com.nishant.code_it.enums.AttemptStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attempt extends BaseModel{
   
	@ManyToOne
	private UserEntity user;
	@ManyToOne
	private Question question;
	@Enumerated(EnumType.STRING)
	private AttemptStatus attemptStatus;
	private String code;
	private LocalDateTime timeStamp;
}
