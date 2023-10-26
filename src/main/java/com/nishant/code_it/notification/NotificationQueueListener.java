package com.nishant.code_it.notification;

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
		
		SocketMessage message = new SocketMessage();
		message.setAttemptId(notificationData.getAttemptId());
		message.setQid(notificationData.getQid());
		message.setResult(notificationData.getAttemptStatus().toString());
		message.setMessage(notificationData.getMessage());
		
		String destination = "/topic/" + notificationData.getUsername();
		messagingTemplate.convertAndSend(destination, message);
	}
}
