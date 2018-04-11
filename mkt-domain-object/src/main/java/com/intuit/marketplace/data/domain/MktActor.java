package com.intuit.marketplace.data.domain;

import com.intuit.marketplace.data.enums.MktActorType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Actor entity, emails are going to be unique.
 * ASSUMPTION: Same actor can't be seller and buyer.
 *
 * @author Bhargav
 * @since 04/05/2018
 */
@Entity(name = "MKT_ACTOR")
@Table(name = "MKT_ACTOR", uniqueConstraints =
            @UniqueConstraint(name = "MKT_ACTOR_UQ_1", columnNames = {"EMAIL"}))
public class MktActor extends MktDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String fname;

    @Column(name = "LAST_NAME", nullable = false)
    private String lname;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private MktActorType actorType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MktActorType getActorType() {
        return actorType;
    }

    public void setActorType(MktActorType actorType) {
        this.actorType = actorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MktActor mktActor = (MktActor) o;

        if (id != null ? !id.equals(mktActor.id) : mktActor.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
