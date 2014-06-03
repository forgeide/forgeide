package org.forgeide.security.schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.AttributeClass;
import org.picketlink.idm.jpa.annotations.AttributeName;
import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;

@Entity
public class PartitionAttribute
{
   @Id
   @GeneratedValue
   private Long id;

   @OwnerReference
   @ManyToOne
   private Partition owner;

   @AttributeClass
   private String attributeClass;

   @AttributeName
   private String attributeName;

   @AttributeValue
   private String attributeValue;

   @Id
   @GeneratedValue
   public Long getId()
   {
      return id;
   }

   public Partition getOwner()
   {
      return owner;
   }

   public void setOwner(Partition owner)
   {
      this.owner = owner;
   }

   public String getAttributeClass()
   {
      return attributeClass;
   }

   public void setAttributeClass(String attributeClass)
   {
      this.attributeClass = attributeClass;
   }

   public String getAttributeName()
   {
      return attributeName;
   }

   public void setAttributeName(String attributeName)
   {
      this.attributeName = attributeName;
   }

   @Lob
   @Column(name = "ATTRIBUTE_VALUE", length = 2048)
   public String getAttributeValue()
   {
      return attributeValue;
   }

   public void setAttributeValue(String attributeValue)
   {
      this.attributeValue = attributeValue;
   }

   public void setId(Long id)
   {
      this.id = id;
   }
}
