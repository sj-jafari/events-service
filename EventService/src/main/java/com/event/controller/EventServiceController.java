package com.event.controller;

import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.services.EventService;

@RestController
@RequestMapping("/api/v1")
public class EventServiceController {
	private final Logger logger = LoggerFactory.getLogger(EventServiceController.class);
	@Autowired
	EventService eventService;

	@GetMapping("/hello")
	public ResponseEntity<?> getHelloMessage() {
		String message = "Hello!";
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	@GetMapping(path = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllEvents() {
		logger.info("Request to get all events: {}");
		List<Document> result = eventService.getAllEventDocuments();
		return new ResponseEntity<List<Document>>(result, HttpStatus.OK);
	}
	
	@GetMapping(path = "/event/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEmployeeEvents(@PathVariable(value = "id") String id) {
		logger.info("Request to get employee events: {}", id);
		List<Document> result = eventService.getAllEventDocumentsForAnEmployee(id);
		return new ResponseEntity<List<Document>>(result, HttpStatus.OK);
	}

}
