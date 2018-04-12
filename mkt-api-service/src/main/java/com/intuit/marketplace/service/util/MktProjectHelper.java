package com.intuit.marketplace.service.util;

import com.intuit.marketplace.data.domain.MktRequest;
import com.intuit.marketplace.data.repository.MktRequestRepository;
import com.intuit.marketplace.service.exception.MktRuntimeException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Utility class with helper methods
 *
 * @author Bhargav
 * @since 04/12/2018
 */
public class MktProjectHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MktProjectHelper.class);


    @Inject
    private MktRequestRepository mktRequestRepository;

    /**
     * check if this request is idempotent or not
     * @param requestGuid
     */
    public void checkForIdempotency(UUID requestGuid) {

        LOGGER.info("Checking idempotency for requestGuid {}", requestGuid);

        MktRequest request = new MktRequest();
        request.setRequestGuid(requestGuid);
        try {
            request = mktRequestRepository.saveAndFlush(request);
        } catch (Exception e) {
            throw new MktRuntimeException("Idempotent request found, transaction was not carried out. RequestGuid = " + requestGuid);
        }
    }
}
