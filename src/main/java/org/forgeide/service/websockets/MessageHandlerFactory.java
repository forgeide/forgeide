package org.forgeide.service.websockets;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.websocket.Session;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.forgeide.annotations.MessageHandler;
import org.forgeide.annotations.MessageOperation;
import org.picketlink.common.reflection.Reflections;

/**
 * 
 * @author Shane Bryzak
 */
public class MessageHandlerFactory implements Extension
{
   private Map<String,Map<String,Method>> handlers = new HashMap<String,Map<String,Method>>();

   public <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> event)
   {
      AnnotatedType<X> type = event.getAnnotatedType();

      for (final Annotation annotation : type.getAnnotations())
      {
         if (MessageHandler.class.isInstance(annotation))
         {
            MessageHandler handler = type.getAnnotation(MessageHandler.class);
            Map<String,Method> methods = new HashMap<String,Method>();
            handlers.put(handler.value(), methods);

            for (final AnnotatedMethod<? super X> m : type.getMethods())
            {
               if (m.getParameters().size() == 2 && 
                   m.getParameters().get(0).getBaseType().equals(Message.class) &&
                   m.getParameters().get(1).getBaseType().equals(Session.class) &&
                   m.isAnnotationPresent(MessageOperation.class))
               {
                  MessageOperation operation = m.getAnnotation(MessageOperation.class);
                  methods.put(operation.value(), m.getJavaMember());
               }
            }

            break;
         }
      }
   }

   public void handleMessage(Message msg, Session session)
   {
      Map<String,Method> methods = handlers.get(msg.getCat());
      if (methods != null)
      {
         Method m = methods.get(msg.getOp());
         if (m != null)
         {
            List<?> refs = BeanProvider.getContextualReferences(m.getDeclaringClass(), false);
            if (refs.size() != 1)
            {
               throw new IllegalStateException("Ambiguous message handlers found for message: " + msg);
            }
            Reflections.invokeMethod(m, refs.get(0), msg, session);
         }
      }
   }
}
