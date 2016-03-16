package com.lab.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

//Queue（點對點）方式  消費這Consumer
/*
Queue（點對點）方式和Topic（發佈/訂閱）方式 的運行結果最明顯的區別是：在Queue（點對點）方式中先運行生產者,再運行消費者,消費者還能接受到消息；
而Topic（發佈/訂閱）方式就不同了，先運行發佈者，再運行訂閱者，訂閱者收到的消息
可能沒有或者是不完全的。
*/
public class QueueConsumer {
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
		connection.start();
		// Session： 一個發送或接收消息的執行緒
		final Session session = connection.createSession(Boolean.TRUE,
				Session.AUTO_ACKNOWLEDGE);
		// Destination ：消息的目的地;消息發送給誰.
		Destination destination = session.createQueue(nameOfQueue);
		// 消費者，消息接收者
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MessageListener() {// 有事務限制
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					System.out.println(textMessage.getText());
				} catch (JMSException e1) {
					e1.printStackTrace();
				}
				try {
					session.commit();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		/*
		 * 另外一種接受方式 while (true) { //設置接收者接收消息的時間，為了便於測試，這裡誰定為100s TextMessage
		 * message = (TextMessage) consumer.receive(100000); if (null !=
		 * message) { System.out.println("收到消息" + message.getText()); } else {
		 * break; } }
		 */
	}
}
