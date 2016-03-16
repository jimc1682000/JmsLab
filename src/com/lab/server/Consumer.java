package com.lab.server;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer implements ExceptionListener {
	private String brokerUrl = "tcp://0.0.0.0:61616";
	private String queue = "TEST";

	private ActiveMQConnectionFactory connectionFactory;
	private Connection conn;
	private Session session;
	private Destination dest;
	MessageConsumer consumer;

	public String start() throws JMSException {
		// Create a ConnectionFactory
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		// Create a Connection
		conn = connectionFactory.createConnection();
		conn.start();
		conn.setExceptionListener(this);
		// Create a Session
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Create the destination (Topic or Queue)
		dest = session.createQueue(queue);
		// Create a MessageConsumer from the Session to the Topic or Queue
		consumer = session.createConsumer(dest);
		// set MessageListener to get message
		// Need to implements MessageListener
		// consumer.setMessageListener(this);
		// Message msg = consumer.receive(1000);
		Message msg = consumer.receive();
		if (msg instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) msg;
			String text = "";
			try {
				text = textMessage.getText();
			} catch (JMSException e) {
				e.printStackTrace();
			}
			return "Received: " + text;
		} else {
			return "Received: " + msg;
		}
	}

	public void stop() throws JMSException {
		consumer.close();
		session.close();
		conn.close();
	}

	public void onException(JMSException ex) {
		ex.printStackTrace();
	}

	public String rtnMsg(String msg) {
		return msg;
	}
}