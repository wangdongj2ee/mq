package com.rabbitmq.switchboard.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Direct交换机
 * 通过routingKeys将消息转发到对应交换机绑定的队列上，这个交换机绑定了多个队列那么每个队列都会收到消息
 * @author clcw9369
 *
 */

public class DirectProducer {

	private static final String EXCHANGE_NAME = "direct_logs";//  交换机名称
	private static final String[] routingKeys = new String[] { "info", "warning", "error" };

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");// 注意是direct
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
