<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>${unitName }</title>
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
									<th>档案性质</th>
									<th style="border-right: none;">发布时间</th>
								</tr>
								<c:forEach items="${news_list}" var="news">
								<tr>
									<td class="taLeft"><a
										href="get_News?id=${news.id}"
										title="${news.title}" target="_blank"> ${news.title}</a></td>
									<td class="taLeft"> ${news.source}</td>
									<td><img alt="档案性质"
										src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAQCAIAAADbObvbAAABxElEQVR42uVVsYrCQBD1n8QigpVYBDHgYROwUMTCSoSIiChJITaC2AhiI0IQS0uxshRRsBUkKDYWioUhKMjdw+X2JFlzEZS746aamczOvn2z++J6/5Xm+qmNd7tdLBZzCqtcLtfrdWvd+XzmeZ74bovRssVikUgkaKgoiqqqplbuO8aA9W3peDyWJMkKN51ONxoNGpZKpVqtZt/NhICZdDkpvYcVCJrNJg2XyyW+appmP75nslWpVIbD4eVy8fl8zCPiUzQavXeS28qnsQVLpVLa1eAwC6rVaiQSYfZ5FVuUpMFggO2ZZbIs43G9hC1d1wVBOB6P9JoXi0U42+02HA7v9/tsNjuZTGw2eD5bxOn3+zj36XTCdRZFcb1eU4hvV7MH4YStB+TU9L4CgQDUC7OjSQCF+oVCoel06hCWNT+bzTKZDA1brdatuORyufl8boa1Wq3a7TZeE+EMVPV6vcPhQIYYj8e73S7UEspElhiG4fF4HmKrUCiMRiM4WIgLQyoxEDQn+kLuzBesYDAIdYYibzYbksEy4Egmk5CGfD5P2lHz+/0cx+F8JlhImhxmiIXcp3m93k6nwx4ixf7ff9V/EtYHoaSH8I3rmCwAAAAASUVORK5CYII="></td>
									<td>2015-07-01</td>
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
