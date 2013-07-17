<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Tribes" otherwise="/login.htm" redirect="module/tribe/tribe.list" />
	
<%@ include file="/WEB-INF/template/header.jsp" %>

<h2><spring:message code="tribe.manage.title"/></h2>	

<a href="tribe.form"><spring:message code="tribe.add"/></a>

<br /><br />

<b class="boxHeader"><spring:message code="tribe.list.title"/></b>
<form method="post" class="box">
	<table>
		<tr>
			<th> </th>
			<th> <spring:message code="tribe.name"/> </th>
		</tr>
		<c:forEach var="tribe" items="${tribeList}">
			<tr>
				<td><input type="checkbox" name="tribeId" value="${tribe.tribeId}"></td>
				<td class="<c:if test="${tribe.retired == true}">retired</c:if>"><a href="tribe.form?tribeId=${tribe.tribeId}">
					   ${tribe.name}
					</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<input type="submit" value="<spring:message code="tribe.retire"/>" name="retire">
	<input type="submit" value="<spring:message code="tribe.unretire"/>" name="unretire">
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>