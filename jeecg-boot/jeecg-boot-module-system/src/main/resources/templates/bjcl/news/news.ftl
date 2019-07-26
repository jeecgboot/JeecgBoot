<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="Keywords" content="${keyword}" />
		<meta name="Description" content="${descContent}" />
<script>
			var _hmt = _hmt || [];
			(function() {
			  var hm = document.createElement("script");
			  hm.src = "https://hm.baidu.com/hm.js?a37f171ad0e5865015d998e281137dae";
			  var s = document.getElementsByTagName("script")[0];
			  s.parentNode.insertBefore(hm, s);
			})();
</script>

		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<title>${title}</title>
		<link rel="icon" type="image/x-ico" href="../img/favicon.ico" />
		<link rel="stylesheet" href="../css/bootstrap.min.css" />
		<link rel="stylesheet" href="../css/animate.min.css" />
		<link rel="stylesheet" href="../css/reset.css" />
		<link rel="stylesheet" href="../css/style.css" />

	</head>
	<body>
		<div>
			<!--导航-->
			<nav class="navbar navbar-default" role="navigation">
				<div class="container"> 
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#example-navbar-collapse">
							<span class="sr-only">切换导航</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
					</div>
					<a href=""><div class="logo animated fadeInDown"></div></a>
					<div class="collapse navbar-collapse" id="example-navbar-collapse">
						<div class="container">
							<ul class="nav navbar-nav">
												       			<li  class="animated fadeInDown" >
									<a href="../../index.html" >首页</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="..//about.html" >关于我们</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="..//products/page_1.html" >产品中心</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="..//shop.html" >门店展示</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="..//news/page_1.html" >新闻动态</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="..//contact.html" >联系我们</a>
					          	</li>
					          								</ul>
						</div>
					</div>
				</div>
			</nav>			<!--轮播图-->
			<div id="myCarousel" class="carousel slide">
			    <div class="carousel-inner">
			        <div class="item active">
			             <img src="../img/banner/banner_common.jpg" class="img-responsive" />
			        </div>
			    </div>
			</div>
			
			<div class="content_box">
				<div class="container">
					<p class="navigation">当前位置：<a href="/">首页</a> &gt; <a href="/news/page_1.html"> 新闻动态 </a> >${title}</p>
					<p class="content_tit">${title}</p>
					<p class="browse text-center">日期：${updateTime}&nbsp;&nbsp;浏览量：${pageviews}次</p>
					<div class="wenzi">
<#--						<span style="font-size:16px;">虽然是日式茶饮品牌，<span style="color:#E53333;"><strong>汴京茶寮</strong></span>也在追求丰富和全面，汴京茶寮颠覆了人们对传统奶茶的印象，以爽口、地道、时尚等特点征服消费者。同时，汴京茶寮还具有一定的文化底蕴，将日式茶饮文化以及欧洲的咖啡牛奶文化进行了有机合理的结合，打造出汴京茶寮专属的茶饮文化，缔造出一个前所未有的茶饮品牌。汴京茶寮加盟项目，让你安心做老板!</span><br />-->
<#--<br />-->
<#--<span style="font-size:16px;">汴京茶寮不仅产品质量好，而且系列产品多。让投资者加盟以后，轻松打开茶饮文化市场。<strong><span style="color:#E53333;">汴京茶寮</span></strong>是一个复合式经营的品牌，加盟了它之后你会发现你的产品经营范围相当广，不仅有汴京茶寮、茶饮、甜品等等一站式的有吃有喝的消费模式，能够让顾客在品牌加盟店畅快选择自己喜爱的口味。</span><br />-->
<#--<br />-->
<#--<p>-->
<#--	<span style="font-size:16px;">在发展经营中，汴京茶寮加盟总部还是一家集加盟、研发培训、物料配送、销售管理为一体的新时代餐饮管理企业。<strong><span style="color:#E53333;">汴京茶寮</span></strong>加盟总部以传承饮食文化，促进行业创新发展为使命，以专业服务成就专业品牌为总汇，致力于为餐饮企业提供专业餐饮品牌管理、餐饮店品牌策划、技术培训、产品研发、设计、原物料供应等服务。全面而周到的服务为投资者提供了更多选择和更全面的合作扶持，吸引了众多投资者的加盟，开启了汴京茶寮的致富奇迹。</span>-->
<#--</p>-->
<#--					</div>-->
						${content}

					<p class="on_article">上一篇：<a href="${upUrl}">${upTitle}</a></p>
					<p class="next_article">下一篇：<a href="${downUrl}">${downTitle}</a></p>

				</div>
			</div>
			<!--底部-->
			<div class="footer">
				<div class="container">
					<div class="footer_box">
						<ul class="list-inline clearfix">
														<li><a href="/" >首页</a></li>
														<li><a href="/about.html" >关于我们</a></li>
														<li><a href="/products/page_1.html" >产品中心</a></li>
														<li><a href="/shop.html" >门店展示</a></li>
														<li><a href="/news/page_1.html" >新闻动态</a></li>
														<li><a href="/contact.html" >联系我们</a></li>
														<li><a href="/sitemap.html">网站地图</a></li>
						</ul>
					</div>
					<p class="text-center ban">@2017-2019 汴京茶寮官方网站 板权所有</p>
					<p class="text-center">地址：www.baidecy.com</p>
							                <p class="text-center">
			                友情链接：
			                        			                   <a href="http://www.jianbingwangjm.cn/" target="_blank" style="color: #fff;">太土鸡蛋煎饼</a>
			                        			                   <a href="http://www.baidecy.com/" target="_blank" style="color: #fff;">广州佰德餐饮公司</a>
			                        			                   <a href="http://www.yizsnn.cn/" target="_blank" style="color: #fff;">一只酸奶牛加盟</a>
			                        			                   <a href="http://www.nanchanvcha.com" target="_blank" style="color: #fff;">男茶女茶</a>
			                        			                   <a href="http://www.wutiaoren.com.cn" target="_blank" style="color: #fff;">五条人糖水铺加盟</a>
			                        			                   <a href="http://www.liumangtutea.com" target="_blank" style="color: #fff;">流氓兔奶茶官网</a>
			                        			                   <a href="http://www.exp-cn.net/" target="_blank" style="color: #fff;">信息周刊</a>
			                        		                 </p>
		            				</div>
			</div>
					</div>
		<script type="text/javascript" src="/js/jquery.min.js"></script>
		<script type="text/javascript" src="/js/js/clearUc.js"></script>
		<script type="text/javascript" src="/js/js/respond-1.4.2.js" ></script>
		<script type="text/javascript" src="/js/bootstrap.min.js" ></script>
		<script type="text/javascript" src="/js/wow.js" ></script>
		<script type="text/javascript">
			$(function(){
				//导航栏样式选中
				$('.nav li a').eq( 4 ).addClass("active");
			});
			new WOW().init();
		</script>
	</body>
</html>
