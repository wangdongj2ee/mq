package com.rabbitmq.switchboard.headers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class HeadersAllCustomer {
	
	
	 public static void main(String[] argv) throws Exception  
	    {  
		 String exchangeName = "headerslogs";
	        // 创建连接和频道  
	        ConnectionFactory factory = new ConnectionFactory();  
	        factory.setHost("localhost");  
	        Connection connection = factory.newConnection();  
	        Channel channel = connection.createChannel();  
	        // 声明转发器  
	        channel.exchangeDeclare(exchangeName, "headers");  
	        // 随机生成一个队列  
	        String queueName = channel.queueDeclare().getQueue();
	        
	      //设置消息头键值对信息  
            Map<String, Object> headers = new HashMap<>();  
            //这里x-match有两种类型  
            //all:表示所有的键值对都匹配才能接受到消息  
            //any:表示只要有键值对匹配就能接受到消息  
            headers.put("x-match", "all");  
            headers.put("name", "jack");  
            headers.put("age" , 31);  
	        channel.queueBind(queueName, exchangeName,"",headers);
	        System.out.println("HeadersAllCustomer  Waiting for messages");
	        Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("HeadersAllCustomer Received '" + envelope.getRoutingKey() + "':'" + message + "'");
				}
			};
			channel.basicConsume(queueName, true, consumer);
	    } 
}
