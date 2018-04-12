# Marketplace spring boot application
=================

About
-----
This project is a project created for intuit assessment. Technology used for this projects, 
- Maven 
  - Build tool which gives lot of flexibility in managing dependency
- Spring Boot 
  - Spring boot is a defacto standard in industry to create quick microservices applications
  - Spring data for crud operations
  - Spring boot scheduler to schedule tasks at given date/time (in this example, project's last date to accept bids)
- h2 
  - In memory database for this application
- Rest Easy
  - I chose resteasy over Spring MVC because resteasy is matured compared to Spring MVC. 
- Junit/Mockito
  - For unit testing mockito and junit

- I tried integrating Swagger 2 but since I am using RestEasy, Swagger 2 is not supported with SpringBoot.
- I'll continue adding this integration in more standard way of registering filter but that may take some time. 
- Support for idempotency is there and that is on the POST APIs with the use of **requestGuid** and table MKT_REQUEST


Project Structure
------------------
* This is multimodule spring-boot project with following major modules,
  * mkt-api-client - This project contains all the Rest end points, request and response models.
  * mkt-domain-object - This project contains all the JPA entity, Relationship between entities, and JPA Repositories
  * mkt-api-service - This project is implementation of APIs defined in **mkt-api-client** and contains business logic
  * mkt-api-server - This project is only for the SpringBoot application server


Try it out!
-----------
* **Do it yourself** and build and deploy it using your favourite IDE or command prompt
* Build the project from parent directory using 'mvn clean install' 
* Navigate to **mkt-api-server** and run the application  with `mvn spring-boot:run`



URLs and Payloads
-----------------

Check following URLs (running this example on your localhost):

1. [Create Actor](http://localhost:8080/mkt/api/v1/actors)
  - > http://localhost:8080/mkt/api/v1/actors (POST)
  - > {
  "requestGuid": "4b2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "firstName": "Testing",
  "lastName": "Seller",
  "email": "seller@testing.com",
  "actorType": "SELLER"
}
  - > {
  "requestGuid": "cb2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "firstName": "Testing",
  "lastName": "Buyer",
  "email": "buyer@testing.com",
  "actorType": "BUYER"
}
  - > {
  "requestGuid": "de2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "firstName": "Testing1",
  "lastName": "Buyer1",
  "email": "another-buyer@testing.com",
  "actorType": "BUYER"
}

2. [Validate actor](http://localhost:8080/mkt/api/v1/actors/{actorId})
  - > http://localhost:8080/mkt/api/v1/actors/{actorId} (GET)
  - Replace '{actorId}'' with Id you got from response of Step #1

3. [Create Actor](http://localhost:8080/mkt/api/v1/projects)
  - > http://localhost:8080/mkt/api/v1/projects (POST)
  - > {
  "requestGuid": "cb2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "sellerId": "1",
  "maximumBudget": "10000.00",
  "lastDayForBids": "2018-04-10T17:39:00.000-07:00",
  "description": "Spring Boot Project"
}

4. [Validate project](http://localhost:8080/mkt/api/v1/projects/{projectId})
  - > http://localhost:8080/mkt/api/v1/projects/{projectId} (GET)
  - > Replace '{projectId}'' with Id you got from response of Step #3

5. [Post a bid on project](http://localhost:8080/mkt/api/v1/projects/{projectId}/post-bid/{buyerId})
  - > http://localhost:8080/mkt/api/v1/projects/{projectId}/post-bid/{buyerId} (POST)
  - > {
  "requestGuid": "7b2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "bidPrice": "9000.00"
}
  - > Replace '{projectId}' and '{buyerId}' with values received from Step #3 and #1 respsectively

6. [Accept a bid on project](http://localhost:8080/mkt/api/v1/projects/{projectId}/accept-bid/{buyerId}) 
  - > http://localhost:8080/mkt/api/v1/projects/{projectId}/accept-bid/{buyerId} (POST)
  - > > {
  "requestGuid": "cb2ccaae-f746-4c8e-ac98-d38ba7dc377d" 
  }
  - > Replace '{projectId}' and '{buyerId}' with values received from Step #3 and #1 respsectively
  - *This step is optional, if not done the scheduler will ACCEPT lower bid automatically*
