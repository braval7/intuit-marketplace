package com.intuit.marketplace.service.exception;

import ch.qos.logback.core.status.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception Mapper for errors
 *
 * @author Bhargav
 * @since 04/11/2018
 */
@Provider
@Singleton
public class MktExceptionMapper implements ExceptionMapper<MktRuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktExceptionMapper.class);

    @Override
    public Response toResponse(MktRuntimeException e) {
        LOGGER.error("MktExceptionMapper - " + e.getMessage(), e);
        String message = e.getMessage();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
    }

}
