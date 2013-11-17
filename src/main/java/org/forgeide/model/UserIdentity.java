package org.forgeide.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.forgeide.security.model.User;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

/**
 * Entity that holds user details
 *
 * @author Shane Bryzak
 */
@IdentityManaged(User.class)
@Entity
public class UserIdentity {
    @Id @OwnerReference
    private IdentityType identityType;

    @AttributeValue
    private String email;

    @AttributeValue
    private String firstName;

    @AttributeValue
    private String lastName;

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
