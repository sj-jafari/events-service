package com.event.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.FindIterable;

/**
 * Provides an implementation for {@link EventService} which uses MongoDB to
 * store and retrieve Events.
 * 
 * @author Jalal
 * @since 20190324
 */
@Service
public class EventServiceImpl implements EventService {
	@Autowired
	private MongoTemplate mongoTemplate;
	private String eventCollectionName = "events";

	/**
	 * Receives an event in Json format and stores it in mongodb.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param document
	 */
	@Override
	public void insertEventDocument(Document document) {
		// eventRepository.insert(document);
		mongoTemplate.insert(document, eventCollectionName);
	}

	/**
	 * Returns list of all event documents ordered by their occurrence timestamp
	 * (asc).
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return List<Document>
	 */
	@Override
	public List<Document> getAllEventDocuments() {
		FindIterable<Document> docs = mongoTemplate.getCollection(eventCollectionName).find()
				.sort(Document.parse("{timestamp:1}"));
		List<Document> list = new ArrayList<Document>();
		for (Document doc : docs) {
			list.add(doc);
		}

		return list;
	}

	/**
	 * returns list of all event documents for an employee, given the employee's id;
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param id
	 */
	@Override
	public List<Document> getAllEventDocumentsForAnEmployee(String id) {
		Document filterDocument = new Document();
		filterDocument.put("employee.id", id);
		FindIterable<Document> docs = mongoTemplate.getCollection(eventCollectionName).find(filterDocument)
				.sort(Document.parse("{timestamp:1}"));
		List<Document> list = new ArrayList<Document>();
		for (Document doc : docs) {
			list.add(doc);
		}

		return list;
	}

}
