package com.lab.sendobj;

import java.util.ArrayList;
import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

//Queue�]�I���I�^�覡  ���O�oConsumer
/*
 Queue�]�I���I�^�覡�MTopic�]�o�G/�q�\�^�覡 ���B�浲�G�̩��㪺�ϧO�O�G�bQueue�]�I���I�^�覡�����B��Ͳ���,�A�B����O��,���O���ٯ౵��������F
 ��Topic�]�o�G/�q�\�^�覡�N���P�F�A���B��o�G�̡A�A�B��q�\�̡A�q�\�̦��쪺����
 �i��S���Ϊ̬O���������C
 */
public class QueueConsumer {
	private static String user = ActiveMQConnection.DEFAULT_USER;
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private static String url = "tcp://localhost:61616";
	private static String nameOfQueue = "example.A";

	public static void main(String[] args) throws Exception {
		// ConnectionFactory �G�s���u�t�AJMS �Υ��Ыسs��
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		// �]�w�i�H����Package�H�ѤϧǦC��
		// System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES",
		// "com.lab.sendobj");
		connectionFactory.setTrustedPackages(new ArrayList<String>(Arrays
				.asList("com.lab.sendobj")));
		// Connection �GJMS �Τ�ݨ�JMS Provider ���s��
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// Session�G �@�ӵo�e�α��������������
		final Session session = connection.createSession(Boolean.TRUE,
				Session.AUTO_ACKNOWLEDGE);
		// Destination �G�������ت��a;�����o�e����.
		Destination destination = session.createQueue(nameOfQueue);
		// ���O�̡A����������
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MessageListener() {// ���ưȭ���
			@Override
			public void onMessage(Message message) {
				ObjectMessage objMsg = (ObjectMessage) message;
				SerializableObject obj = null;
				try {
					obj = (SerializableObject) objMsg.getObject();
				} catch (JMSException e1) {
					e1.printStackTrace();
				}
				System.out.println(obj.toString());
				try {
					session.commit();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		/*
		 * �t�~�@�ر����覡 while (true) { //�]�m�����̱����������ɶ��A���F�K����աA�o�̽֩w��100s TextMessage
		 * message = (TextMessage) consumer.receive(100000); if (null !=
		 * message) { System.out.println("�������" + message.getText()); } else {
		 * break; } }
		 */
	}
}