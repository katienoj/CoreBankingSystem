<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h4>Transaction History For Account No :
	${account.account_id}</h4>
<table class="table table-hover table table-striped">
	<thead>
		<tr>
			<td>Transaction Time</td>
			<td>Amount</td>
			<td>Type</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${t_histories}" var="history">
			<tr>
				<td><fmt:formatDate value="${history.transaction_time}" pattern="MM/d/yyyy - hh:mm" /></td>
				<td>Ksh.<fmt:formatNumber value="${history.transaction_amount}"/></td>
				<td><c:choose>
						<c:when test="${history.transaction_type == 1}">
							Deposit
						</c:when>
						<c:when test="${history.transaction_type == 2}">
							Withdrawal
						</c:when>
						<c:when test="${history.transaction_type == 3}">
							<c:choose>
								<c:when test="${history.transaction_relation_type == 1}">
									Transfer to Account No - ${history.transaction_relation_account_id}
								</c:when>
								<c:when test="${history.transaction_relation_type == 2}">
									Receive from Account No - ${history.transaction_relation_account_id}
								</c:when>
								<c:otherwise>&nbsp;</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>Unknown Transaction</c:otherwise>
					</c:choose></td>
			</tr>
		</c:forEach>
	</tbody>
</table>