package com.rabbitmq.one;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * mq生产者
 * @author clcw9369
 *
 */
public class Producer {
	
	public final static String QUEUE_NAME="rabbitMQ.test";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		boolean durable = false;//如果我们声明一个持久队列（队列将在服务器重启后幸存），则为true。
		boolean exclusive = false;//是否是仅限于此连接的专属队列
		boolean autoDelete = false;//在最后一个连接断开时是否自动删除队列
		channel.queueDeclare(QUEUE_NAME, durable,exclusive,autoDelete,null);
		String message="hello world";
		channel.basicPublish("", QUEUE_NAME,null,message.getBytes("UTF-8"));
		channel.close();
		connection.close();
		System.out.println("发送完成，message="+message);
	}
}
