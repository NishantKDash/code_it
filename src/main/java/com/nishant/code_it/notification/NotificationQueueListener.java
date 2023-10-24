package com.nishant.code_it.notification;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.nishant.code_it.rabbitmq.NotificationQueueData;

@Service
public class NotificationQueueListener {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
 
	@RabbitListener(queues = "${notificationqueue.name}")
	public void sendNotification(NotificationQueueData notificationData)
	{
		
		List<String> results  = notificationData.getResults();
		SocketMessage message = new SocketMessage();
		message.setAttemptId(notificationData.getAttemptId());
		message.setQid(notificationData.getQid());
		message.setResult(notificationData.getAttemptStatus().toString());
		
		String destination = "/notify/" + notificationData.getUsername();
		messagingTemplate.convertAndSend(destination, message);
	}
}
