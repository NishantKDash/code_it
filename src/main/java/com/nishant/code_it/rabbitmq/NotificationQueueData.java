package com.nishant.code_it.rabbitmq;

import java.util.List;

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
	private List<String> results;
	private String username;
}
