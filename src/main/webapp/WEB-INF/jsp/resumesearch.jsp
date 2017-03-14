<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<form:form method="POST" commandName="resumesSearch" action = "" >
<%-- <form:errors path="*" cssClass="error" /> --%>   
<table>

<tr>
<td>Расположение : </td>
<td>
<!-- ComboBox-->
<form:select path="location">
	<form:option value="${resumesSearch.location}"/>
	<form:options items="${locations}" />       
</form:select>
</td>
<td><form:errors path="location" cssClass="error" /></td>
<td>Cтрока поиска резюме (SQL LIKE) : </td>
<!-- TextBox -->
<td><form:input path="searchstr" value = "${resumesSearch.searchstr}"/></td>
<td><form:errors path="searchstr" cssClass="error" /></td>
<td><input type="submit" class="button" name="submit" value="Установить"/></td>
</tr>
</table>

</form:form>
