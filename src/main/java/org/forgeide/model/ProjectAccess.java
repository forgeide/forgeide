package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.forgeide.security.schema.IdentityType;

/**
 * Controls access levels for projects
 *
 * @author Shane Bryzak
 *
 */
@Entity
public class ProjectAccess implements Serializable
{
   private static final long serialVersionUID = 6539427720605504095L;

   public enum AccessLevel {OWNER, READ, READ_WRITE, RESTRICTED};

   @Id @GeneratedValue
   private Long id;

   @ManyToOne
   private Project project;

   @ManyToOne
   private IdentityType user;

   // Indicates whether the user currently has this project open
   private boolean open;

   // Access level
   private AccessLevel accessLevel;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public Project getProject()
   {
      return project;
   }

   public void setProject(Project project)
   {
      this.project = project;
   }

   public IdentityType getUser()
   {
      return user;
   }

   public void setUser(IdentityType user)
   {
      this.user = user;
   }

   public boolean isOpen()
   {
      return open;
   }

   public void setOpen(boolean open)
   {
      this.open = open;
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
