package com.nishant.code_it.rabbitmq;

import com.nishant.code_it.enums.AttemptStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueueData {
 
	private Long qid;
	private Long attemptId;
	private AttemptStatus attemptStatus;
	private String message;
	private String username;
}
