<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<aside id="side_bar" class="span5">
	<ul id="admin_menu">
		<li><a class="btn btn-primary btn-large"
			href="${page.url_host}${page.url_apppath}admin/customer/list">Customers
				</a></li>
		<li><a class="btn btn-primary btn-large"
			href="${page.url_host}${page.url_apppath}admin/customer/search">Find
				Customer</a></li>
		<li><a class="btn btn-primary btn-large"
			href="${page.url_host}${page.url_apppath}admin/account/search">Find
				Account</a></li>
		<li><a class="btn btn-primary btn-large"
			href="${page.url_host}${page.url_apppath}admin/bank_branch/list">
				Branches List</a></li>
                                
                
	</ul>
</aside>