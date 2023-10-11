package com.nishant.code_it.notification;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {
	@MessageMapping("/notify/{username}")
	@SendTo("/topic/{username}")
	public SocketMessage message(@RequestBody SocketMessage message , @DestinationVariable("username") String username)
	{
		return message;
	}

}
