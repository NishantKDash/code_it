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
public class QuestionAdditionDto {
   private String title;
   private String description;
   private List <ExampleDto> examples;
   private List <TestCaseDto> testCases;
}
