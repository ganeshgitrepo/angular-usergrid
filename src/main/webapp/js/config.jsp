<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%
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
java.util.Properties props = new java.util.Properties();
props.load(this.getClass().getResourceAsStream("/app.properties"));
%>
var config = { 
  rootUri:     "<%= props.getProperty("app.rootUri") %>",
  rootPath:    "<%= props.getProperty("app.rootPath") %>",
  apiPath:     "<%= props.getProperty("app.apiPath") %>",
  usergridUri: "<%= props.getProperty("app.usergridUri") %>",
  appName:     "<%= props.getProperty("app.name")    %>", 
  orgName:     "<%= props.getProperty("org.name")    %>"
};