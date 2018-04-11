package com.intuit.marketplace.data.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Project Entity which can exist independently
 *
 * @author Bhargav
 * @since 04/04/2018
 */
@Entity(name = "MKT_PROJECT")
@Table(name = "MKT_PROJECT")
public class MktProject extends MktDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @Column(name = "MAX_BUDGET", precision = 38, scale = 12, nullable = false)
    private BigDecimal maximumBudget;

    @Column(name = "LAST_DATE_FOR_BID", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastDayForBids;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private MktActor seller;

    public MktActor getSeller() {
        return seller;
    }

    public void setSeller(MktActor seller) {
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMaximumBudget() {
        return maximumBudget;
    }

    public void setMaximumBudget(BigDecimal maximumBudget) {
        this.maximumBudget = maximumBudget;
    }

    public DateTime getLastDayForBids() {
        return lastDayForBids;
    }

    public void setLastDayForBids(DateTime lastDayForBids) {
        this.lastDayForBids = lastDayForBids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MktProject that = (MktProject) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
