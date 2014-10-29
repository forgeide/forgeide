package org.forgeide.service.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles the redirect request for a GitHub oAuth authorization
 *
 * See https://developer.github.com/v3/oauth/#web-application-flow for details
 *
 * @author Shane Bryzak
 */
@WebServlet("/github-auth-redirect")
public class GitHubRedirectServlet extends HttpServlet
{
   private static final long serialVersionUID = 1689782533950057765L;

   public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException
   {
      String code = request.getParameter("code");
      String state = request.getParameter("state");

      response.sendRedirect("/success.html");
   }
}
