package com.intuit.marketplace.data.repository;

import com.intuit.marketplace.data.domain.MktActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for MktActor
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Repository
public interface MktActorRepository extends JpaRepository<MktActor, Long> {

    /**
     * find an actor by email
     *
     * @param email
     * @return
     */
    MktActor findByEmail(String email);

}
