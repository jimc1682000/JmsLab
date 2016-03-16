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

//Queue�]�I���I�^�覡  �ͦs��Producer
/*
Queue�]�I���I�^�覡�MTopic�]�o�G/�q�\�^�覡 ���B�浲�G�̩��㪺�ϧO�O�G�bQueue�]�I���I�^�覡�����B��Ͳ���,�A�B����O��,���O���ٯ౵��������F
��Topic�]�o�G/�q�\�^�覡�N���P�F�A���B��o�G�̡A�A�B��q�\�̡A�q�\�̦��쪺����
�i��S���Ϊ̬O���������C
*/
public class QueueProducer {
	private static String user = ActiveMQConnection.DEFAULT_USER;
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private static String url = "tcp://localhost:61616";
	private static String nameOfQueue = "example.A";

	public static void main(String[] args) throws Exception {
		// ConnectionFactory �G�s���u�t�AJMS �Υ��Ыسs��
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		// Connection �GJMS �Τ�ݨ�JMS Provider ���s��
		Connection connection = connectionFactory.createConnection();
		// Connection �Ұ�
		connection.start();
		System.out.println("Connection is start...");
		// Session�G �@�ӵo�e�α��������������
		Session session = connection.createSession(Boolean.TRUE,
				Session.AUTO_ACKNOWLEDGE);
		// Queue �G�������ت��a;�����o�e����.
		Destination destination = session.createQueue(nameOfQueue);
		// MessageProducer�G�����o�e��
		MessageProducer producer = session.createProducer(destination);
		// �]�m�����[�ơA���B�ǲߡA��ڮھڱM�רM�w
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		// �c�y�����A���B�g���A���شN�O�ѼơA�Ϊ̤�k���
		sendMessage(session, producer);
		session.commit();
		connection.close();
		System.out.println("send text ok.");
	}

	public static void sendMessage(Session session, MessageProducer producer)
			throws Exception {
		for (int i = 1; i <= 100; i++) {// ������,�F��1000�N����
			TextMessage message = session.createTextMessage("ActiveMq �o�e������"
					+ i);
			// �o�e������ت��a��
			System.out.println("�o�e�����G" + "ActiveMq �o�e������" + i);
			producer.send(message);
		}
	}
}
