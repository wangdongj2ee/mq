package com.rabbitmq.switchboard.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Topic交换机
 * 根据规则转发消息，比如在消费者配置routingKey为kernel.*时，匹配所有kernel开头的之后一个标识符的消息将转发到此队列，如果为kernel.#时匹配所有kernel开头的信息
 * 
   *可以匹配一个标识符。
   #可以匹配0个或多个标识符。
   Routing key的长度不能超过255个字节。
 * @author clcw9369
 *
 */

public class TopicProducer {
	
	private static final String EXCHANGE_NAME = "topic_logs";//  交换机名称
	private static final String[] routingKeys = new String[] {"kernel.info", "cron.warning",  "auth.info", "kernel.critical" };
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");// 注意是direct
		// 发送信息
		for (String routingKey : routingKeys) {
			String message = "RoutingSendDirect Send the message level:" + routingKey;
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
			System.out.println("RoutingSendDirect Send" + routingKey + "':'" + message);
		}
		channel.close();
		connection.close();
	}
}
