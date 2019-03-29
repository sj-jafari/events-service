package com.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.event.services.EventService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTests {
	@Autowired
	private EventService eventService;
	private String sampleDocument1 = "{\"entityName\":\"Employee\",\"eventType\":\"CREATED\",\"timestamp\":\"2019-03-29 00:15:44.929\",\"employee\":{\"id\":\"ce437015-2dad-4259-abe4-6a2f9d914fb3\",\"name\":\"Jane Doe\",\"email\":\"jane@doe.com\",\"birthday\":\"1989-01-01\",\"department\":null}}\n";
	private String sampleDocument2 = "{\"entityName\":\"Employee\",\"eventType\":\"CREATED\",\"timestamp\":\"2019-03-29 00:15:44.929\",\"employee\":{\"id\":\"11111111-1111-1111-1111-111111111111\",\"name\":\"Jane Doe\",\"email\":\"jane@doe.com\",\"birthday\":\"1989-01-01\",\"department\":null}}\n";

	@Test
	public void contextLoads() {
		assertNotNull(eventService);
	}

	@Test
	public void insertEvent() {
		int countBeforeInsert = eventService.getAllEventDocuments().size();
		Document eventDocument = Document.parse(sampleDocument1);
		eventService.insertEventDocument(eventDocument);
		int countAfterInsert = eventService.getAllEventDocuments().size();

		assertEquals(countBeforeInsert + 1, countAfterInsert);
	}

	@Test
	public void getAllEvents() {
		Document eventDocument = Document.parse(sampleDocument1);
		eventService.insertEventDocument(eventDocument);
		List<Document> eventsList = eventService.getAllEventDocuments();

		assertNotNull(eventsList);
		assertNotEquals(0, eventsList.size());
	}

	@Test
	public void getEmployeeEvents() throws JSONException {
		Document eventDocument1 = Document.parse(sampleDocument1);
		Document eventDocument2 = Document.parse(sampleDocument2);

		JSONObject jsonObject = new JSONObject(eventDocument2.toJson());
		String id = jsonObject.getJSONObject("employee").getString("id");

		int countBeforeInsert = eventService.getAllEventDocumentsForAnEmployee(id).size();

		eventService.insertEventDocument(eventDocument1);
		eventService.insertEventDocument(eventDocument2);

		int countAfterInsert = eventService.getAllEventDocumentsForAnEmployee(id).size();

		assertEquals(countBeforeInsert + 1, countAfterInsert);
	}

}
