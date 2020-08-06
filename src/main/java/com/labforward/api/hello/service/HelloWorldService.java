package com.labforward.api.hello.service;

import com.labforward.api.core.exception.ResourceNotFoundException;
import com.labforward.api.core.validation.EntityValidator;
import com.labforward.api.hello.domain.Greeting;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class HelloWorldService {

	public static final String GREETING_NOT_FOUND = "Greeting Not Found";

	public static String DEFAULT_ID = "default";

	public static String DEFAULT_MESSAGE = "Hello World!";

	private Map<String, Greeting> greetings;

	private EntityValidator entityValidator;

	public HelloWorldService(EntityValidator entityValidator) {
		this.entityValidator = entityValidator;

		this.greetings = new HashMap<>(1);
		save(getDefault());
	}

	private static Greeting getDefault() {
		return new Greeting(DEFAULT_ID, DEFAULT_MESSAGE);
	}

	public Greeting createGreeting(Greeting request) {
		entityValidator.validateUpdate(request);

		if (request.getId() == null || request.getId().isEmpty())
			request.setId(UUID.randomUUID().toString());
		return save(request);
	}

	public Optional<Greeting> getGreeting(String id) {
		entityValidator.validateId(id);
		Greeting greeting = greetings.get(id);
		if (greeting == null) {
			return Optional.empty();
		}

		return Optional.of(greeting);
	}

	public Optional<Greeting> getDefaultGreeting() {
		return getGreeting(DEFAULT_ID);
	}

	public Greeting updateGreeting(String id, Greeting request){
		entityValidator.validateUpdate(id,request);
		Optional<Greeting> savedGreeting = getGreeting(request.getId());
		
		
		//Update if ID is present
		// Create is ID is missing
		
		if (savedGreeting.isPresent()) {
			Greeting toUpdate = savedGreeting.get(); 
			toUpdate.setMessage(request.getMessage());
			return save(toUpdate);
		}else {
			return createGreeting(request);
		}
		
	}
	
	public void deleteGreeting(String greetingId) {
		Optional<Greeting> savedGreeting = getGreeting(greetingId);
		
		if (savedGreeting.isPresent()) {
			delete(savedGreeting.get());
		}else {
			throw new ResourceNotFoundException(GREETING_NOT_FOUND);
		}
		
		
	}
	
	private Greeting save(Greeting greeting) {
		this.greetings.put(greeting.getId(), greeting);

		return greeting;
	}
	
	private void delete(Greeting greeting) {
		this.greetings.remove(greeting.getId());

	}
}
