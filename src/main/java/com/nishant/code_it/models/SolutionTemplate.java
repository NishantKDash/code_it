package com.nishant.code_it.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SolutionTemplate extends BaseModel{
 
	private String java;
	private String python;
	private String cpp;
}
