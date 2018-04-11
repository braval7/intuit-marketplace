package com.intuit.marketplace.data.repository;

import com.intuit.marketplace.data.domain.MktProjectBids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for MktProjectBids
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Repository
public interface MktProjectBidsRepository extends JpaRepository<MktProjectBids, Long> {

    /**
     * Find all Project Bids for given project order by Bid Price (lowest first)
     *
     * @param projectId
     * @return
     */
    @Query(value = "select mktProjectBids from " +
            "com.intuit.marketplace.data.domain.MktProjectBids mktProjectBids " +
            "where mktProjectBids.project.id = :projectId order by bid_price")
    List<MktProjectBids> findProjectBidsOrderByBidPrice(@Param("projectId") Long projectId);


    @Query(value = "select mktProjectBids from " +
            "com.intuit.marketplace.data.domain.MktProjectBids mktProjectBids " +
            "where mktProjectBids.project.id = :projectId " +
            "and mktProjectBids.actor.id = :actorId")
    MktProjectBids findProjectBidsByProjectAndActor(@Param("projectId") Long projectId,
                                                    @Param("actorId") Long actorId);
}
