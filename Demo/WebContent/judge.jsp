<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>重庆市翔宇建筑工程（集团）</title>
<link rel="stylesheet" href="css/default.css" />
<link rel="stylesheet" href="css/common.css">
<link rel="stylesheet" href="css/content.css">

</head>

<body>
	<div id="main">
		<div class="cont_ltit">
			<div class="top_title" style="text-align: center;">
				<span>人民法院判决信息</span>
			</div>
			<div class="company_dl company_act_photo">
				<dl>
					<dt>法定代表人或者负责人姓名：</dt>
					<dd>${doc.businessEntity}&nbsp;</dd>
					<dt>省份：</dt>
					<dd>${doc.areaName}&nbsp;</dd>
					<dt>判决文号：</dt>
					<dd>${doc.caseCode}&nbsp;</dd>
					<dt>生效法律文书确定的义务：</dt>
					<dd>${doc.duty}</dd>
					<dt>被执行人的履行情况：</dt>
					<dd>${doc.performance}</dd>
					<dt>失信被执行人行为具体情形：</dt>
					<dd>${doc.disruptTypeName}</dd>
					<dt>判决法院：</dt>
					<dd>${doc.courtName}</dd>
					<dt>判决时间：</dt>
					<dd>${doc.publishDate}&nbsp;</dd>
				</dl>
			</div>
		</div>
	</div>
</body>
</html>
