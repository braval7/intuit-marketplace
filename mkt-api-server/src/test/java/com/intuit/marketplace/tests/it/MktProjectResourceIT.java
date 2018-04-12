package com.intuit.marketplace.tests.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.marketplace.api.rest.v1.model.MktAcceptProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;
import com.intuit.marketplace.api.rest.v1.response.MktProjectResponse;
import com.intuit.marketplace.data.domain.MktActor;
import com.intuit.marketplace.data.domain.MktProject;
import com.intuit.marketplace.data.domain.MktProjectBids;
import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.data.enums.MktProjectBidStatus;
import com.intuit.marketplace.data.repository.MktActorRepository;
import com.intuit.marketplace.data.repository.MktProjectBidsRepository;
import com.intuit.marketplace.data.repository.MktProjectRepository;
import com.intuit.marketplace.tests.config.TestConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for Marketplace project
 *
 * @author Bhargav
 * @since 04/10/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MktProjectResourceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Inject
    private MktActorRepository mktActorRepository;

    @Inject
    private MktProjectRepository mktProjectRepository;

    @Inject
    private MktProjectBidsRepository mktProjectBidsRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateActor() throws Exception {

        // create Request Model
        MktCreateActorModel actorModel = new MktCreateActorModel();
        actorModel.setEmail("seller1@testing.com");
        actorModel.setActorType(MktActorType.SELLER.name());
        actorModel.setFirstName("Testing");
        actorModel.setLastName("Seller");
        actorModel.setRequestGuid(UUID.randomUUID());

        // call the API
        ResponseEntity<MktBaseResponse> responseEntity = restTemplate.
                postForEntity("/api/v1/actors", actorModel, MktBaseResponse.class);
        MktBaseResponse createActorResponse = responseEntity.getBody();

        // validate the response through getter
        ResponseEntity<MktActorResponse> response =
                restTemplate.getForEntity(
                        "/api/v1/actors/{actorId}", MktActorResponse.class, createActorResponse.getId());

        // also call DB to find the record
        Optional<MktActor> actor = mktActorRepository.findById(createActorResponse.getId());
        assertEquals("Actor was not created in the DB", createActorResponse.getId(), actor.get().getId());
    }

    @Test
    public void endToEndScenario() {
        /**
         * This Test creates following scenario
         * 1. Create an actor with type seller
         * 2. Create a project with Seller Actor ID
         * 3. Create another actor with type Buyer
         * 4. Post a bid
         * 5. Accept a bid
         */

        // create Seller Model
        MktCreateActorModel actorModel = new MktCreateActorModel();
        actorModel.setEmail("seller@testing.com");
        actorModel.setActorType(MktActorType.SELLER.name());
        actorModel.setFirstName("Testing");
        actorModel.setLastName("Seller");
        actorModel.setRequestGuid(UUID.randomUUID());

        // call the API
        ResponseEntity<MktBaseResponse> responseEntity = restTemplate.
                postForEntity("/api/v1/actors", actorModel, MktBaseResponse.class);
        MktBaseResponse seller = responseEntity.getBody();

        // create a project
        MktCreateProjectModel createProjectModel = new MktCreateProjectModel();
        createProjectModel.setRequestGuid(UUID.randomUUID());
        createProjectModel.setSellerId(seller.getId());
        createProjectModel.setMaximumBudget(BigDecimal.TEN);
        createProjectModel.setDescription("Description");
        createProjectModel.setLastDayForBids(DateTime.now(DateTimeZone.UTC).plusHours(1));

        // call the API
        responseEntity = restTemplate.
                postForEntity("/api/v1/projects", createProjectModel, MktBaseResponse.class);
        MktBaseResponse project = responseEntity.getBody();

        // create buyer Model
        actorModel = new MktCreateActorModel();
        actorModel.setEmail("buyer@testing.com");
        actorModel.setActorType(MktActorType.BUYER.name());
        actorModel.setFirstName("Testing");
        actorModel.setLastName("Buyer");
        actorModel.setRequestGuid(UUID.randomUUID());

        // call the API
        responseEntity = restTemplate.
                postForEntity("/api/v1/actors", actorModel, MktBaseResponse.class);
        MktBaseResponse buyer = responseEntity.getBody();

        // post a bid

        MktCreateProjectBidModel projectBidModel = new MktCreateProjectBidModel();
        projectBidModel.setRequestGuid(UUID.randomUUID());
        projectBidModel.setBidPrice(new BigDecimal("5"));
        // call the API
        responseEntity = restTemplate.
                postForEntity("/api/v1/projects/{projectId}/post-bid/{buyerId}", projectBidModel, MktBaseResponse.class,
                        project.getId(), buyer.getId());
        MktBaseResponse postBid = responseEntity.getBody();

        // validate that data exists in the DB
        // check for project
        Optional<MktProject> project1 = mktProjectRepository.findById(project.getId());
        assertNotNull("Project was not created successfully", project1.get());

        // check for bid on project
        Optional<MktProjectBids> projectBids = mktProjectBidsRepository.findById(postBid.getId());
        assertNotNull("Bid on a project was not created successfully", projectBids.get());
        assertEquals("Bid status should be PENDING", projectBids.get().getBidStatus(), MktProjectBidStatus.PENDING);


        MktAcceptProjectBidModel model = new MktAcceptProjectBidModel();
        model.setRequestGuid(UUID.randomUUID());
        // accept the bid
        responseEntity = restTemplate.
                postForEntity("/api/v1/projects/{projectId}/accept-bid/{buyerId}", model, MktBaseResponse.class,
                        project.getId(), buyer.getId());

        // pull the bid record again from DB
        projectBids = mktProjectBidsRepository.findById(postBid.getId());

        // bid status should chagne to accepted
        assertEquals("Bid status should be ACCEPTED", projectBids.get().getBidStatus(), MktProjectBidStatus.ACCEPTED);
    }




}
