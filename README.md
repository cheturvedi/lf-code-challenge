# Labforward Code Challenge for Backend Engineer Candidate

This is a simple Hello World API for recruiting purposes. You, as a candidate, should work on the challenge on your own account. Please clone the repo to your account and create a PR with your solution. 

## Introduction

You can run the application by typing

	./gradlew bootRun

This will start up a Spring Boot application with Tomcat server running on 8080.

Show all other possible tasks:

	./gradlew tasks
	
## Your Task	

You need to add a new endpoint to the API to allow users to *update the greetings they created*. 

## Acceptance Criteria

This task is purposefully open-ended. You are free to come up with your own implementation based on your assumptions. You are also welcome to improve the existing code by refactoring, cleaning up, etc. where necessary. Hint: there is a missing core piece in the application :) 

Extra points for describing a user interface which utilizes the API with the new endpoint. This can be a text document, simple mock-ups, or even an interactive HTML proof-of-concept. Be creative and show us how you approach UI problems.

We understand that not everyone has the same amount of "extra" time. It is also up to you to determine the amount of time you spend on the exercise. So that the reviewer understands how you are defining the scope of work, please clearly indicate your own “Definition of Done” for the task in a README file along with any other pertinent information.

Regardless of how far you take the solution towards completion, please assume you are writing production code. Your solution should clearly communicate your development style, abilities, and approach to problem solving. 

Let us know if you have any questions, and we look forward to seeing your approach.

Good Luck!


# Solution submitted

- Completed CRUD (Create, Read, Update , Delete) functionality of Greetings with REST API
- Added tests for Controller & Service layers for each of CRUD functionality
- Added a  HTML proof-of-concept which uses jquery to communicate with spring boot server
   UI is placed at index.html , as a result UI loads at localhost:8080/

## API Design 

- Create : POST request with messgae as json data in request body. chooses a RANDOM id for the greeting
			returns 200 with created  message in the response body
- Read : GET request with ID as URL parameter 
- Update : a PUT request to update existing resource, creates the resource if id is missing. 
            update is designed in accordance of https://tools.ietf.org/html/rfc7231#section-4.3.4
			returns 200 with created / updated message in the response body
- Delete : DELETE request with ID as URL parameter
			returns 404 if ID is missing , 200 with no content on success
			
returns 422 with possible errors for bad requests

## UI Design

- radio button to choose between Create ,Get, Update & Delete functionality. 
- 1 button 'Go' to fire off the query
- 2 text boxes : Id , Message
- Result text area which shows success if there were no errors 
	or displays as the error message sent by the server
-  Based on the seleted mode , an appropriate REST API call is made using jQuery 

