package com.event.services;

import java.util.List;

import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * A general interface for definition of services available for events
 * 
 * @author Jalal
 * @since 20190324
 */
@Service
public interface EventService {

	/**
	 * Receives an event in Json format and stores it in db.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param document
	 */
	public void insertEventDocument(Document document);

	/**
	 * returns list of all event documents.
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @return List<Document>
	 */
	public List<Document> getAllEventDocuments();
	
	/**
	 * returns list of all event documents for an employee, given the employee's id;
	 * 
	 * @author Jalal
	 * @since 20190324
	 * @param id
	 */
	public List<Document> getAllEventDocumentsForAnEmployee(String id);
}
