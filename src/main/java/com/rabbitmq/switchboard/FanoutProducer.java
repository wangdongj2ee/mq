package com.rabbitmq.switchboard;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * 创建一个名字叫logs的fanout交换机，此交换机会将所有的信息数据发送到绑定到此交换机的所有的队列
 * @author clcw9369
 *
 */

public class FanoutProducer {
	
	public static void main(String[] args) throws IOException, TimeoutException {
		String exchangeName = "logs";
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName,"fanout");
		for (int i=0;i<5;i++){
            String message="Hello World"+i;
            channel.basicPublish(exchangeName,"",null,message.getBytes());
            System.out.println("EmitLog Sent '" + message + "'");
        }
        channel.close();
        connection.close();
	}
}
