package org.forgeide.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.IdentityClass;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

/**
 * 
 * @author Shane Bryzak
 *
 */
@IdentityManaged (org.picketlink.idm.model.IdentityType.class)
@Entity
public class IdentityType implements Serializable {
    private static final long serialVersionUID = 202010860818364683L;

    @ManyToOne
    @OwnerReference
    private Partition partition;

    @Id @Identifier
    private String id;

    @IdentityClass
    private String identityClass;

    @AttributeValue
    private Date createdDate;

    @AttributeValue
    private Date expirationDate;

    @AttributeValue
    private boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentityClass() {
        return identityClass;
    }

    public void setIdentityClass(String identityClass) {
        this.identityClass = identityClass;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
