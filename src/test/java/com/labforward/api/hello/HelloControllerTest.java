package com.labforward.api.hello;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.labforward.api.common.MVCIntegrationTest;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest extends MVCIntegrationTest {

	private static final String HELLO_LUKE = "Hello Luke";

	@Test
	public void getHelloIsOKAndReturnsValidJSON() throws Exception {
		mockMvc.perform(get("/hello"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.id", is(HelloWorldService.DEFAULT_ID)))
		       .andExpect(jsonPath("$.message", is(HelloWorldService.DEFAULT_MESSAGE)));
	}

	@Test
	public void returnsBadRequestWhenMessageMissing() throws Exception {
		String body = "{}";
		mockMvc.perform(post("/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void returnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
		String body = "{ \"tacos\":\"value\" }}";
		mockMvc.perform(post("/hello").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.validationErrors", hasSize(1)))
			    .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void returnsBadRequestWhenMessageEmptyString() throws Exception {
		Greeting emptyMessage = new Greeting("");
		final String body = getGreetingBody(emptyMessage);

		mockMvc.perform(post("/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void createOKWhenRequiredGreetingProvided() throws Exception {
		Greeting hello = new Greeting(HELLO_LUKE);
		final String body = getGreetingBody(hello);

		mockMvc.perform(post("/hello").contentType(MediaType.APPLICATION_JSON)
		                              .content(body))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.message", is(hello.getMessage())));
	}

	private String getGreetingBody(Greeting greeting) throws JSONException {
		JSONObject json = new JSONObject().put("message", greeting.getMessage());

		if (greeting.getId() != null) {
			json.put("id", greeting.getId());
		}

		return json.toString();
	}
	
	/*
	 * Update controller inputs : greeting id, greeting body
	 * id -> invalid / missing / valid
	 * body -> invalid / missing /valid
	 * id in req body & id in URL don't match 
	 * 
	 */

	@Test
	public void updateOKonValidRequest() throws Exception {
		String id= UUID.randomUUID().toString();
		Greeting updateMsg = new Greeting("Vader");
		updateMsg.setId(id);
		
		String body = getGreetingBody(updateMsg);

		mockMvc.perform(put("/hello/"+id).contentType(MediaType.APPLICATION_JSON)
		                              .content(body))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.message", is(updateMsg.getMessage())))
		       .andExpect(jsonPath("$.id", is(updateMsg.getId())));
		
		updateMsg.setMessage("Annakin");
		body = getGreetingBody(updateMsg);
		
		mockMvc.perform(put("/hello/" + id).contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is(updateMsg.getMessage())))
				.andExpect(jsonPath("$.id", is(updateMsg.getId())));
		
	}
	
	@Test
	public void UpdateReturnsBadRequestWhenMessageMissing() throws Exception {
		String body = "{}";
		String id= UUID.randomUUID().toString();
		mockMvc.perform(put("/hello/"+id).content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("id")));
	}

	@Test
	public void UpdateReturnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
		String body = "{ \"tacos\":\"value\" }}";
		mockMvc.perform(put("/hello/invalid").content(body).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.validationErrors", hasSize(1)))
			    .andExpect(jsonPath("$.validationErrors[*].field", contains("id")));
	}
	
	@Test
	public void UpdateReturnsBadRequestWhenMissMatchingIdProvided() throws Exception {
		String id= UUID.randomUUID().toString();
		Greeting updateMsg = new Greeting("Vader");
		updateMsg.setId(id);
		
		String body = getGreetingBody(updateMsg);

		mockMvc.perform(put("/hello/"+id).contentType(MediaType.APPLICATION_JSON)
		                              .content(body))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.message", is(updateMsg.getMessage())))
		       .andExpect(jsonPath("$.id", is(updateMsg.getId())));
		
		updateMsg.setMessage("Annakin");
		updateMsg.setId("invalid");
		body = getGreetingBody(updateMsg);
		
		mockMvc.perform(put("/hello/" + id)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.validationErrors", hasSize(1)))
				.andExpect(jsonPath("$.validationErrors[*].field", contains("id")));

	}

	
	/*
	 * Delete
	 */  
	
	@Test
	public void deleteOKonValidRequest() throws Exception {
		String id= UUID.randomUUID().toString();
		Greeting arthur = new Greeting("Arthur");
		arthur.setId(id);
		final String body = getGreetingBody(arthur);

		mockMvc.perform(post("/hello").contentType(MediaType.APPLICATION_JSON)
		                              .content(body))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.message", is(arthur.getMessage())))
		       .andExpect(jsonPath("$.id", is(arthur.getId())));
		
		mockMvc.perform(delete("/hello/"+id))
	       .andExpect(status().isOk());
	}
	
	@Test
	public void deleteReturnsResourceNotFoundOnInavlidID() throws Exception {
		String id= UUID.randomUUID().toString();
		
		mockMvc.perform(delete("/hello/"+id))
	       .andExpect(status().isNotFound());
	}
}
