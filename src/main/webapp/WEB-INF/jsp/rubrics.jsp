<%@page import="webapp.model.JobRubric"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="header.jsp"/>
    </head>
    <body>
        <jsp:include page="footer.jsp"/>
        <jsp:include page="resumesearch.jsp"/>
        
        <h4>Рубрики</h4>
        <table>
            <tr><td><a href="rubrics/id/0/0">Все</a></td></tr>
            <%
            List<JobRubric> jobRubricList = (List)request.getAttribute("jobRubricList");
            int size = jobRubricList.size();
            int sizehalf = size / 2;
            for (int i = 0; i < sizehalf; i++){
                out.println("<tr>");
                out.println("<td><a href=\"rubrics/id/" + jobRubricList.get(i).getId() + "/0\">");
                out.println(jobRubricList.get(i).getTitle() + "</a></td>");
                int j = i + sizehalf;
                if (j < size) {
                    out.println("<td><a href=\"rubrics/id/" + jobRubricList.get(j).getId() + "/0\">");
                    out.println(jobRubricList.get(j).getTitle() + "</a></td>");
                } else {
                    out.println("<td></td>");
                }
                out.println("</tr>");
            }
            %>
        </table>    
    </body>
</html>
