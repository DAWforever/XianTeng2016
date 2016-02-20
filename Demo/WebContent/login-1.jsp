<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Login One</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" href="css/bootstrap.min.css">
<link href="css/templatemo_style.css" rel="stylesheet" type="text/css">
</head>
<body class="templatemo-bg-gray">
	<div class="container">
		<div class="col-md-12">
			<h1 class="margin-bottom-15">Welcome!</h1>
			<form
				class="form-horizontal templatemo-container templatemo-login-form-1 margin-bottom-30"
				action="login" method="post">
				<div class="form-group">
					<div class="col-xs-12">
						<div class="control-wrapper">
							<label for="username" class="control-label fa-label"><i
								class="fa fa-user fa-medium"></i></label> <input type="text"
								class="form-control" name="username" placeholder="Username">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<div class="control-wrapper">
							<label for="password" class="control-label fa-label"><i
								class="fa fa-lock fa-medium"></i></label> <input type="password"
								class="form-control" name="password" placeholder="Password">
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<div class="checkbox control-wrapper">
							<label> <input type="checkbox"> 记住我
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<div class="control-wrapper">
							<input type="submit" value="登录" class="btn btn-info"> <a
								href="forgot-password.html" class="text-right pull-right">忘记密码?</a>
						</div>
					</div>
				</div>
				<hr>
			</form>
			<div class="text-center">
				<a href="Search.jsp" class="templatemo-create-new"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 首页| </a> 
				<a href="create-account.html" class="templatemo-create-new">注册 <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span></a>
			</div>
		</div>
	</div>
</body>
</html>