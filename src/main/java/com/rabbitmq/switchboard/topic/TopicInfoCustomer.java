package com.rabbitmq.switchboard.topic;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class TopicInfoCustomer {
	
	private static final String EXCHANGE_NAME = "topic_logs";  
    public static void main(String[] argv) throws Exception  
    {  
        // 创建连接和频道  
        ConnectionFactory factory = new ConnectionFactory();  
        factory.setHost("localhost");  
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
        // 声明转发器  
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");  
        // 随机生成一个队列  
        String queueName = channel.queueDeclare().getQueue();  
        //接收所有与kernel相关的消息  
        channel.queueBind(queueName, EXCHANGE_NAME, "*.info");
        System.out.println("TopicInfoCustomer  Waiting for messages");
        Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("ReceiveLogsDirect1 Received '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
    }  
}
