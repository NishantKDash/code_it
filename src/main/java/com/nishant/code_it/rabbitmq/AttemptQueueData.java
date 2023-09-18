package com.nishant.code_it.rabbitmq;

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
	private String code;

}
