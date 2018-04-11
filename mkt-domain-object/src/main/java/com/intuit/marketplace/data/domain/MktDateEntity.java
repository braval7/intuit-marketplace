package com.intuit.marketplace.data.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Date fields used by all entities
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class MktDateEntity implements Serializable {

    @Column(name = "CREATED_D")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDate;

    @Column(name = "MODIFIED_D")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDate;

    @Version
    private Long version = Long.valueOf(0L);

    public DateTime getCreatedDate() {
        return this.createdDate;
    }

    public DateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public Long getVersion() {
        return this.version;
    }

    @PreUpdate
    @PrePersist
    public void setAuditDates() {
        this.modifiedDate = (new DateTime()).withZone(DateTimeZone.UTC);
        if(this.createdDate == null) {
            this.createdDate = this.modifiedDate;
        }

        this.createdDate = this.createdDate.withZone(DateTimeZone.UTC);
    }
}
