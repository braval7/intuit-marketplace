package com.intuit.marketplace.data.domain;

import com.intuit.marketplace.data.enums.MktActorType;
import com.intuit.marketplace.data.enums.MktProjectBidStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

/**
 * Bids placed by actors on a project
 * Note one buyer can place only one bid for one project
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Entity(name = "MKT_PROJECT_BIDS")
@Table(name = "MKT_PROJECT_BIDS",
        uniqueConstraints = @UniqueConstraint(name = "MKT_PROJECT_BIDS_UQ_1", columnNames = {"PROJECT_ID", "BUYER_ID"}))
public class MktProjectBids extends MktDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private MktProject project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER_ID", nullable = false)
    private MktActor actor;

    @Column(name = "BID_PRICE", precision = 38, scale = 12, nullable = false)
    private BigDecimal bidPrice;

    @Column(name = "BID_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private MktProjectBidStatus bidStatus;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public MktProject getProject() {
        return project;
    }

    public void setProject(MktProject project) {
        this.project = project;
    }

    public MktActor getActor() {
        return actor;
    }

    public void setActor(MktActor actor) {
        this.actor = actor;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public MktProjectBidStatus getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(MktProjectBidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MktProjectBids that = (MktProjectBids) o;

        if (Id != null ? !Id.equals(that.Id) : that.Id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Id != null ? Id.hashCode() : 0;
    }
}
