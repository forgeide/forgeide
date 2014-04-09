package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.forgeide.security.schema.IdentityType;

/**
 * Defines access level for project resources
 *
 * @author Shane Bryzak
 *
 */
@Entity
public class ResourceAccess implements Serializable
{
   private static final long serialVersionUID = -790604070028430439L;

   public enum AccessLevel {READ, WRITE, READ_WRITE};

   @Id @GeneratedValue
   private Long id;

   @ManyToOne
   private ProjectResource resource;

   @ManyToOne
   private IdentityType user;

   private AccessLevel accessLevel;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public ProjectResource getResource()
   {
      return resource;
   }

   public void setResource(ProjectResource resource)
   {
      this.resource = resource;
   }

   public IdentityType getUser()
   {
      return user;
   }

   public void setUser(IdentityType user)
   {
      this.user = user;
   }

   public AccessLevel getAccessLevel()
   {
      return accessLevel;
   }

   public void setAccessLevel(AccessLevel accessLevel)
   {
      this.accessLevel = accessLevel;
   }
}
