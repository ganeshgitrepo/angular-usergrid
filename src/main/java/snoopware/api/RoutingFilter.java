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
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class RoutingFilter implements Filter {
	private static final Logger log = Logger.getLogger(RoutingFilter.class.getName());

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
				//log.log(Level.INFO,"Dispatching {0} to /index.html", req.getServletPath());
				rd.forward(request, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
}
