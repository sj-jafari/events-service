package com.event.messaging;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.event.services.EventService;

/**
 * This class is responsible for handling incoming messages from RabbitMQ.
 * 
 * @author Jalal
 * @since 20180324
 * @version
 */
@Component
public class EventMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(EventMessageListener.class);
	@Autowired
	private EventService eventService;

	/**
	 * Consumes an incoming employee event message and stores via
	 * {@link EventService}.
	 * 
	 * @author Jalal
	 * @since 20180324
	 * @version
	 * @param event
	 */
	@RabbitListener(queues = RabbitConfig.QUEUE_EMPLOYEE_EVENTS)
	public void processEmployeeEventMessage(Message event) {

		logger.info("Event Received: " + event.toString());

		// extract message body. We know it should be in json format.
		byte[] body = event.getBody();
		String messagContent = new String(body);
		Document document = Document.parse(messagContent);
		eventService.insertEventDocument(document);

	}
}