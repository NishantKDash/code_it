package com.nishant.code_it.models;

import jakarta.persistence.Entity;
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
public class Example extends BaseModel{

	@ManyToOne
	private Question question;
	private String input;
	private String output;
	private String explanation;
}
