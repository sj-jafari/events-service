package com.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.event.controller.EventServiceController;
import com.event.messaging.EventMessageListener;
import com.event.messaging.RabbitConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationTests {
	@Autowired
	private EventServiceController controller;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private EventMessageListener eventMessageListener;
	private String sampleMessage = "{\"entityName\":\"Employee\",\"eventType\":\"TEST\",\"timestamp\":\"2019-03-29 00:15:44.929\",\"employee\":{\"id\":\"ce437015-2dad-4259-abe4-6a2f9d914fb3\",\"name\":\"Jane Doe\",\"email\":\"jane@doe.com\",\"birthday\":\"1989-01-01\",\"department\":null}}\n";

	@Test
	public void contextLoads() {
		assertNotNull(controller);
	}

	@Test
	public void helloMessageTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/hello")).andDo(print()).andExpect(status().isOk()).andReturn();

		assertEquals("Hello!", result.getResponse().getContentAsString());
	}

	@Test
	public void getEventsTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/event")).andDo(print()).andExpect(status().isOk()).andReturn();
		JSONArray jsonResult = new JSONArray(result.getResponse().getContentAsString());

		assertNotNull(jsonResult);
	}

	@Test
	public void getEmployeeEventsTest() throws Exception {
		Document eventDocument1 = Document.parse(sampleMessage);

		JSONObject jsonObject = new JSONObject(eventDocument1.toJson());
		String id = jsonObject.getJSONObject("employee").getString("id");

		MvcResult result = mockMvc.perform(get("/api/v1/event/" + id)).andDo(print()).andExpect(status().isOk())
				.andReturn();
		JSONArray jsonResult = new JSONArray(result.getResponse().getContentAsString());

		assertNotNull(jsonResult);
	}

	@Test
	public void eventProcessingTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/event")).andDo(print()).andExpect(status().isOk()).andReturn();
		JSONArray jsonResultBeforeEvent = new JSONArray(result.getResponse().getContentAsString());
		int sizeBeforeEvent = jsonResultBeforeEvent.length();

		// send message
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setConsumerQueue(RabbitConfig.QUEUE_EMPLOYEE_EVENTS);
		Message message = new Message(sampleMessage.getBytes(), messageProperties);
		eventMessageListener.processEmployeeEventMessage(message);
		Thread.sleep(3000);

		result = mockMvc.perform(get("/api/v1/event")).andDo(print()).andExpect(status().isOk()).andReturn();
		JSONArray jsonResultAfterEvent = new JSONArray(result.getResponse().getContentAsString());
		int sizeAfterEvent = jsonResultAfterEvent.length();

		assertNotEquals(sizeBeforeEvent, sizeAfterEvent);

	}

}
