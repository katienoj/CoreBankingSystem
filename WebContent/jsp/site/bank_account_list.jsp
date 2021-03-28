<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table class="table table-hover">
	<thead>
		<tr>
			<td>Bank Account No.</td>
			<td>Available Balance</td>
			<td>Account Type</td>
			<td></td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${customer.accounts}" var="_account">
			<tr>
				<td>${_account.account_id}</td>
				<td>Ksh.<fmt:formatNumber value="${_account.amount}" /></td>
				<td><c:choose>
						<c:when test="${account.account_type == 1 }">Transactional Account </c:when>
				                <c:when test="${account.account_type == 2 }">Savings Account </c:when>
                                                <c:when test="${account.account_type == 3 }">Joint Account </c:when>
				                <c:when test="${account.account_type == 4 }">Corporate Account </c:when>
			                        <c:otherwise> Undefined </c:otherwise>
					</c:choose></td>
				<td><a class="btn btn-success"
					href="${page.url_host}${page.url_apppath}session/account/${_account.account_id}">Select</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>