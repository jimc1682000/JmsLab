package com.lab.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

//Queue（點對點）方式  生存者Producer
/*
Queue（點對點）方式和Topic（發佈/訂閱）方式 的運行結果最明顯的區別是：在Queue（點對點）方式中先運行生產者,再運行消費者,消費者還能接受到消息；
而Topic（發佈/訂閱）方式就不同了，先運行發佈者，再運行訂閱者，訂閱者收到的消息
可能沒有或者是不完全的。
*/
public class QueueProducer {
	private static String user = ActiveMQConnection.DEFAULT_USER;
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private static String url = "tcp://localhost:61616";
	private static String nameOfQueue = "example.A";

	public static void main(String[] args) throws Exception {
		// ConnectionFactory ：連接工廠，JMS 用它創建連接
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		// Connection ：JMS 用戶端到JMS Provider 的連接
		Connection connection = connectionFactory.createConnection();
		// Connection 啟動
		connection.start();
		System.out.println("Connection is start...");
		// Session： 一個發送或接收消息的執行緒
		Session session = connection.createSession(Boolean.TRUE,
				Session.AUTO_ACKNOWLEDGE);
		// Queue ：消息的目的地;消息發送給誰.
		Destination destination = session.createQueue(nameOfQueue);
		// MessageProducer：消息發送者
		MessageProducer producer = session.createProducer(destination);
		// 設置不持久化，此處學習，實際根據專案決定
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		// 構造消息，此處寫死，項目就是參數，或者方法獲取
		sendMessage(session, producer);
		session.commit();
		connection.close();
		System.out.println("send text ok.");
	}

	public static void sendMessage(Session session, MessageProducer producer)
			throws Exception {
		for (int i = 1; i <= 100; i++) {// 有限制,達到1000就不行
			TextMessage message = session.createTextMessage("ActiveMq 發送的消息"
					+ i);
			// 發送消息到目的地方
			System.out.println("發送消息：" + "ActiveMq 發送的消息" + i);
			producer.send(message);
		}
	}
}
