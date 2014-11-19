package org.forgeide.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class Foo implements Serializable
{
   private String value;

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }
}
