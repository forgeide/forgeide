package org.forgeide.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.forgeide.model.ProjectTemplate;
import org.forgeide.model.TemplateService;

/**
 * RESTful endpoints for managing project templates
 *
 * @author Shane Bryzak
 *
 */
@Path("/templates")
@Stateless
public class TemplateServices
{
   @Inject EntityManager entityManager;

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public List<ProjectTemplate> listTemplates()
   {
       return entityManager.createQuery("select t from ProjectTemplate t", ProjectTemplate.class)
           .getResultList();
   }

   @GET
   @Path("/{code}")
   @Produces(MediaType.APPLICATION_JSON)
   public ProjectTemplate getTemplate(@PathParam("code") String code)
   {
       return entityManager.createQuery(
                "select t from ProjectTemplate t where t.code = :code", ProjectTemplate.class)
                .setParameter("code", code)
                .getSingleResult();
   }

   @GET
   @Path("/{code}/services")
   @Produces(MediaType.APPLICATION_JSON)
   public List<TemplateService> listServices(@PathParam("code") String code)
   {
      ProjectTemplate t = entityManager.createQuery(
               "select t from ProjectTemplate t where t.code = :code", ProjectTemplate.class)
               .setParameter("code", code)
               .getSingleResult();

      return entityManager.createQuery(
               "select s from TemplateService s where s.template = :template", 
               TemplateService.class)
               .setParameter("template", t)
               .getResultList();
   }

   @GET
   @Path("/{code}/services/{serviceCode}")
   @Produces(MediaType.APPLICATION_JSON)
   public TemplateService getService(@PathParam("code") String code,
            @PathParam("serviceCode") String serviceCode)
   {
      ProjectTemplate t = entityManager.createQuery(
               "select t from ProjectTemplate t where t.code = :code", ProjectTemplate.class)
               .setParameter("code", code)
               .getSingleResult();

      return entityManager.createQuery(
               "select s from TemplateService s where s.template = :template and s.code = :code", 
               TemplateService.class)
               .setParameter("template", t)
               .setParameter("code", serviceCode)
               .getSingleResult();
   }
}
