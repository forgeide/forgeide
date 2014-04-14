package org.forgeide.events;

import org.forgeide.model.ProjectResource;

/**
 * This event is raised when a new project resource is created
 *
 * @author Shane Bryzak
 */
public class NewResourceEvent
{
   private ProjectResource resource;

   public NewResourceEvent(ProjectResource resource)
   {
      this.resource = resource;
   }

   public ProjectResource getResource()
   {
      return resource;
   }
}
