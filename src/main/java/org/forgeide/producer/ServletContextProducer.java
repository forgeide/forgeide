package org.forgeide.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

@ApplicationScoped
public class ServletContextProducer {

    private ServletContext servletContext;

    public void init(@Observes ServletContextEvent event) {
        this.servletContext = event.getServletContext();
    }

    @Produces
    public ServletContext getServletContext() {
        return servletContext;
    }
}
