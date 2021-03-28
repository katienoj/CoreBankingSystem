<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h4>Account Details</h4>
<table class="table table-striped">
	<tr>
		<td>Bank Account No.</td>
		<td>${account.account_id}</td>
	</tr>
	<tr>
		<td>Balance</td>
		<td> Ksh. <fmt:formatNumber value="${account.amount}" /></td>
	</tr>


	<tr>
		<td>Account Type</td>
		<td><c:choose>
				<c:when test="${account.account_type == 1 }">Transactional Account </c:when>
			        <c:when test="${account.account_type == 2 }">Savings Account </c:when>
                                <c:when test="${account.account_type == 3 }">Joint Account </c:when>
			        <c:when test="${account.account_type == 4 }">Corporate Account </c:when>
			        <c:otherwise> Undefined </c:otherwise>
			</c:choose></td>
	</tr>
	<tr>
		<td>Bank Branch</td>
		<td>${account.bank_branch.name}<br />${account.bank_branch.location}
		</td>
	</tr>

	<tr>
		<td>Account Ownership</td>
		<td><c:choose>
				<c:when test="${account.joint_account == false }">Single Account</c:when>
				<c:otherwise> Joint Account </c:otherwise>
			</c:choose> <c:if test="${account.joint_account == true }">
				<br />
				<u>Owners</u>
				<ul>
					<c:forEach items="${account.customers}" var="_customer">
						<li>${_customer.givenname}</li>
					</c:forEach>
				</ul>
			</c:if></td>
	</tr>
</table>