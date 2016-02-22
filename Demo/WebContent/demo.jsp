<!DOCTYPE html>
<html>
<head>
<title>新闻情感分析演示</title>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" type="text/css"
	href="http://static.bosonnlp.com/vendor/jquery-ui/themes/smoothness/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="http://static.bosonnlp.com/vendor/bootstrap/dist/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="http://static.bosonnlp.com/stylesheets/global.css?20160122" />
<link rel="stylesheet" type="text/css"
	href="http://static.bosonnlp.com/vendor/css-toggle-switch/dist/toggle-switch.min.css" />
<link rel="stylesheet" type="text/css"
	href="http://static.bosonnlp.com/stylesheets/demo.css?20160122" />
</head>
<body>

	<div id="content-page">

		<div id="sub-navbar">
			<div class="container">
				<ul>
					<li class="selected"><a href="/demo">单文本演示</a><span></span></li>
				</ul>
			</div>
		</div>
		<div id="demo-page">
			<div id="demo-container" class="container">
				<div class="main-title">
					<span class="title">新闻情感分析演示</span>
				</div>
				<div class="input-chunk chunk">
					<textarea id="txt-analysis" class="input"></textarea>
					<a id="btn-analysis" class="btn btn-arrow btn-radius"><span class="icon"></span>提交文本</a>
				</div>
				<div class="main-title">
					<span class="dot-line-gray"></span> <span class="title">分析结果</span>
				</div>
				<div>
				<span id="result"></span>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
$("a#btn-analysis").click(function(){
	var text = $("textarea#txt-analysis").val();
	$("span#result").html("<img src='images/loading.gif'/>");
	$.ajax({
		type : "POST",
		url : "SentimentAnalysis",
		data : {
			"text" : text
		},
		success : function(msg) {
			$("span#result").html(msg);
		}
	});
	
});
</script>
</html>
