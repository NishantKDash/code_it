package com.nishant.code_it.rabbitmq;

import com.nishant.code_it.enums.Language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttemptQueueData {
	
	private String username;
	private Long qid;
	private Long attemptId;
	private String code;
	private Language language;

}
