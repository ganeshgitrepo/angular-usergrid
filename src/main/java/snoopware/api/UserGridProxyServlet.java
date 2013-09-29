/*
 * Copyright 2013 David M. Johnson (snoopdave@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package snoopware.api;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.java.client.Client;

/**
 * Proxy all requests to UserGrid and to add credentials to /users GET and POST requests.
 *
 * @author David M. Johnson (snoopdave@gmail.com)
 */
@WebServlet(name = "usergridProxy", urlPatterns = {"/api/*"},
  initParams = {
    @WebInitParam(name = "log", value = "false"),
    @WebInitParam(name = "targetUri", value = "https://api.usergrid.com")
})
@SuppressWarnings("serial")
public class UserGridProxyServlet extends ProxyServlet {
  private final Logger log = LoggerFactory.getLogger(RoutingFilter.class);
  private Client client;
  private String applicationName;

  /**
   * Override to authorized UserGrid client with creds from app.propeties.
   */
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    client = new UsergridClient();
    applicationName = UsergridClient.applicationName;
    targetUri = UsergridClient.targetUri;
  }

  /**
   * Override to add Authorization to requests that need it.
   * @param req
   * @param proxyReq
   */
  @Override
  protected void copyRequestHeaders(HttpServletRequest req, HttpRequest proxyReq) {
    super.copyRequestHeaders(req, proxyReq);

    boolean hasAccessToken = req.getQueryString() == null
            ? false : req.getQueryString().contains("access_token=");

    if (!hasAccessToken
      && ("POST".equalsIgnoreCase(req.getMethod()) || "GET".equalsIgnoreCase(req.getMethod()))
      && req.getPathInfo().contains("/" + applicationName + "/user")) {

      String header = "Bearer " + client.getAccessToken();
      proxyReq.setHeader("Authorization", header);
      log.debug("Added header {} to URL {}", header, req.getRequestURL().toString());
    } else {
      log.debug("Not adding header to request with " + req.getRequestURL().toString());
    }
  }
}
