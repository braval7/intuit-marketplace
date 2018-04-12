package com.intuit.marketplace.data.repository;

import com.intuit.marketplace.data.domain.MktRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Repository for MktRequest object
 *
 * @author Bhargav
 * @since 04/12/2018
 */
public interface MktRequestRepository extends JpaRepository<MktRequest, Long> {

    MktRequest findByRequestGuid(@Param("requestGuid") UUID requestGuid);
}
