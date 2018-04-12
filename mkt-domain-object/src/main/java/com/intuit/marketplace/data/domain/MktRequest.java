package com.intuit.marketplace.data.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

/**
 * Request table to handle idempotency of the system.
 * REQUEST_GUID is unique for each operation.
 *
 * @author Bhargav
 * @since 04/12/2018
 */
@Entity(name = "MKT_REQUEST")
@Table(name = "MKT_REQUEST",
        uniqueConstraints = @UniqueConstraint(name = "MKT_REQUEST_UQ_1", columnNames = {"REQUEST_GUID"}))
public class MktRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "REQUEST_GUID", nullable = false, unique = true, columnDefinition = "varchar2(38)")
    @Type(type = "uuid-char")
    private UUID requestGuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(UUID requestGuid) {
        this.requestGuid = requestGuid;
    }
}
