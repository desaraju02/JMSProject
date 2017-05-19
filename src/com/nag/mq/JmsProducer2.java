package com.nag.mq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class JmsProducer2 {
	
	private static String host = "ipediwastst01.northamerica.cerner.net";
	private static int port = 1420;
	private static String channel = "CERN.DEF.SVRCONN";
	private static String queueManagerName = "CERN.DEVTEST";
	private static String destinationName = "Test.RESP";
	private static boolean isTopic = false;
	private static int status = 1;
	
	public JmsProducer2()
	{
		
	}
	
	public void sendMessage(Message msg)
	{
		Connection connection = null;
		 Session session = null;
		 Destination destination = null;
		 MessageProducer producer = null;

		 try {
		   // Create a connection factory
		   JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
		   JmsConnectionFactory cf = ff.createConnectionFactory();

		   // Set the properties
		   cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
		   cf.setIntProperty(WMQConstants.WMQ_PORT, port);
		   cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
		   cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
		   cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManagerName);

		   // Create JMS objects
		   connection = cf.createConnection();
		   session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		   if (isTopic) {
		     destination = session.createTopic(destinationName);
		   }
		   else {
		     destination = session.createQueue(destinationName);
		   }
		   producer = session.createProducer(destination);

		   msg.setJMSCorrelationID(msg.getJMSMessageID());
		   
		    

		   // Start the connection
		   connection.start();

		   // And, send the message
		   producer.send(msg);
		   System.out.println("Sent message:\n" + msg);

		   recordSuccess();
		 }
		 catch (JMSException jmsex) {
		   recordFailure(jmsex);
		 }
		 finally {
		   if (producer != null) {
		     try {
		       producer.close();
		     }
		     catch (JMSException jmsex) {
		       System.out.println("Producer could not be closed.");
		       recordFailure(jmsex);
		     }
		   }

		   if (session != null) {
		     try {
		       session.close();
		     }
		     catch (JMSException jmsex) {
		       System.out.println("Session could not be closed.");
		       recordFailure(jmsex);
		     }
		   }

		   if (connection != null) {
		     try {
		       connection.close();
		     }
		     catch (JMSException jmsex) {
		       System.out.println("Connection could not be closed.");
		       recordFailure(jmsex);
		     }
		   }
		 }
		 System.exit(status);
		 return;

	}

	
	private static void processJMSException(JMSException jmsex) {
		 System.out.println(jmsex);
		 Throwable innerException = jmsex.getLinkedException();
		 if (innerException != null) {
		   System.out.println("Inner exception(s):");
		 }
		 while (innerException != null) {
		   System.out.println(innerException);
		   innerException = innerException.getCause();
		 }
		 return;
		}

		/**
		* Record this run as successful.
		*/
		private static void recordSuccess() {
		 System.out.println("SUCCESS");
		 status = 0;
		 return;
		}

		/**
		* Record this run as failure.
		* 
		* @param ex
		*/
		private static void recordFailure(Exception ex) {
		 if (ex != null) {
		   if (ex instanceof JMSException) {
		     processJMSException((JMSException) ex);
		   }
		   else {
		     System.out.println(ex);
		   }
		 }
		 System.out.println("FAILURE");
		 status = -1;
		 return;
		}

}
