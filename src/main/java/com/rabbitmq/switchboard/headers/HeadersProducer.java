package com.rabbitmq.switchboard.headers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Headers交换机
 * 通过参数匹配队列转发消息，在生产消息时创建一个Map，在map里面放参数，把map赋值给Builder的headers属性，通过Builder的build方法创建
 * 一个BasicProperties对象，并在放消息的时候传参
 * 消费者在与交换机绑定时也需要一个map，里面至少需要一个参数x-match，x-match分为any和all，any代表这个map里面的参数与生产者的map里面参数
 * 有一个能匹配上就会转发消息，all代表所有参数都要匹配上才会转发消息
 * 
 * @author clcw9369
 *
 */

public class HeadersProducer {
	
	
	public static void main(String[] args) throws IOException, TimeoutException {
		String exchangeName = "headerslogs";
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName,"headers");
		String message = "exchanges type headers test msg~";
		Map<String, Object> headers = new HashMap<>();  
        headers.put("name", "jack");  
        headers.put("age", 31);
        Builder builder = new Builder();
        builder.headers(headers);
        channel.basicPublish(exchangeName,"",builder.build(),message.getBytes());
        channel.close();
        connection.close();
	}
}
