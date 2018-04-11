package com.intuit.marketplace.service.impl;

import com.intuit.marketplace.api.rest.v1.model.MktBaseResponse;
import com.intuit.marketplace.api.rest.v1.model.MktCreateActorModel;
import com.intuit.marketplace.api.rest.v1.model.MktCreateProjectModel;
import com.intuit.marketplace.api.rest.v1.response.MktActorResponse;
import com.intuit.marketplace.data.domain.MktActor;
import com.intuit.marketplace.data.domain.MktProject;
import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.data.repository.MktActorRepository;
import com.intuit.marketplace.service.MktActorService;
import com.intuit.marketplace.service.exception.MktRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Concrete implementation of MktProjectService
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Service
public class MktActorServiceImpl implements MktActorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktActorServiceImpl.class);

    @Inject
    private MktActorRepository mktActorRepository;

    @Override
    public MktBaseResponse createActor(MktCreateActorModel model) {

        LOGGER.info("executing createActor");
        // do data validations
        performValidations(model);

        // create MktActor object
        MktActor actor = new MktActor();
        actor.setFname(model.getFirstName());
        actor.setLname(model.getLastName());
        actor.setActorType(MktActorType.valueOf(model.getActorType()));
        actor.setEmail(model.getEmail());

        // save the object to database
        actor = mktActorRepository.save(actor);

        LOGGER.info("Actor object saved to database");

        MktBaseResponse response = new MktBaseResponse();
        response.setRequestGuid(model.getRequestGuid());
        response.setId(actor.getId());

        return response;
    }

    private void performValidations(MktCreateActorModel model) {
        // validate if input is null
        if (model == null) {
            throw new MktRuntimeException("Provided model is null, Actor creation failed");
        }

        // check for required fields
        if (model.getEmail() == null) {
            throw new MktRuntimeException("Actor email address can't be null, Actor creation failed");
        }

        if (model.getFirstName() == null) {
            throw new MktRuntimeException("Actor first name can't be null, Actor creation failed");
        }

        if (model.getLastName() == null) {
            throw new MktRuntimeException("Actor last name can't be null, Actor creation failed");
        }

        if (model.getActorType() == null) {
            throw new MktRuntimeException("Actor Type can't be null, Actor creation failed");
        }
    }

    @Override
    @Transactional
    public MktActorResponse getActor(Long actorId) {
        LOGGER.info("executing getActor");

        // check if actor exists in the database, if not then throw error
        Optional<MktActor> actor = mktActorRepository.findById(actorId);

        if (!actor.isPresent()) {
            throw new MktRuntimeException("Actor doesn't exists");
        }

        // construct response
        MktActorResponse response = new MktActorResponse();
        response.setId(actor.get().getId());
        response.setEmail(actor.get().getEmail());
        response.setFirstName(actor.get().getFname());
        response.setLastName(actor.get().getLname());
        response.setActorType(actor.get().getActorType().name());

        // return response
        return response;
    }
}
