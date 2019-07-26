<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="Keywords" content="奶茶店加盟|奶茶加盟|茶饮加盟|汴京茶寮|汴京茶寮加盟|汴京茶寮官网" />
		<meta name="Description" content="汴京茶寮运用花月诗酒茶的浪漫曲调，将一份日式的悠闲放置到其极具特色茶饮，帮助消费者缓解负面情绪减轻来自生活中的压力，汴京茶寮官网将这份感动复制到各个地方。" />
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
		<title>产品中心_汴京茶寮官网</title>
		<link rel="icon" type="image/x-ico" href="http://www.bianjc.cn/img/favicon.ico" />
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
					<a href="http://www.bianjc.cn/"><div class="logo animated fadeInDown"></div></a> 
					<div class="collapse navbar-collapse" id="example-navbar-collapse">
						<div class="container">
							<ul class="nav navbar-nav">
												       			<li  class="animated fadeInDown" >
									<a href="../index.html/" >首页</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="../about.html" >关于我们</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="../products/page_1.html" >产品中心</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="../shop.html" >门店展示</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="../news/page_1.html" >新闻动态</a>
					          	</li>
					          					       			<li  class="animated fadeInDown" >
									<a href="../contact.html" >联系我们</a>
					          	</li>
					          								</ul>
						</div>
					</div>
				</div>
			</nav>			<!--轮播图-->
			<div id="myCarousel" class="carousel slide">
			    <div class="carousel-inner">
			        <div class="item active">
			             <img src="http://www.bianjc.cn/img/banner/banner_common.jpg" class="img-responsive" />
			        </div>
			    </div>
			</div>
			
			<div class="content_box">
				<div class="container">
					<p class="navigation">当前位置：<a href="index.html">首页</a> &gt; 产品中心</p>
					<img class="center-block" src="http://www.bianjc.cn/img/img_04_03.png" />
					<p class="content_tit">产品中心</p>
					<div class="content_show">
						<ul class="list-inline clearfix">

							<#list productList as product >
							<li class="col-md-3 col-xs-6">
								<div>
									<img src="${product.img}" alt="${product.name}"  class="img-responsive wow lightSpeedIn" data-wow-duration="0.6s" data-wow-delay="0" data-wow-iteration="1"/>
									<p>${product.name}</p>
								</div>
							</li>
							</#list>
					    </ul>
					</div>
				</div>
			</div>
			<!--分页-->
			<div class="page">
				<div class="container">
					<ul class="list-inline clearfix" >
						<li ><a  href="${home}" title="首页">首页</a></li>
						<#if upPage?? >
							<li class="on" ><a  href="${upPage}" title="上一页"></a></li>
						</#if>
						<#list pagesList as page >
							<#if (page==current ) >
								<li><a class="current">${page}</a><li>
							<#else>
								<li><a href="${url}/product/page_${page}.html">${page}</a></li>
							</#if>
<#--							<li><a class="current">1</a><li>-->
						</#list>
						<#if downPage?? >
						<li class="next" ><a  href="${downPage}" title="下一页"></a></li>
						</#if>
						<li><a  href="${pages}" title="尾页">尾页</a></li>
					</ul>
				</div>
			</div>
			<!--底部-->
			<div class="footer">
				<div class="container">
					<div class="footer_box">
						<ul class="list-inline clearfix">
														<li><a href="../../index.html" >首页</a></li>
														<li><a href="../about.html" >关于我们</a></li>
														<li><a href="../products/page_1.html" >产品中心</a></li>
														<li><a href="../shop.html" >门店展示</a></li>
														<li><a href="../news/page_1.html" >新闻动态</a></li>
														<li><a href="../contact.html" >联系我们</a></li>
														<li><a href="sitemap.html">网站地图</a></li>
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
		<script type="text/javascript" src="http://www.bianjc.cn/js/jquery.min.js"></script>
		<script type="text/javascript" src="http://www.bianjc.cn/js/js/clearUc.js"></script>
		<script type="text/javascript" src="http://www.bianjc.cn/js/js/respond-1.4.2.js" ></script>
		<script type="text/javascript" src="http://www.bianjc.cn/js/bootstrap.min.js" ></script>
		<script type="text/javascript" src="http://www.bianjc.cn/js/wow.js" ></script>
		<script type="text/javascript">
			$(function(){
				//导航栏样式选中
				$('.nav li a').eq( 2 ).addClass("active");
			});
			new WOW().init();
		</script>
	</body>
</html>
