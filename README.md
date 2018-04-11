# intuit-marketplace
Intuit marketplace assessment

1. Compile the project from parent directory using "mvn -U clean install"
2. After that run the server by going into mkt-api-server and running the command "mvn spring-boot:run"
3. Go to http://localhost:8080/mkt/api/v1/projects in browser to get dummy response (to make sure server is up)

Configuration of Swagger is work in progress. 


Step 1: Create Actors using below url and payloads,

POST http://localhost:8080/mkt/api/v1/actors

{
  "requestGuid": "4b2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "firstName": "Testing",
  "lastName": "Seller",
  "email": "seller@testing.com",
  "actorType": "SELLER"
}

{
  "requestGuid": "cb2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "firstName": "Testing",
  "lastName": "Buyer",
  "email": "buyer@testing.com",
  "actorType": "BUYER"
}

{
  "requestGuid": "de2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "firstName": "Testing1",
  "lastName": "Buyer1",
  "email": "another-buyer@testing.com",
  "actorType": "BUYER"
}


Step 2: Validate that actors are created properly,

GET http://localhost:8080/mkt/api/v1/actors/{actorId}


Step 3: Create Project using below URL and payload,

POST http://localhost:8080/mkt/api/v1/projects
{
  "requestGuid": "cb2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "sellerId": "1",
  "maximumBudget": "10000.00",
  "lastDayForBids": "2018-04-10T17:39:00.000-07:00",
  "description": "Spring Boot Project"
}

Step 4: Validate that project got created using below URL,

GET http://localhost:8080/mkt/api/v1/projects/{projectId}

Step 5: Post a bid on project for actor of type buyer using below URL,

POST http://localhost:8080/mkt/api/v1/projects/{projectId}/post-bid/{buyerId}

{
  "requestGuid": "7b2ccaae-f746-4c8e-ac98-d38ba7dc377d",
  "bidPrice": "9000.00"
}


Step 6: Optionally, seller can go ahead and accept the bid manually (scheduler will do this internally)

POST http://localhost:8080/mkt/api/v1/projects/{projectId}/accept-bid/{buyerId}

