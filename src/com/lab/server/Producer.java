package com.lab.server;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer implements Runnable {
	private String brokerUrl = "tcp://0.0.0.0:61616";
	private String queue = "TEST";

	private ActiveMQConnectionFactory connectionFactory;
	private Connection conn;
	private Destination dest;
	private Session session;
	private MessageProducer producer;

	@Override
	public void run() {
		try {
			start();
			stop();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	private void start() throws JMSException {
		// Create a ConnectionFactory
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		// Create a Connection
		conn = connectionFactory.createConnection();
		conn.start();
		// Create a Session
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Create the destination (Topic or Queue)
		dest = session.createQueue(queue);

		// Create a MessageProducer from the Session to the Topic or Queue
		producer = session.createProducer(dest);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		// Create a messages
		String text = "Hello world! From: " + Thread.currentThread().getName()
				+ " : " + this.hashCode();
		TextMessage message = session.createTextMessage(text);

		// Tell the producer to send the message
		System.out.println("Sent message: " + message.hashCode() + " : "
				+ Thread.currentThread().getName());
		producer.send(message);
	}

	private void stop() throws JMSException {
		// Clean up
		session.close();
		conn.close();
	}
}