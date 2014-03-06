package org.forgeide.security.schema;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.PartitionClass;
import org.picketlink.idm.jpa.annotations.entity.ConfigurationName;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

/**
 * Stores identity partition information
 * 
 * @author Shane Bryzak
 */
@IdentityManaged(org.picketlink.idm.model.Partition.class)
@Entity
public class Partition implements Serializable
{
   private static final long serialVersionUID = -6840228093092371674L;

   @Id
   @Identifier
   private String id;

   @AttributeValue
   private String name;

   @PartitionClass
   private String typeName;

   @ConfigurationName
   private String configurationName;

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getTypeName()
   {
      return typeName;
   }

   public void setTypeName(String typeName)
   {
      this.typeName = typeName;
   }

   public String getConfigurationName()
   {
      return configurationName;
   }

   public void setConfigurationName(String configurationName)
   {
      this.configurationName = configurationName;
   }
}
