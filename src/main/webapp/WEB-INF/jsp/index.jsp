<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="header.jsp"/>
    </head>
    <body>

<jsp:include page="footer.jsp"/>        

<jsp:include page="resumesearch.jsp"/>

<form:form method="POST" commandName="resyncForm" action = "" >
    <input type="submit" class="button" name="resync" value="Синхронизация"/>
</form:form>

    </body>
</html>
