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
					<dt>当事人：</dt>
					<dd>${doc.iname}&nbsp;</dd>
					<dt>判决标题：</dt>
					<dd>${doc.title}&nbsp;</dd>
					<dt>判决文号：</dt>
					<dd>${doc.caseCode}&nbsp;</dd>
					<dt>判决内容：</dt>
					<dd id="content">${doc.content}</dd>
					<dt>判决法院：</dt>
					<dd>${doc.courtName}</dd>
					<dt>发布时间：</dt>
					<dd>${doc.publishDate}&nbsp;</dd>
				</dl>
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
