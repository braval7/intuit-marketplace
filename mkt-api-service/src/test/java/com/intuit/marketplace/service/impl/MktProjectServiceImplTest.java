package com.intuit.marketplace.service.impl;

import com.intuit.marketplace.api.rest.v1.model.MktAcceptProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectBidModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.data.domain.MktActor;
import com.intuit.marketplace.data.domain.MktProject;
import com.intuit.marketplace.data.domain.MktProjectBids;
import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.data.repository.MktActorRepository;
import com.intuit.marketplace.data.repository.MktProjectBidsRepository;
import com.intuit.marketplace.data.repository.MktProjectRepository;
import com.intuit.marketplace.service.MktActorService;
import com.intuit.marketplace.service.exception.MktRuntimeException;
import com.intuit.marketplace.service.scheduler.MktAcceptProjectBidScheduler;
import com.intuit.marketplace.service.util.MktProjectHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for MktProjectServiceImpl
 *
 * @author Bhargav
 * @since 04/11/2018
 */

public class MktProjectServiceImplTest {

    @InjectMocks
    private MktProjectServiceImpl mktProjectService;

    @Mock
    private MktProjectRepository mktProjectRepository;

    @Mock
    private MktProjectBidsRepository mktProjectBidsRepository;

    @Mock
    private MktActorService mktActorService;

    @Mock
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Mock
    private MktActorRepository mktActorRepository;

