<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ page import="java.io.*,java.util.*" %>

<p>Hello ${user.systemId}!</p>

<%
   // New location to be redirected
   String site = new String("adminui/adminUiHome.page");
   response.setHeader("Location", site); 
%>

<%@ include file="/WEB-INF/template/footer.jsp"%>