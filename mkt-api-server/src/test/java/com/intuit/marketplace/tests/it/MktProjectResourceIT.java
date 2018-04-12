package com.intuit.marketplace.tests.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktProjectResponse;
import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.tests.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.util.UUID;

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
@SpringBootTest(classes = TestConfig.class)
public class MktProjectResourceIT {

    @Inject
    private TestRestTemplate restTemplate;

    @Inject
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateProject() throws Exception {

        // create Actor
        MktCreateActorModel seller = new MktCreateActorModel();
        seller.setEmail("seller@testing.com");
        seller.setActorType(MktActorType.SELLER.name());
        seller.setFirstName("Testing");
        seller.setLastName("Seller");
        seller.setRequestGuid(UUID.randomUUID());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String response = restTemplate.postForObject("mkt/api/v1/projects", entity, String.class);

        System.out.println("BhargavResult:::" + response);

//        final ResponseEntity entity = this.restTemplate.getForObject("mkt/api/v1/projects", MktProjectResponse.class, u);

        // call to create actor
//        mockMvc.perform(post("mkt/api/v1/actors").content(objectMapper.writeValueAsString(seller)))
//                .andDo(print())
//                .andExpect(status().isOk());

//        MktCreateProjectModel model = new MktCreateProjectModel();
        //model.set

    }


}
