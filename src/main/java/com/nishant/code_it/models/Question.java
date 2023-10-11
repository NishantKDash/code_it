package com.nishant.code_it.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseModel{
   
	
	private String title;
	private String description;
	private String solutionTemplate;
	@OneToMany(mappedBy = "question" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	private List<Example> examples;
	@OneToMany(mappedBy = "question" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	private List<TestCase> testCases;
}
