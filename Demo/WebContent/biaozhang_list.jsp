<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>${unitName}</title>
<link rel="stylesheet" href="css/default.css" />
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/content.css">
</head>


<body>
	<div id="main">
		<div class="cont_ltit">
			<div class="top_title" style="text-align: center;">
				<span>媒体评价信息</span>
			</div>
			<div class="cont_lcen">
				<div class="tabJs">
					<div class="tab01">
						<table cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="300px">
								<col width="210px">
								<col width="80px">
								<col width="80px">
							</colgroup>
							<tbody>
								<tr>
									<th>档案标题</th>
									<th>发布单位</th>
									<th style="border-right: none;">发布时间</th>
								</tr>
								<c:forEach items="${BiaoZhanglist}" var="data">
								<tr>
									<td><a
										href="get_BiaoZhang?id=${data.id}"
										title="${data.title}" target="_blank">${data.title}</a></td>
									<td> ${data.unit}</td>
									<td>${data.date}</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
