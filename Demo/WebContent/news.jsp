<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title> ${unitName}</title>
<link rel="stylesheet" href="css/default.css" />
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/content.css">

</head>

<body>
	<div id="main">
		<div class="cont_ltit">
			<div class="cont_lcen">
				<h1 class="h1_company">
					${news.title}</a>
				</h1>
				<div class="company_dl">
					<dl>
						<dt>档案编号：</dt>
						<dd>MT-371-0164-6585&nbsp;</dd>
						<dt>档案性质：</dt>
						<dd>良好信息</dd>
						<dt>发布媒体：</dt>
						<dd>${news.source}</dd>
						<dt>发布时间：</dt>
						<dd>2015-07-01&nbsp;</dd>
						<dt>档案内容：</dt>
						<dd id="content">
						${news.content}
						</dd>
						<dt>相关链接：</dt>
						<dd>
							<a target="_blank"
								href="${news.url}">${news.url}</a>&nbsp;
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
var content = $("dd#content").text();
var key = "${unitName}";
var a = new RegExp(key,"g");
content = content.replace(a,("<span style='color:#b70000'>" + key + "</span>"))
$("dd#content").html(content);
</script>
</html>
