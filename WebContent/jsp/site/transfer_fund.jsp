<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class="table .table-condensed">
	<thead>
		<h4>Account Summary</h4>
	</thead>
	<tr>
		<td>Bank Account No.</td>
		<td>${account.account_id}</td>
	</tr>
	<tr>
		<td>Balance</td>
		<td>Ksh.<fmt:formatNumber value="${account.amount}"/></td>
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
</table>

<div class="center-holder">

	<form id="form-withdraw" name="form-withdraw" class="center-box"
		action="${page.url_host}${page.url_apppath}post/transfer"
		method="post">
		<h4>Transfer Money</h4>
		<table>
			<tr>
				<td style="width: 150px;"><label>Transfer to Ac No</label></td>
				<td style="width: 250px;"><input id="account_id_to" name="account_id_to" autocomplete="off"
					type="text" class="input-block-level" required="required"
					placeholder="Bank Account ID">
					</td>
			</tr>
			<tr>
				<td style="width: 150px;"><label>Amount to Transfer</label></td>
				<td style="width: 250px;"><input name="transfer_amount" autocomplete="off"
					type="text" class="input-block-level" required="required"
					placeholder="Transfer Amount"></td>
			</tr>
			<tr>
				<td><label>Security Pin</label></td>
				<td><input name="pin" type="text"  autocomplete="off"
					required="required" class="input-block-level"
					placeholder="Security Pin" /></td>
			</tr>
			<tr>
				<td></td>
				<td><a href="#myModal" role="button"
					class="btn btn-warning btn-primary" data-toggle="modal"
					style="float: right;"> Transfer</a></td>
			</tr>
		</table>
		<!-- Modal -->
		<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">x</button>
				<h3 id="myModalLabel">Confirmation</h3>
			</div>
			<div class="modal-body">
				<p class="alert">
					Are you sure you want to transfer?.<br />
				</p>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
				<button class="btn btn-primary btn-warning" type="submit">Transfer</button>
			</div>
		</div>

	</form>
</div>