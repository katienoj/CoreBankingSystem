<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h3>
	Details of Loan ID ${data.account_id}
	<a href="${page.url_host}${page.url_apppath}admin/loan/edit/${data.account_id}"
		style="margin-top: 15px;" class="pull-right btn btn-small btn-info">Edit
		Account</a>
	<a href="${page.url_host}${page.url_apppath}admin/loan/transaction/${data.account_id}"
		style="margin-top: 15px;margin-right:20px;" class="pull-right btn btn-small">View Transactions</a>
</h3>

<table class="table">
	<tr>
		<td>Loan ID</td>
		<td>${data.account_id}</td>
	</tr>
	<tr>
		<td>Balance</td>
		<td><fmt:formatNumber value="${data.amount}" type="currency"/></td>
	</tr>
	<tr>
		<td>Account Type</td>
		<td><c:choose>
				<c:when test="${data.account_type ==1 }">
							Normal Loan
							</c:when>
				<c:when test="${data.account_type == 2 }">
						        Development Loan
							</c:when>
                                <c:when test="${data.account_type ==3 }">
							Emergency Loan
							</c:when>
				<c:when test="${data.account_type == 4 }">
						        Education Loan
							</c:when>
			</c:choose></td>
	</tr>
</table>

<h3>Customers who own Account</h3>
<table class="table table-hover">
	<thead>
		<tr>
			<td>Customer ID</td>
			<td>Name</td>
			<td></td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${data.customers}" var="_customer">
			<tr>
				<td>${_customer.customer_id}</td>
				<td><b style="font-size: 15px;">${_customer.givenname}</b><br />${_customer.username}
				</td>
				<td><a
					href="${page.url_host}${page.url_apppath}admin/customer/details/${_customer.customer_id}"
					class="btn btn-small">View Details</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
