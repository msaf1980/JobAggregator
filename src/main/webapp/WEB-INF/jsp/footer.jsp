<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table>
<tr>    
    <td>База резюме</td>
    <td>
        <c:url value="/" var="mainUrl" />
        <a href="${mainUrl}"/>Главная</a>
    </td>
    <td>
        <c:url value="/rubrics" var="rubricsUrl" />
        <a href="${rubricsUrl}"/>Рубрики</a>
    </td>
</tr>
</table>