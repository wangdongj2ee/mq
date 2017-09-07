package com.rabbitmq.response;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Basic.Deliver;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Customer {

	public final static String QUEUE_NAME = "rabbitMQ.test";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost("localhost");
		// 创建一个新的连接
		Connection connection = factory.newConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();
		// 声明要关注的队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("Customer Waiting Received messages");
		channel.basicQos(1);//指定该消费者每次接受多少个消息，只要等接收到的消息都接收完成了才接收新的消息
		// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Customer Received '" + message + "'");
				try {
					//Thread.sleep(10000);
					channel.basicAck(envelope.getDeliveryTag(), false);  
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Customer Received '" + message + "'");
			}
		};
		boolean ack = false;//消息确认 truew为自动确认  false需要手动确认，如果此值为false的话那么需要channel.basicAck来确认消息消费完成
		channel.basicConsume(QUEUE_NAME, ack, consumer);
	}
}
