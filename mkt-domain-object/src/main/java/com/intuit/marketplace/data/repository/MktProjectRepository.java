package com.intuit.marketplace.data.repository;

import com.intuit.marketplace.data.domain.MktProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for MktProject
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Repository
public interface MktProjectRepository extends JpaRepository<MktProject, Long> {
}
