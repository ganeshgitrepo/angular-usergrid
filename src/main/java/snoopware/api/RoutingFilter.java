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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class RoutingFilter implements Filter {
  private final Logger logger = LoggerFactory.getLogger(RoutingFilter.class);

  private String angularRoutes[] = {
    "login",  
    "register",  
    "page1",  
    "page2"  
  };

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest)request;

    for (String angularRoute : angularRoutes) {
      if (req.getServletPath().endsWith(angularRoute)) {
        RequestDispatcher rd = req.getRequestDispatcher("/index.html");
        logger.debug("Dispatching {} to /index.html", req.getServletPath());
        rd.forward(request, response);
        return;
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {}
}
