package com.nishant.code_it.rabbitmq;

import org.springframework.beans.factory.annotation.Value;

public class QueueNames {

	@Value("${attemptqueue.name}")
	public static String attemptQueueName;
	@Value("${notificationqueue.name}")
	public static String notificationQueueName;
	@Value("${attemptqueue.key}")
	public static String attemptQueueKey;
	@Value("${notificationqueue.key}")
	public static String notificationQueueKey;
	@Value("${exchange.name}")
	public static String exchangeName;
   
}
