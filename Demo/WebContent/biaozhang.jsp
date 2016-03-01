<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>重庆工业设备安装集团有限公司</title>
<link rel="stylesheet" href="css/default.css" />
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/content.css">

</head>

<body>
	<div id="main">
		<div class="cont_ltit">
			<div class="top_title" style="text-align: center;">
				<span>通报表彰信息</span>
			</div>
			<div class="company_dl company_act_photo">
				<dl>
					<dt>标题：</dt>
					<dd>${doc.title}&nbsp;</dd>
					<dt>表彰企业：</dt>
					<dd>${doc.corp_Name}&nbsp;</dd>
					<dt>文号：</dt>
					<dd>${doc.code}&nbsp;</dd>
					<dt>表彰年度：</dt>
					<dd>${doc.year}&nbsp;</dd>
					<dt>发布单位：</dt>
					<dd>${doc.unit}</dd>
					<dt>发布日期：</dt>
					<dd>${doc.date}</dd>
					<dt>数据来源：</dt>
					<dd><a href="${doc.data_Source}">${doc.data_Source}</a>&nbsp;</dd>
				</dl>
			</div>
		</div>
	</div>
</body>
</html>
