package org.forgeide.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Required for activating JAX-RS without any XML configuration
 *
 * @author Shane Bryzak
 *
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application
{
  // Intentionally left blank
}
