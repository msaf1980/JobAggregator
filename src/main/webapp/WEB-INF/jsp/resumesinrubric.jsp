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
        
        <c:choose>
            <c:when test="${not empty resumeList}">
                <table class="table1b">
                    <tr>
                        <th class="th1b">Имя</td>
                        <th class="th1b">Позиция</td>
                        <th class="th1b">Источник</td>    
                    </tr>
                    <c:forEach items="${resumeList}" var="resume">               
                        <tr>
                            <c:url value="/resume/id/${resume.id}" var="resumeUrl" />
                            <td class="td1b"><a href="${resumeUrl}">${resume.name}</a></td>
                            <td class="td1b">${resume.header}</td>
                            <td class="td1b"><a href="${resume.url}">Источник</a></td>
                        </tr>
                    </c:forEach>
                </table>

            </c:when>    
            <c:otherwise>
                <c:choose>
                    <c:when test="${empty resumePageList}">
                        <h2>Ни одного резюме не найдено !</h2>
                    </c:when>
                    <c:otherwise>
                        <h2>Резюме закончились !</h2>                    
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>

        <table>
            <tr>
                <c:forEach items="${resumePageList}" var="resumePage">               
                    <td>
                        <c:choose>
                            <c:when test="${empty resumePage.id}">
                                ${resumePage.title} 
                            </c:when>    
                            <c:otherwise>
                                <c:url value="/rubrics/id/${locationId}/${resumePage.id}" var="resumePageUrl" />
                                <a href="${resumePageUrl}"/>${resumePage.title}</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </table>
       
    </body>
</html>    