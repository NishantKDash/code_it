package com.nishant.code_it.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
  
	private Long qid;
	private String title;
	private String description;
	private String solutionTemplate;
	private List<ExampleDto> examples;
}
