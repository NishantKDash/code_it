package com.nishant.code_it.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {
	
	
	@Value("${attemptqueue.name}")
	private String attemptQueueName;
	@Value("${notificationqueue.name}")
	private String notificationQueueName;
	@Value("${attemptqueue.key}")
	private String attemptQueueKey;
	@Value("${notificationqueue.key}")
	private String notificationQueueKey;
	@Value("${exchange.name}")
	private String exchangeName;
   
	@Bean("attemptqueue")
	public Queue attemptqueue()
	{
		return new Queue(attemptQueueName);
	}
	
	@Bean("notificationqueue")
	public Queue notificationqueue()
	{
		return new Queue(notificationQueueName);
	}
	
	@Bean
	public TopicExchange exchange()
	{
		return new TopicExchange(exchangeName);
	}
	@Bean
	public Binding attemptqueuebinding(@Qualifier("attemptqueue")Queue queue , TopicExchange exchange)
	{
		return BindingBuilder.bind(queue).to(exchange).with(attemptQueueKey);
	}
	
	@Bean
	public Binding notificationqueuebinding(@Qualifier("notificationqueue")Queue queue , TopicExchange exchange)
	{
		return BindingBuilder.bind(queue).to(exchange).with(notificationQueueKey);
	}
	@Bean
	public MessageConverter converter()
	{
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory)
	{
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		return rabbitTemplate;
	}
}
