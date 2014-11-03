package org.forgeide.service.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.forgeide.controller.GitHubRegistrationController;

/**
 * Handles the redirect request for a GitHub oAuth authorization
 *
 * See https://developer.github.com/v3/oauth/#web-application-flow for details
 *
 * @author Shane Bryzak
 */
@WebServlet("/github_auth_callback")
public class GitHubRedirectServlet extends HttpServlet
{
   private static final long serialVersionUID = 1689782533950057765L;

   @Inject GitHubRegistrationController controller;

   public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException
   {
      String code = request.getParameter("code");
      String state = request.getParameter("state");

      controller.processCode(state, code);

      response.sendRedirect("/github_callback.html");
   }
}
