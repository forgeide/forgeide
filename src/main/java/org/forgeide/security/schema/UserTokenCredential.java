/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.forgeide.security.schema;

import java.io.Serializable;
import java.util.Date;

import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.jpa.annotations.CredentialClass;
import org.picketlink.idm.jpa.annotations.CredentialProperty;
import org.picketlink.idm.jpa.annotations.EffectiveDate;
import org.picketlink.idm.jpa.annotations.ExpiryDate;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.ManagedCredential;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@ManagedCredential(TokenCredentialStorage.class)
@Entity
public class UserTokenCredential implements Serializable
{

   private static final long serialVersionUID = -6626899276420737288L;

   @Id
   @GeneratedValue
   private Long id;

   @OwnerReference
   @ManyToOne
   private IdentityType owner;

   @CredentialClass
   private String typeName;

   @Temporal(TemporalType.TIMESTAMP)
   @EffectiveDate
   private Date effectiveDate;

   @Temporal(TemporalType.TIMESTAMP)
   @ExpiryDate
   private Date expiryDate;

   @CredentialProperty
   @Column
   private String type;

   @CredentialProperty
   @Column(columnDefinition = "TEXT")
   private String token;

   public String getType()
   {
      return this.type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getToken()
   {
      return this.token;
   }

   public void setToken(String token)
   {
      this.token = token;
   }
}
