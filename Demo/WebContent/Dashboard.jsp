<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!doctype html>
<html lang="en">

<head>
<meta charset="utf-8" />
<title>Dashboard</title>

<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<script src="js/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/bootstrap.min.css">
<script src="js/hideshow.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		//When page loads...
		$("section#main").hide(); //Hide all content

		$("li.icn_weiboo").click(function() {
			$("section.weixin").hide();
			$("section.weibo").show();
		});

		$("li.icn_weixin").click(function() {
			$("section.weibo").hide();
			$("section.weixin").show();
		});

		//On Click Event
		$("ul.tabs li").click(function() {

			$("ul.tabs li").removeClass("active"); //Remove any "active" class
			$(this).addClass("active"); //Add "active" class to selected tab
			$(".tab_content").hide(); //Hide all tab content
			var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
			$(activeTab).fadeIn(); //Fade in the active ID content
			return false;
		});
		
		$("button#add_save").click(function(){
			var account = $("input#message-account").val();
			var url = $("input#message-url").val();
			var myDate = new Date();
			var year = myDate.getFullYear();
			var month = myDate.getMonth();
			var day = myDate.getDay();
			$("#tbody_weibo").append('<tr><td><input type="checkbox"></td><td>'+account+'</td><td>'+url+'</td><td>'+year+'-'+month+'-'+day+'</td><td><input type="image" src="images/icn_trash.png" title="Trash"></td></tr>');
			$(".modal").modal('hide');
			
		});

	});
</script>
</head>


<body>

	<header id="header">
		<hgroup>
			<h1 class="site_title">
				<a href="index.html">数据采集平台管理</a>
			</h1>
			<h2 class="section_title">Dashboard</h2>
			<div class="btn_view_site">
				<ul><li class="icn_jump_back"><a href="login-1.jsp">退出</a></li></ul>
			</div>
		</hgroup>
	</header>
	<!-- end of header bar -->


	<aside id="sidebar" class="column">
		<h3>数据源</h3>
		<ul class="toggle">
			<li class="icn_weiboo"><a href="#">微博</a></li>
			<li class="icn_weixin"><a href="#">微信</a></li>
			<li class="icn_else"><a href="#">其他</a></li>
		</ul>
		<h3>用户</h3>
		<ul class="toggle">
			<li class="icn_add_user"><a href="#">新增用户</a></li>
			<li class="icn_view_users"><a href="#">查看所有用户</a></li>
		</ul>
		<h3>管理员</h3>
		<ul class="toggle">
			<li class="icn_settings"><a href="#">选项</a></li>
			<li class="icn_security"><a href="#">安全</a></li>
			<li class="icn_jump_back"><a href="#">退出</a></li>
		</ul>

	</aside>
	<!-- end of sidebar -->


	<section id="main" class="column weibo">

		<article class="module width_3_quarter">
			<header>
				<h3>微博数据源</h3>
			</header>
			<div class="module_content">
				<article class="stats_graph">
					<img src="images/weibo.png" height="100" alt="" />
				</article>

				<article class="stats_overview">
					<div class="overview_today">
						<p class="overview_day">今日新增</p>
						<p class="overview_count">453</p>
					</div>
					<div class="overview_previous">
						<p class="overview_day">总微博数</p>
						<p class="overview_count">5,676</p>
					</div>
				</article>
				<div class="clear"></div>
			</div>
		</article>
		<!-- end of stats article -->

		<article class="module width_3_quarter">
			<header>
				<h3 class="tabs_involved">待爬微博帐号管理</h3>
			</header>

			<div class="tab_container">
				<div id="tab1" class="tab_content">
					<table class="tablesorter" cellspacing="0">
						<thead>
							<tr>
								<th></th>
								<th>微博帐号名</th>
								<th>URL</th>
								<th>添加时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="tbody_weibo">
							<tr>
								<td><input type="checkbox"></td>
								<td>新浪财经</td>
								<td>finance</td>
								<td>5th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>第一财经日报</td>
								<td>yicairibao</td>
								<td>6th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>001期货网</td>
								<td>001qh</td>
								<td>10th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>招商证券</td>
								<td>cmschina</td>
								<td>16th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>中国证券英才网</td>
								<td>stockyc</td>
								<td>16th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- end of #tab1 -->


			</div>
			<!-- end of .tab_container -->
			<div style="margin: 10px 0 10px 80%;">
				<button type="button" class="btn btn-primary" id="weibo_add"
					data-toggle="modal" data-target="#myModal">新增帐号</button>
			</div>
		</article>
		<!-- end of content manager article -->
	</section>


	<section id="main" class="column weixin">

		<article class="module width_3_quarter">
			<header>
				<h3>微信数据源</h3>
			</header>
			<div class="module_content">
				<article class="stats_graph">
					<img src="images/wechat.jpg" height="100" alt="" />
				</article>
				<article class="stats_overview">
					<div class="overview_today">
						<p class="overview_day">今日新增</p>
						<p class="overview_count">1,876</p>
					</div>
					<div class="overview_previous">
						<p class="overview_day">总微信文章数</p>
						<p class="overview_count">20,163</p>
					</div>
				</article>
				<div class="clear"></div>
			</div>
		</article>
		<!-- end of stats article -->

		<article class="module width_3_quarter">
			<header>
				<h3 class="tabs_involved">待爬微信帐号管理</h3>
			</header>

			<div class="tab_container">
				<div id="tab1" class="tab_content">
					<table class="tablesorter" cellspacing="0">
						<thead>
							<tr>
								<th></th>
								<th>微信帐号名</th>
								<th>URL</th>
								<th>添加时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="tbody_weibo">
							<tr>
								<td><input type="checkbox"></td>
								<td>Lorem Ipsum Dolor Sit Amet</td>
								<td>Articles</td>
								<td>5th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>Ipsum Lorem Dolor Sit Amet</td>
								<td>Freebies</td>
								<td>6th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>Sit Amet Dolor Ipsum</td>
								<td>Tutorials</td>
								<td>10th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>Dolor Lorem Amet</td>
								<td>Articles</td>
								<td>16th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
							<tr>
								<td><input type="checkbox"></td>
								<td>Dolor Lorem Amet</td>
								<td>Articles</td>
								<td>16th April 2011</td>
								<td><input type="image"
									src="images/icn_trash.png" title="Trash"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- end of #tab1 -->


			</div>
			<!-- end of .tab_container -->
			<div style="margin: 10px 0 10px 80%;">
				<button type="button" class="btn btn-primary" id="wechat_add"
					data-toggle="modal" data-target="#myModal">新增帐号</button>
			</div>
		</article>
		<!-- end of content manager article -->
	</section>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span>&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">请输入帐号名和URL：</h4>
				</div>
				<div class="modal-body">

					<div class="form-group" id="form-title">
						<label for="recipient-name" class="control-label">帐号名:</label> <input
							type="text" class="form-control" name="username"
							id="message-account">

					</div>
					<div class="form-group" id="form-content">
							<label for="recipient-name" class="control-label">URL:</label> <input
							type="text" class="form-control" name="url"
							id="message-url">
					</div>
					<div style="margin: 5px"></div>


					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary" id="add_save">Save
							changes</button>
					</div>
				</div>

			</div>
		</div>
	</div>




</body>

</html>