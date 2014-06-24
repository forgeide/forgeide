package org.forgeide.security.schema;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.RelationshipDescriptor;
import org.picketlink.idm.jpa.annotations.RelationshipMember;

/**
 * 
 * @author Shane Bryzak
 *
 */
@Entity
public class RelationshipIdentity implements Serializable
{
   private static final long serialVersionUID = -7939098158655476688L;

   @Id
   @GeneratedValue
   private Long identifier;

   @RelationshipDescriptor
   private String descriptor;

   @RelationshipMember
   @ManyToOne
   private IdentityType identityType;

   @OwnerReference
   @ManyToOne
   private RelationshipType owner;

   public Long getIdentifier() {
       return identifier;
   }

   public void setIdentifier(Long identifier) {
       this.identifier = identifier;
   }

   public String getDescriptor() {
       return descriptor;
   }

   public void setDescriptor(String descriptor) {
       this.descriptor = descriptor;
   }

   public IdentityType getIdentityType() {
       return identityType;
   }

   public void setIdentityType(IdentityType identityType) {
       this.identityType = identityType;
   }

   public RelationshipType getOwner() {
       return owner;
   }

   public void setOwner(RelationshipType owner) {
       this.owner = owner;
   }
}
