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
        <c:when test="${not empty resume}">
        <table class="table1b">
            <tr>
                <th class="th1b">Имя</th><th class="th1b">Позиция</th><th class="th1b">Возраст</th>
                <th class="th1b">Пол</th>
                <th class="th1b">Семейн. полож.</th><th class="th1b">Дети</th>
                <th class="th1b">Желаемая зарплата</th>
                <th class="th1b">Тип занятости</th>
                <th class="th1b">График</th>
                <th class="th1b">Готов. к командировкам</th>
                <th class="th1b">Водит. права</th>
                <th class="th1b">Курение</th>
            </tr>
            <tr>
                <td class="td1b"><a href="${resume.url}">${resume.name}</a></td><td class="td1b">${resume.header}</td><td class="td1b">${resume.age}</td>
                <td class="td1b">${resume.sexDescription}</td>
                <td class="td1b">${resume.maritalDescription}</td><td class="td1b">${resume.childDescription}</td>
                <td class="td1b">${resume.salary}</td>
                <td class="td1b">${resume.workingtype.title}</td>
                <td class="td1b">${resume.schedule.title}</td>
                <td class="td1b">${resume.journeyDescription}</td>
                <td class="td1b">${resume.driver_licenses}</td>
                <td class="td1b">${resume.smokeDescription}</td>
            </tr>
        </table>
            
        <table width="50%" class="table1b">
            <c:if test="${not empty resume.skills}">
            <tr><th class="th1b">Навыки</th></tr>
            <tr><td class="td1b">${resume.skillsHTML}</td></tr>
            </c:if>
            <c:if test="${not empty resume.personal_qualities}">
            <tr><th class="th1b">Качества</th></tr>
            <tr><td class="td1b">${resume.personal_qualitiesHTML}</td></tr>
            </c:if>
        </table>
        
        <table class="table1b">
            <tr>
                <th class="th1b">Образование</th>
                <td class="td1b">${resume.education.title}</td>
            </tr>
            <c:if test="${not empty resume.institutionList}">
                <tr>
                    <th class="th1b">Учреждение</th>
                    <th class="th1b">Форма</th>
                    <th class="th1b">Специализация</th>
                    <th class="th1b">Даты</th>
                    <th class="th1b">Описание</th>
                </tr>
                <c:forEach items="${resume.institutionMainList}" var="institution">               
                    <tr>
                        <td class="td1b">${institution.institution}</td>
                        <td class="td1b">${institution.form}</td>
                        <td class="td1b">${institution.specialityName}</td>
                        <td class="td1b">${institution.dates}</td>
                        <td class="td1b">${institution.description}</td>
                    </tr>
                </c:forEach>
                <c:if test="${not empty resume.institutionAdditionalList}">
                    <tr>
                        <th class="th1b" colspan="2">Дополнительное образование</th>
                        <th class="th1b" colspan="3"></th>
                    </tr>   
                    <c:forEach items="${resume.institutionAdditionalList}" var="institution">               
                        <tr>
                            <td class="td1b">${institution.institution}</td>
                            <td class="td1b">${institution.form}</td>
                            <td class="td1b">${institution.specialityName}</td>
                            <td class="td1b">${institution.dates}</td>
                            <td class="td1b">${institution.descriptionHTML}</td>
                        </tr>
                    </c:forEach>
                </c:if>
            </c:if>
        </table>
        <table>
            <td>
                <c:if test="${not empty resume.resumerubricList}">
                    <table class="table1b">
                        <tr>
                            <th class="th1b">Рубрики</th>
                            <td class="td1b"></td>
                        </tr>
                        <tr>
                            <th class="th1b">Отрасль</th>
                            <th class="th1b">Специализация</th>
                        </tr>

                        <c:forEach items="${resume.resumerubricList}" var="resumerubric">               
                            <tr>
                                <td class="td1b">${resumerubric.jobrubric.title}</td>
                                <td class="td1b">${resumerubric.jobspecialty.title}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>

            </td>
            <td>
                <table class="table1b">
                    <tr>
                        <th class="th1b">Город проживания</th>
                        <td class="td1b">${resume.cityAndDistinct}</td>
                    </tr>

                    <tr>
                        <th colspan="2" class="th1b">Готовность работать</td>
                    </tr>
                    <tr>
                        <th class="th1b">Город</th>
                        <th class="th1b">Район</th>
                    </tr>
                    <c:forEach items="${resume.citiesreferenceList}" var="citiesreference">               
                        <tr>
                            <td class="td1b">${citiesreference.city.title}</td>
                            <td class="td1b">${citiesreference.citydistinct.title}</td>
                        </tr>
                    </c:forEach>
                 </table>
            </td>
        </table>        
                    

        <table width="70%" class="table1b">
            <tr>
                <th class="th1b">Опыт работы</th>
                <td class="td1b" colspan="2">${resume.experience.title}</th>
            </tr>
            <c:if test="${not empty resume.resumejobList}">
                <tr>
                    <th class="th1b">Позиция</th>
                    <th class="th1b">Компания</th>
                    <th class="th1b">Город</th>
                    <th class="th1b" style="width:120px">Время работы</th>
                    <th class="th1b">Описание</th>
                </tr>                    
                <c:forEach items="${resume.resumejobList}" var="resumejob"> 
                    <tr>
                        <td class="td1b">${resumejob.PK.position}</td>
                        <td class="td1b">${resumejob.PK.company}</td>
                        <td class="td1b">${resumejob.city.title}</td>
                        <td class="td1b" style="width:120px">${resumejob.dates}</td>
                        <td class="td1b">${resumejob.descriptionHTML}</td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
            
        <c:if test="${not empty otherResumes}">
            <br>
            <table class="table1b">
                <tr>
                    <th class="th1b">Другие резюме пользователя</th>
                    <th class="th1b">Желаемая зарплата</th>
                </tr>
                <c:forEach items="${otherResumes}" var="resume"> 
                    <c:url value="/resume/id/${resume.id}" var="resumeUrl" />
                    <tr>
                        <td class="td1b"><a href="${resumeUrl}">${resume.header}</a></td>
                        <td class="td1b">${resume.wanted_salary}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
                    
        </c:when>    
        <c:otherwise>
            <h2>Резюме не найдено !</h2>
        </c:otherwise>
        </c:choose>
        
    </body>
</html>
