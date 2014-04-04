package org.forgeide.service.metadata;

/**
 * 
 * @author Shane Bryzak
 */
public class ResultMetadata
{
   private boolean passed;
   private String message;
   private String exception;

   public boolean isPassed()
   {
      return passed;
   }

   public void setPassed(boolean passed)
   {
      this.passed = passed;
   }

   public String getMessage()
   {
      return message;
   }

   public void setMessage(String message)
   {
      this.message = message;
   }

   public String getException()
   {
      return exception;
   }

   public void setException(String exception)
   {
      this.exception = exception;
   }
}
