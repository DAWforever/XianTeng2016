<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Solr Search</title>
<link rel="stylesheet"
	href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="css/index.css" />
<link rel="stylesheet" href="css/common2.css" />
<link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<style>
.btn {
	color: white;
	background-color: #38f;
	width: 102px;
	height: 35px;
	font-size: 16px;
	border: 0;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<ul class="nav nav-pills pull-right header">
			<li><a href="./Dashboard.jsp" id="signin-btn"><span
					class="glyphicon glyphicon-user" aria-hidden="true" title="登录"></span>&nbsp;登录</a></li>
			<li><a href="./Search.jsp"><span
					class="glyphicon glyphicon-home" aria-hidden="true" title="实验室"></span>&nbsp;实验室</a></li>
			<li><a href="./Search.jsp"><span
					class="glyphicon glyphicon-question-sign" aria-hidden="true"
					title="帮助"></span>&nbsp;帮助</a></li>
		</ul>
	</div>

	<div class="logo">
		<img src="images/earth.jpg" width="150px" style="margin-left: 50px;" />
	</div>

	<div class="search-block">
		<form class="form-inline search-form" action="CoreQuery"
			method="post" novalidate="novalidate">
			<input type="text" class="search-input" maxlength="128"
				id="mrs_qstring" name="query"> <input type="submit"
				value="搜索" class="btn btn-primary btn-large" id="mrs_qbutton">
			<br>
		</form>
	</div>



	<div class="footer navbar-fixed-bottom">
		<ul class="pull-right">
			<li>2015</li>
			<li>|</li>
			<li>Copyright © <a href="http://iip.nju.edu.cn" target="blank"
				style="color: blue;">NJU_IIP_Group</a></li>
			<li>|</li>
			<li>Powered by <a href="http://lucene.apache.org/solr/"
				target="blank" style="color: blue;">Solr</a></li>
		</ul>
	</div>

</body>
</html>