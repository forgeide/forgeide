package org.forgeide.security.schema;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.RelationshipClass;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;

/**
 * 
 * @author Shane Bryzak
 * 
 */
@IdentityManaged(Relationship.class)
@Entity
public class RelationshipType implements Serializable
{
   private static final long serialVersionUID = -3127084575080236509L;

   @Id
   @Identifier
   private String id;

   public String getId()
   {
      return this.id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   @RelationshipClass
   private String typeName;

   public String getTypeName()
   {
      return this.typeName;
   }

   public void setTypeName(String typeName)
   {
      this.typeName = typeName;
   }

}