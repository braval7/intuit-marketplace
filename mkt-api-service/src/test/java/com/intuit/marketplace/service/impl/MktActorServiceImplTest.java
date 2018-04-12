package com.intuit.marketplace.service.impl;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;
import com.intuit.marketplace.data.domain.MktActor;
import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.data.repository.MktActorRepository;
import com.intuit.marketplace.service.exception.MktRuntimeException;
import com.intuit.marketplace.service.util.MktProjectHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for MktActorServiceImpl
 *
 * @author Bhargav
 * @since 04/11/2018
 */
public class MktActorServiceImplTest {

    @InjectMocks
    private MktActorServiceImpl mktActorService;

    @Mock
    private MktActorRepository mktActorRepository;

    @Mock
    private MktProjectHelper mktProjectHelper;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createActor() {
        // create mock data
        MktCreateActorModel mockModel = createMockModel(MktActorType.SELLER);
        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(mktActorRepository.save(any(MktActor.class))).thenReturn(actor);
        doNothing().when(mktProjectHelper).checkForIdempotency(mockModel.getRequestGuid());

        // call service
        MktBaseResponse response = mktActorService.createActor(mockModel);

        // validate results
        verify(mktActorRepository, times(1)).save(any(MktActor.class));
        assertEquals("Wrong object returned in response", mockModel.getRequestGuid(), response.getRequestGuid());
        assertEquals("Wrong object returned in response", actor.getId(), response.getId());
    }

    @Test
    public void performValidations() {
        // create mock data
        MktCreateActorModel mockModel = createMockModel(MktActorType.SELLER);

        // call the service
        mktActorService.performValidations(mockModel);
    }

    @Test
    public void performValidationsFailureTests() {

        // call the service when model is null
        try {
            mktActorService.performValidations(null);
            fail("This code should not get called");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when model is null",
                    "Provided model is null, Actor creation failed", e.getMessage());
        }

        MktCreateActorModel mockModel = createMockModel(MktActorType.SELLER);

        // call the service when email is null
        try {
            mockModel.setEmail("");
            mktActorService.performValidations(mockModel);
            fail("This code should not get called");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when email is null",
                    "Actor email address can't be null, Actor creation failed", e.getMessage());
        }

        // call the service when first name is null
        try {
            mockModel = createMockModel(MktActorType.SELLER);
            mockModel.setFirstName("");
            mktActorService.performValidations(mockModel);
            fail("This code should not get called");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when email is null",
                    "Actor first name can't be null, Actor creation failed", e.getMessage());
        }

        // call the service when last name is null
        try {
            mockModel = createMockModel(MktActorType.SELLER);
            mockModel.setLastName("");
            mktActorService.performValidations(mockModel);
            fail("This code should not get called");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when email is null",
                    "Actor last name can't be null, Actor creation failed", e.getMessage());
        }

        // call the service when actor Type is null
        try {
            mockModel = createMockModel(MktActorType.SELLER);
            mockModel.setActorType("");
            mktActorService.performValidations(mockModel);
            fail("This code should not get called");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when email is null",
                    "Actor Type can't be null, Actor creation failed", e.getMessage());
        }
    }

    @Test
    public void getActorThrowsExceptionWhenActorNotFound() {
        // mock data
        Optional<MktActor> optionalActor = Optional.ofNullable(null);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        // call service
        try {
            mktActorService.getActor(1L);
            fail("This code should not get called");
        } catch (Exception e) {
            assertEquals("Wrong exception thrown", e.getClass(), MktRuntimeException.class);
            assertEquals("Wrong message when actor not found",
                    "Actor doesn't exists", e.getMessage());
        }

        // validate result
        verify(mktActorRepository, times(1)).findById(1L);
    }

    @Test
    public void getActor() {
        // mock data
        MktActor actor = Mockito.mock(MktActor.class);
        when(actor.getId()).thenReturn(1L);
        when(actor.getActorType()).thenReturn(MktActorType.BUYER);
        when(actor.getEmail()).thenReturn("Email@emailTest.com");
        Optional<MktActor> optionalActor = Optional.ofNullable(actor);
        when(mktActorRepository.findById(1L)).thenReturn(optionalActor);

        // call the service
        MktActorResponse response = mktActorService.getActor(1L);

        // validate result
        verify(mktActorRepository, times(1)).findById(1L);
        assertEquals("Wrong id returned from response", actor.getId(), response.getId());
        assertEquals("Wrong actor type returned from response", MktActorType.BUYER, MktActorType.valueOf(response.getActorType()));
        assertEquals("Wrong email returned from response", "Email@emailTest.com", response.getEmail());

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
