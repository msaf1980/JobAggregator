<%-- 
    Document   : resync
    Created on : 19.12.2016, 13:13:31
    Author     : msv
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cp" value="${pageContext.request.servletContext.contextPath}" scope="request" />

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
       <jsp:include page="header.jsp"/>
    </head>
    <body>
        <jsp:include page="footer.jsp"/>   
        
        <table border="0">
            <tr>
                <td>Статус : </td>
                <td>${resync.message}</td>
            </tr>
        </table>
    </body>
</html>
