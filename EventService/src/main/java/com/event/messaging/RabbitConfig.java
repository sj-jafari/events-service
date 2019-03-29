package com.event.messaging;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
 
/**
 * The configuration class for RabbitMQ message broker. Connection info are
 * introduced in application.properties file.
 * 
 * @author Jalal
 * @since 20190324
 */
@Configuration
@Primary
public class RabbitConfig  
{
    public static final String QUEUE_EMPLOYEE_EVENTS = "employee-events-queue";
    public static final String EXCHANGE_EMPLOYEES = "employee-exchange";
 
    /**
	 * Returns a queue for employee events.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Queue
	 */
    @Bean
    Queue employeeEventsQueue() {
        return QueueBuilder.durable(QUEUE_EMPLOYEE_EVENTS).build();
    }
 
    /**
	 * Returns an exchange for employee events.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Exchange
	 */
    @Bean
    Exchange employeesExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_EMPLOYEES).build();
    }
 
    /**
	 * Binds the employeeEventsQueue to employeesExchange with the
	 * employeeEventsQueue as routing key.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return Binding
	 */
    @Bean
    Binding binding(Queue employeeEventsQueue, TopicExchange employeesExchange) {
        return BindingBuilder.bind(employeeEventsQueue).to(employeesExchange).with(QUEUE_EMPLOYEE_EVENTS);
    }
}