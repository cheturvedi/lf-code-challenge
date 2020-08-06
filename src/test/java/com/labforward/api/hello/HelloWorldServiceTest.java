package com.labforward.api.hello;

import com.labforward.api.core.exception.EntityValidationException;
import com.labforward.api.core.exception.ResourceNotFoundException;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldServiceTest {

	@Autowired
	private HelloWorldService helloService;

	public HelloWorldServiceTest() {
	}

	@Test
	public void getDefaultGreetingIsOK() {
		Optional<Greeting> greeting = helloService.getDefaultGreeting();
		Assert.assertTrue(greeting.isPresent());
		Assert.assertEquals(HelloWorldService.DEFAULT_ID, greeting.get().getId());
		Assert.assertEquals(HelloWorldService.DEFAULT_MESSAGE, greeting.get().getMessage());
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithEmptyMessageThrowsException() {
		final String EMPTY_MESSAGE = "";
		helloService.createGreeting(new Greeting(EMPTY_MESSAGE));
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithNullMessageThrowsException() {
		helloService.createGreeting(new Greeting(null));
	}

	@Test
	public void createGreetingOKWhenValidRequest() {
		final String HELLO_LUKE = "Hello Luke";
		Greeting request = new Greeting(HELLO_LUKE);

		Greeting created = helloService.createGreeting(request);
		Assert.assertEquals(HELLO_LUKE, created.getMessage());
	}
	
	/*
	 * Get Greeting tests 
	 */
	@Test(expected = EntityValidationException.class)
	public void getGreetingWithEmptyIDThrowsException(){
		helloService.getGreeting("");
	}
	
	@Test(expected = EntityValidationException.class)
	public void getGreetingWithNullIDThrowsException() {
		helloService.getGreeting(null);
	}
	
	@Test(expected = EntityValidationException.class)
	public void getGreetingOKWithInvalidIDthrowsException() {
		helloService.getGreeting("notAnID");
	}
	
	@Test
	public void getGreetingOKWhenValidRequest() {
		final String HELLO_LUKE = "Hello Luke";
		Greeting request = new Greeting(HELLO_LUKE);

		Greeting created = helloService.createGreeting(request);
		Optional<Greeting> retreived = helloService.getGreeting(created.getId());
		
		Assert.assertEquals(created.getId(), retreived.get().getId());
		Assert.assertEquals(created.getMessage(), retreived.get().getMessage());
		
	}
	
	/*
	 * Update Greeting tests
	 */
	
	
	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithEmptyIDThrowsException() {
		final String EMPTY_MESSAGE = "";
		final String EMPTY_ID = "";
		Greeting request = new Greeting(EMPTY_MESSAGE);
		request.setId(EMPTY_ID);
		helloService.updateGreeting(EMPTY_ID,request);
	}

	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithNullIDThrowsException() {
		final String EMPTY_MESSAGE = "";
		Greeting request = new Greeting(EMPTY_MESSAGE);
		request.setId(null);
		helloService.updateGreeting(null,request);
	}
	
	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithInvalidIDThrowsException() {
		final String EMPTY_MESSAGE = "";
		final String INVALID_ID = "invalid";
		Greeting request = new Greeting(EMPTY_MESSAGE);
		request.setId(INVALID_ID);
		helloService.updateGreeting(INVALID_ID,request);
	}
	
	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithEmptyMessageThrowsException() {
		final String EMPTY_MESSAGE = "";
		final String id=UUID.randomUUID().toString();
		Greeting request = new Greeting(EMPTY_MESSAGE);
		request.setId(id);
		helloService.updateGreeting(id,request);
	}

	@Test(expected = EntityValidationException.class)
	public void updateGreetingWithNullMessageThrowsException() {
		helloService.updateGreeting(UUID.randomUUID().toString(),new Greeting(null));
	}
	
	@Test
	public void updateExistingGreetingOKWhenValidRequest() {
		final String HELLO_LUKE = "Hello Luke";
		Greeting request = new Greeting(HELLO_LUKE);

		Greeting created = helloService.createGreeting(request);
		
		final String HELLO_VADER = "Hello vader";
		created.setMessage(HELLO_VADER);
		helloService.updateGreeting(created.getId(),created);
		
		Optional<Greeting> retreived = helloService.getGreeting(created.getId());
		
		Assert.assertEquals(created.getId(), retreived.get().getId());
		Assert.assertEquals(created.getMessage(), retreived.get().getMessage());
		
	}
	
	@Test
	public void updateNewGreetingOKWhenValidRequest() {
		final String HELLO_VADER = "Hello vader";
		final String id= UUID.randomUUID().toString();
		Greeting request = new Greeting(HELLO_VADER);
		request.setId(id);
		helloService.updateGreeting(id,request);
		
		Optional<Greeting> retreived = helloService.getGreeting(request.getId());
		
		Assert.assertEquals(request.getMessage(), retreived.get().getMessage());
		
	}
	
	/*
	 * Delete greeting tests
	 */
	
	@Test(expected = EntityValidationException.class)
	public void deleteGreetingWithEmptyIDThrowsException() {
		helloService.deleteGreeting("");
	}

	@Test(expected = EntityValidationException.class)
	public void deleteGreetingWithNullIDThrowsException() {
		helloService.deleteGreeting(null);
	}
	
	@Test(expected = EntityValidationException.class)
	public void deleteGreetingWithInvalidIDThrowsException() {
		
		helloService.deleteGreeting("invalid");
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void deleteGreetingWithAbsenteeIDThrowsException() {
		helloService.deleteGreeting(UUID.randomUUID().toString());
	}
	
	@Test
	public void deleteExistingGreetingOKWhenValidRequest() {
		final String HELLO_ARTHUR = "Hello Arthur";
		Greeting request = new Greeting(HELLO_ARTHUR);

		Greeting created = helloService.createGreeting(request);
		
		helloService.deleteGreeting(created.getId());
		
		Optional<Greeting> retreived = helloService.getGreeting(created.getId());
		Assert.assertEquals(retreived.isPresent(),false);
		
	}
}