    @Mock
    private MktProjectHelper mktProjectHelper;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mktProjectHelper).checkForIdempotency(any(UUID.class));
    }

    @Test
    public void createProject() {
        // create mock data
        MktCreateProjectModel model = createProjectModel();

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.SELLER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        when(mktProjectRepository.save(any(MktProject.class))).thenReturn(project);

        // call service
        MktBaseResponse response = mktProjectService.createProject(model);

        // validate response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).save(any(MktProject.class));
        verify(threadPoolTaskScheduler, times(1)).schedule(any(MktAcceptProjectBidScheduler.class), any(Date.class));
        assertEquals("wrong request guid returned from response", model.getRequestGuid(), response.getRequestGuid());
        assertEquals("wrong id returned from response", project.getId(), response.getId());
    }

    @Test
    public void createProjectWhenSellerNotExistsShouldFail() {
        // create mock data
        MktCreateProjectModel model = createProjectModel();

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.SELLER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(null);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        // call service
        try {
            mktProjectService.createProject(model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Seller doesn't exists, project creation failed", e.getMessage());
        }

        // validate response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(0)).save(any(MktProject.class));
        verify(threadPoolTaskScheduler, times(0)).schedule(any(MktAcceptProjectBidScheduler.class), any(Date.class));
    }

    @Test
    public void createProjectWhenShouldFailWhenLastDayForBidsInThePast() {
        // create mock data
        MktCreateProjectModel model = createProjectModel();
        model.setLastDayForBids(DateTime.now(DateTimeZone.UTC).minusDays(1));

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.SELLER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        // call service
        try {
            mktProjectService.createProject(model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Project's last day for bids can't be in the past, project creation failed", e.getMessage());
        }

        // validate response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(0)).save(any(MktProject.class));
        verify(threadPoolTaskScheduler, times(0)).schedule(any(MktAcceptProjectBidScheduler.class), any(Date.class));
    }

    @Test
    public void createProjectWithBuyerAsActorShouldFail() {
        // create mock data
        MktCreateProjectModel model = createProjectModel();

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        // call service
        try {
            mktProjectService.createProject(model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Given actor is not a project seller, project creation failed", e.getMessage());
        }

        // validate response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(0)).save(any(MktProject.class));
        verify(threadPoolTaskScheduler, times(0)).schedule(any(MktAcceptProjectBidScheduler.class), any(Date.class));
    }

    @Test
    public void postProjectBid() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        // no bid exists for this combination
        when(mktProjectBidsRepository.findProjectBidsByProjectAndActor(2L, 1L)).thenReturn(null);

        MktProjectBids projectBids = Mockito.mock(MktProjectBids.class);
        when(projectBids.getId()).thenReturn(1L);

        when(mktProjectBidsRepository.save(any(MktProjectBids.class))).thenReturn(projectBids);
        MktCreateProjectBidModel model = createProjectBidModel();

        // call service
        MktBaseResponse response = mktProjectService.postProjectBid(project.getId(), actor.getId(), model);

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(1)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(1)).save(any(MktProjectBids.class));
    }

    @Test
    public void postProjectBidShouldFailWhenProjectDoesntExists() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(null);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktCreateProjectBidModel model = createProjectBidModel();

        // call service
        try {
            mktProjectService.postProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "project doesn't exists, can't post bid", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(0)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(0)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void postProjectBidShouldFailWhenBuyerDoesntExists() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(null);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktCreateProjectBidModel model = createProjectBidModel();

        // call service
        try {
            mktProjectService.postProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "buyer doesn't exists, can't post bid", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(0)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void postProjectBidShouldFailWhenActorIsNotBuyer() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.SELLER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktCreateProjectBidModel model = createProjectBidModel();

        // call service
        try {
            mktProjectService.postProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Provided actor is not Buyer, can't post bid", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(0)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void postProjectBidShouldFailWhenBidPriceIsHigherThanMaximum() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktCreateProjectBidModel model = createProjectBidModel();
        model.setBidPrice(new BigDecimal("11"));

        // call service
        try {
            mktProjectService.postProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Bid price is higher than maximum budget for a project, can't post bid", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(0)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void postProjectBidShouldFailWhenBidAlreadyPlacedBySameBuyer() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktProjectBids mockBid = Mockito.mock(MktProjectBids.class);
        when(mktProjectBidsRepository.findProjectBidsByProjectAndActor(2L, 1L)).thenReturn(mockBid);

        MktCreateProjectBidModel model = createProjectBidModel();

        // call service
        try {
            mktProjectService.postProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Actor 1 has already placed a bid on this project", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(1)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void acceptProjectBid() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktProjectBids projectBids = Mockito.mock(MktProjectBids.class);
        when(projectBids.getId()).thenReturn(1L);

        MktAcceptProjectBidModel model = new MktAcceptProjectBidModel();
        model.setRequestGuid(UUID.randomUUID());

        when(mktProjectBidsRepository.save(any(MktProjectBids.class))).thenReturn(projectBids);
        when(mktProjectBidsRepository.findProjectBidsByProjectAndActor(2L, 1L)).thenReturn(projectBids);

        // call service
        MktBaseResponse response = mktProjectService.acceptProjectBid(project.getId(), actor.getId(), model);

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(1)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(1)).save(any(MktProjectBids.class));
    }

    @Test
    public void acceptProjectBidShouldFailWhenProjectDoesntExists() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(null);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktAcceptProjectBidModel model = new MktAcceptProjectBidModel();
        model.setRequestGuid(UUID.randomUUID());

        // call service
        try {
            mktProjectService.acceptProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "project doesn't exists, can't accept bid", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(0)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(0)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void acceptProjectBidShouldFailWhenBuyerDoesntExists() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(null);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        MktAcceptProjectBidModel model = new MktAcceptProjectBidModel();
        model.setRequestGuid(UUID.randomUUID());

        // call service
        try {
            mktProjectService.acceptProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "buyer doesn't exists, can't accept bid", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(0)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }

    @Test
    public void acceptProjectBidShouldFailWhenBuyerHaveNotPlacedBid() {

        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        MktProject project = Mockito.mock(MktProject.class);
        when(project.getId()).thenReturn(2L);
        when(project.getMaximumBudget()).thenReturn(BigDecimal.TEN);
        when(project.getLastDayForBids()).thenReturn(DateTime.now());
        Optional<MktProject> optionalProject = Optional.ofNullable(project);
        when(mktProjectRepository.findById(2L)).thenReturn(optionalProject);

        when(mktProjectBidsRepository.findProjectBidsByProjectAndActor(2L, 1L)).thenReturn(null);

        MktAcceptProjectBidModel model = new MktAcceptProjectBidModel();
        model.setRequestGuid(UUID.randomUUID());

        // call service
        try {
            mktProjectService.acceptProjectBid(project.getId(), actor.getId(), model);
            fail("This code should not have been executed");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Buyer 1 has not placed a bid on this project 2", e.getMessage());
        }

        // verify response
        verify(mktActorRepository, times(1)).findById(1L);
        verify(mktProjectRepository, times(1)).findById(2L);
        verify(mktProjectBidsRepository, times(1)).findProjectBidsByProjectAndActor(2L, 1L);
        verify(mktProjectBidsRepository, times(0)).save(any(MktProjectBids.class));
    }


    private MktCreateProjectBidModel createProjectBidModel() {
        MktCreateProjectBidModel model = new MktCreateProjectBidModel();
        model.setRequestGuid(UUID.randomUUID());
        model.setBidPrice(BigDecimal.ONE);

        return model;
    }

    private MktCreateProjectModel createProjectModel() {
        MktCreateProjectModel model = new MktCreateProjectModel();
        model.setRequestGuid(UUID.randomUUID());
        model.setDescription("Description");
        model.setLastDayForBids(DateTime.now(DateTimeZone.UTC).plusHours(1));
        model.setMaximumBudget(BigDecimal.TEN);
        model.setSellerId(1L);

        return model;
    }

    MktCreateActorModel createMockModel(MktActorType actorType) {
        MktCreateActorModel model = new MktCreateActorModel();
        model.setRequestGuid(UUID.randomUUID());
        model.setEmail("TestingUser@user.com");
        model.setFirstName("FirstName");
        model.setLastName("LastName");
        model.setActorType(actorType.name());

        return model;
    }


}
