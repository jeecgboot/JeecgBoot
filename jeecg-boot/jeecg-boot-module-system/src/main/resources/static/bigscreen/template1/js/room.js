//计划完成表的当前所选
var indexnum = 0;
var color=['#F35331','#2499F8','#3DF098','#33B734'];
var fontColor='#FFF';

//定义进度条组件和属性
var y_gauge1 =null;
var y_gauge2 =null;
var y_gauge3 =null;
var y_gauge4 =null;
var m_gauge1 =null;
var option_Progress =null;

//订单情况螺旋图
var orderStatus=null;
var orderStatus_option =null;

//定义仪表盘组件和属性
var gauge1 =null;
var gauge2 =null;
var gauge3 =null;
var gauge4 =null;
var gauge5 =null;
var option_gauge =null;

//产品饼图组件和属性
var productPie=null;
var productPie_option=null;

//业务进展图组件和属性
var businessProgress=null;
var businessProgress_placeHoledStyle = null;
var businessProgress_dataStyle =null;
var businessProgress_option=null;

//生产质量堆积图组件和属性
var quality_chart = null;
var quality_option=null;

//词云组件和属性
var wordCloud= null;
var wordCloud_option=null;

//生产计划折线图组件和属性
var plan_chart = null;
var plan_option=null;

//环形图的风格定义
var dataStyle = {
	normal: {
		label: {show:false},
		labelLine: {show:false}
	}
};
var placeHolderStyle = {
	normal : {
		color: 'rgba(0,0,0,0.1)',
		label: {show:false},
		labelLine: {show:false}
	},
	emphasis : {
		color: 'rgba(0,0,0,0)'
	}
};

//最大订单号
var lastOrderNumber=1;

$(document).ready(function ()
{	
	//环形进度条设置对象	
	option_Progress={
		title : {
			text: '目前进度',
			subtext: '50%',
			x: 'center',
			y: 90,
			itemGap: 10,
			textStyle : {
				color : '#B7E1FF',
				fontWeight: 'normal',
				fontFamily : '微软雅黑',
				fontSize : 24
			},
			subtextStyle:{
				color: '#B7E1FF',
				fontWeight: 'bolder',
				fontSize:24,
				fontFamily : '微软雅黑'
			}
		},
		series : [{
			type : 'pie',
			center : ['50%', '50%'],
			radius : [75,90],
			x: '0%',
			tooltip:{show:false},		
			data : [{
				name:'达成率', 
				value:79,
				itemStyle:{color :'rgba(0,153,255,0.8)'},
				hoverAnimation: false, 
				label : {
					show : false,
					position : 'center',
					textStyle: {						
						fontFamily:'微软雅黑',
						fontWeight: 'bolder',
						color:'#B7E1FF',
						fontSize:24
					}
				},
				labelLine : {
					show : false
				}
			},
			{
				name:'79%',
				value:21,
				itemStyle:{color: 'rgba(0,153,255,0.1)'},
				hoverAnimation: false, 
				label : {
					show : false,
					position : 'center',
					padding:20,		
					textStyle: {
						fontFamily:'微软雅黑',
						fontSize: 24,
						color:'#B7E1FF'
					}
				},
				labelLine : {
					show : false
				}
			}]
		},
		{
			type : 'pie',
			center : ['50%', '50%'],
			radius : [95,100],
			x: '0%',
			hoverAnimation: false, 
			data : [{
				value:100,
				itemStyle:{color :'rgba(0,153,255,0.3)'},
				label : {show : false},
				labelLine : {show : false}
			}]	
		},
		{
			type : 'pie',
			center : ['50%', '50%'],
			radius : [69,70],
			x: '0%',
			hoverAnimation: false, 
			data : [{
				value:100,
				itemStyle:{color :'rgba(0,153,255,0.3)'},
				label : {show : false},
				labelLine : {show : false}
			}]	
		}]
	};	
	
	//年仪表盘
	y_gauge1 = echarts.init(document.getElementById('y_gauge1'));
	y_gauge2 = echarts.init(document.getElementById('y_gauge2'));
	y_gauge3 = echarts.init(document.getElementById('y_gauge3'));
	y_gauge4 = echarts.init(document.getElementById('y_gauge4'));
	
	//订单完成情况螺旋图
	var yearPlanData=[];
	var yearOrderData=[];
	var differenceData=[];
	var visibityData=[];
	var xAxisData=[];
	
	for(var i=0;i<12;i++)
	{
		yearPlanData.push(Math.round(Math.random()*900)+100);
		yearOrderData.push(Math.round(Math.random()*yearPlanData[i]));
		differenceData.push(yearPlanData[i]-yearOrderData[i]);
		visibityData.push(yearOrderData[i]);
		xAxisData.push((i+1).toString()+"月");
	}
	orderStatus= echarts.init(document.getElementById('orderStatus'));
	orderStatus_option={
		title :{show:false},
		tooltip : {
			trigger: 'axis',
			formatter: function (params){
				return params[0].name + '<br/>'
					   + params[0].seriesName + ' : ' + params[0].value + '<br/>'
					   + params[1].seriesName + ' : ' + params[1].value + '<br/>'
					   +'完成率：'
					   + (params[0].value > 0 ? (params[1].value/params[0].value*100).toFixed(2)+'%' : '-') 
					   + '<br/>'
			},
			textStyle: {
				color: '#FFF',
				fontSize:24
			}
		},
		toolbox: {show:false},
		legend:{
			top: 'top',
			textStyle: {
				color: '#B7E2FF',
				fontSize:24,
				fontFamily:'微软雅黑'
			},
			data:['计划生产','已接订单']
		},
		xAxis: {
			data: xAxisData,
			axisLabel: {
				textStyle: {
					color: '#B7E1FF',
					fontSize:24
				}
			},
			axisLine:{
				lineStyle:{
					color:'#09F'	
				}
			},
			axisTick:{
				lineStyle:{
					color:'#09F'	
				}
			}
		},
		yAxis: {
			inverse: false,
			splitArea: {show: false},
			axisLine:  {show: false},
			axisTick:  {show: false},
			axisLabel: {
				textStyle: {
					color: '#B7E1FF',
					fontSize:24,
					fontFamily:'Arial',
				}
			},
			splitLine :{
				lineStyle:{
					color:'#09F'	
				}
			}
		},
		grid: {
			left: 100
		},
		series : [{
				name:'计划生产',
				type:'line',
				smooth :true,
				symbol: 'circle',
				symbolSize: 10,
				showAllSymbol : true,
				color:color[1],
				data:yearPlanData
			},
			{
				name:'已接订单',
				type:'line',
				smooth :true,
				symbol: 'circle',
				symbolSize: 10,
				showAllSymbol : true,
				color:'#F90',
				itemStyle:{					
					normal:{
					  lineStyle: {
						width:2
					  }
					}
				},
				data:yearOrderData
			},
			{
				name:'不可见',
				type:'bar',
				stack: '1',
				barWidth: 1,
				itemStyle:{
					normal:{
						color:'rgba(0,0,0,0)'
					},
					emphasis:{
						color:'rgba(0,0,0,0)'
					}
				},
				data:visibityData
			},
			{
				name:'变化',
				type:'bar',
				stack: '1',
				barWidth: 1,
				color:'#B7E1FF',
				data:differenceData
			}
		]
	}
	orderStatus.setOption(orderStatus_option);
	
	//产品销售的环形图
	var productLegend=[['轿车1','轿车2','轿车3','轿车4','轿车5','轿车6','轿车7','轿车8','轿车9'],['SUV1','SUV2','SUV3','SUV4'],['卡车1','卡车2','卡车3','卡车4','卡车5','卡车6']];
	var productClassLegend=['轿车','SUV','卡车'];
	var productClassColor=['rgba(255,153,0,','rgba(153,204,102,','rgba(0,102,255,'];
	var productClassData=[];
	var productData=[];
	var productColor=[];
	for(var i=0;i<productClassLegend.length;i++)
	{	
		var total=0;
		for(var j=0;j<productLegend[i].length;j++)
		{
			var n=Math.round(Math.random()*100)+1;
			productData.push({name:productLegend[i][j],value:n});
			total+=n;
		}
		for(var j=0;j<productLegend[i].length;j++)
		{		
			productColor.push(productClassColor[i]+(1.0-productData[j].value/total).toFixed(2)+")");
		}
		productClassData.push({name:productClassLegend[i],value:total});
	}
	
	productPie=echarts.init(document.getElementById('productPie'));
	productPie_option={
		title : {
			text: '销售额',			
			x: 'center',
			y: 'center',
			itemGap: 10,
			textStyle : {
				color : '#09F',
				fontWeight: 'normal',
				fontFamily : '微软雅黑',
				fontSize : 32
			}
		},
		calculable : false,
		tooltip : {
			trigger: 'item',
			textStyle: {
				color:'#FFF',
				fontSize:24
			},
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		},
		series : [
			{
				name:'类别',
				type:'pie',
				selectedMode: 'single',
				radius : ['20%','40%'],				
				width: '40%',
				funnelAlign: 'right',
				itemStyle : {
					normal : {
						color:function(d)
						{
							return productClassColor[d.dataIndex]+'1)';
						},
						borderColor:'#032749',
						label : {
							position : 'inner',							
							fontSize:28,
						},
						labelLine : {
							show : false
						}
					}
				},
				data:productClassData
			},
			{
				name:'车型',
				type:'pie',
				radius : ['40%','70%'],				
				width: '35%',
				funnelAlign: 'left',
				itemStyle : {					
					normal : {
						color:function(d)
						{
							return productColor[d.dataIndex];
						},
						borderColor:'#032749',
						label : {
							color:'#B7E1FF',						
							fontSize:24
						}
					}
				},			
				data:productData
			}
		]
	};
	productPie.setOption(productPie_option);
	
	//业务进展图
	businessProgress=echarts.init(document.getElementById('businessProgress'));	
	businessProgress_placeHoledStyle = {
		normal:{
			barBorderColor:'rgba(0,0,0,0)',
			color:'rgba(0,0,0,0)'
		},
		emphasis:{
			barBorderColor:'rgba(0,0,0,0)',
			color:'rgba(0,0,0,0)'
		}
	};
	businessProgress_dataStyle = { 
		normal: {
			barBorderColor:'rgba(0,102,255,1)',
			color:function(d){
				return 'rgba(0,102,255,0.3)';
			},
			label : {
				show: true,
				position: 'insideLeft',
				formatter: '{c}%',
				textStyle: {						
					fontFamily:'Arial',
					fontWeight: 'bolder',
					color:'#B7E1FF',
					fontSize:24
				}
			}
		}
	};
	
	businessProgress_option = {
		title: {show:false},
		tooltip : {
			trigger: 'axis',
			axisPointer : {
				type : 'shadow'
			},
			textStyle: {
				color:'#FFF',
				fontSize:24
			},
			formatter : '{b}<br/>{a0}:{c0}%<br/>{a2}:{c2}%<br/>{a4}:{c4}%<br/>{a6}:{c6}%'
		},
		legend: {			
			itemGap : 60,
			top:'top',
			textStyle: {						
				fontFamily:'微软雅黑',
				fontWeight: 'bolder',
				color:'#B7E1FF',
				fontSize:24
			},
			data:['项目投标', '投标进度','项目进行', '项目交付']
		},
		toolbox: {show : false},
		grid: {
			left: 150
		},
		xAxis : [
			{
				type : 'value',
				position: 'top',
				axisLine:{
					lineStyle:{color:'#09F'}
				},
				splitLine :{
					lineStyle:{color:'#09F'	}
				},
				axisLabel: {show: false},
			}
		],
		yAxis : [
			{
				type : 'category',
				data : ['重大业务1', '重大业务2', '重大业务3', '重大业务4'],
				axisLabel: {
					textStyle: {
						color: '#B7E1FF',
						fontSize:24
					}
				},
				axisLine:{
					lineStyle:{
						color:'#09F'	
					}
				},
				splitLine :{
					lineStyle:{color:'#09F'	}
				}
			}
		],
		series : [
			{
				name:'项目投标',
				type:'bar',
				stack: '进度',
				itemStyle : businessProgress_dataStyle,
				data:[100, 100, 100, 70]
			},
			{
				name:'项目投标',
				type:'bar',
				stack: '进度',
				itemStyle: businessProgress_placeHoledStyle,
				data:[0, 0, 0, 30]
			},
			{
				name:'投标进度',
				type:'bar',
				stack: '进度',
				itemStyle : businessProgress_dataStyle,
				data:[100, 100, 42, 0]
			},
			{
				name:'投标进度',
				type:'bar',
				stack: '进度',
				itemStyle: businessProgress_placeHoledStyle,
				data:[0, 0, 58, 100]
			},
			{
				name:'项目进行',
				type:'bar',
				stack: '进度',
				itemStyle : businessProgress_dataStyle,
				data:[100, 100, 0, 0]
			},
			{
				name:'项目进行',
				type:'bar',
				stack: '进度',
				itemStyle: businessProgress_placeHoledStyle,
				data:[0, 0, 100, 100]
			},
			{
				name:'项目交付',
				type:'bar',
				stack: '进度',
				itemStyle : businessProgress_dataStyle,
				data:[71, 50, 0, 0]
			},
			{
				name:'项目交付',
				type:'bar',
				stack: '进度',
				itemStyle: businessProgress_placeHoledStyle,
				data:[29, 50, 100, 100]
			}
		]
	};
	businessProgress.setOption(businessProgress_option);
	
	
	//监控仪表盘
	/*option_gauge = {		
		title: {
			text: '', //标题文本内容
		},
		toolbox: { //可视化的工具箱
			show: false,                
		},
		tooltip: { //弹窗组件
			formatter: "{a} <br/>{b} : {c}%"
		},			
		series: [{          
			type: 'gauge',
			axisLine: {// 坐标轴线
				lineStyle: { // 属性lineStyle控制线条样式
					color:[[0.2, color[0]],[0.8, color[1]],[1, color[0]]],
					width: 18
				 }
			},				 
			splitLine: { // 分隔线
					show:true,
					length: 18,
					lineStyle: {                            
						color: '#28292D',
						width: 1
					}
				},
			axisTick : { //刻度线样式（及短线样式）
				show:false,
				lineStyle: {                    
						color: 'auto',
						width: 1
					},
				length : 20
			},
			axisLabel : {
				color:'#FFF',
				fontSize:14,
				fontFamily:'Verdana, Geneva, sans-serif'
			},
			title: {					
					textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						fontWeight: 'bolder',
						fontSize: 20,                          
						color: '#FFF'
					},
					offsetCenter: [0, '30%']
				},
			pointer: {
					width: 5,                     
					color: '#F00',
					shadowColor: '#FF0',
					shadowBlur: 10
				},
			detail: {
				show:false,
				formatter:'{value}%',
				textStyle: 
				{
					fontFamily:'Arial',
					color: '#000',
					fontSize:'32px'						
				},
				offsetCenter: [0, '90%']
			},
			data: [{value: 45, name: '水'}]
		}]
     };
		
	gauge1 = echarts.init(document.getElementById('gauge1'));
	gauge2 = echarts.init(document.getElementById('gauge2'));
	gauge3 = echarts.init(document.getElementById('gauge3'));
	gauge4 = echarts.init(document.getElementById('gauge4'));	
	gauge5 = echarts.init(document.getElementById('gauge5'));
	option_gauge.series[0].axisLine.lineStyle.color=[[0.2, color[0]],[0.8, color[1]],[1, color[2]]];
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="水";
	$('#vg1').html(option_gauge.series[0].data[0].value);
	gauge1.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="电";
	$('#vg2').html(option_gauge.series[0].data[0].value);
	gauge2.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="天然气";
	$('#vg3').html(option_gauge.series[0].data[0].value);
	gauge3.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="压缩空气";
	$('#vg4').html(option_gauge.series[0].data[0].value);
	gauge4.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="蒸汽";
	$('#vg5').html(option_gauge.series[0].data[0].value);
	gauge5.setOption(option_gauge);*/
	
	//生产质量堆积图
	quality_chart = echarts.init(document.getElementById('quality'));
	quality_option={
		title: {			
			show:false,
			text: 'AUDIT',
			left: 'center',
			textStyle: {
				color: '#F00',
				fontSize:32
			}
		},
		xAxis: {
			data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
			axisLabel: {
				textStyle: {
					color: '#B7E1FF',
					fontSize:24
				}
			},
			axisLine:{
				lineStyle:{
					color:'#09F'	
				}
			},
			axisTick:{
				lineStyle:{
					color:'#09F'	
				}
			}
		},
		yAxis: {
			inverse: false,
			splitArea: {show: false},
			axisLine:  {show: false},
			axisTick:  {show: false},
			axisLabel: {
				textStyle: {
					color: '#B7E1FF',
					fontSize:24,
					fontFamily:'Arial',
				}
			},
			splitLine :{
				lineStyle:{
					color:'#09F'	
				}
			}
		},
		grid: {
			left: 100
		},
		tooltip: {
			trigger: 'item',
			textStyle: {
				color: '#B7E1FF',
				fontSize:24
			}
		},
		legend:{
			show:false,
			top: 'bottom',
			textStyle: {
				color: '#F00',
				fontSize:24,
				fontFamily:'微软雅黑'
			},
			data:['AUDIT分数1','AUDIT分数']
		},
		series: [
			{
				name: 'AUDIT分数1',
				type: 'bar',
				stack: 'one',
				itemStyle: 
				{
					normal: {color: color[1]}
				},
				barWidth : 60,
				data:[2200,2900,3680,2200,2900,3680,2200,2900,3680,2200,2900,3680]
			},
			{
				name: 'AUDIT分数',
				type: 'bar',
				stack: 'one',
				itemStyle: {
					normal: {
						color: '#F90',
						label: {
							 show: true,
							 position: 'insideTop',
							 textStyle: {
								 color: '#000',
								 fontSize:24
							 }
						 }
					}
				},
				barWidth : 50,
				data: [1800,1100,320,1800,1100,320,1800,1100,320,1800,1100,320]
			}
		]
	};
	quality_chart.setOption(quality_option);
	
	//生产计划折线图
	var plan_data1=[];
	var plan_data2=[];
	var plan_xAxis=[];
	for (var i = 1; i <= 12; i++) {
		plan_xAxis.push(i+"月");
		plan_data1.push(Math.round(Math.random() * 100));
		plan_data2.push(Math.round(Math.random() * 100));
	}
	plan_chart = echarts.init(document.getElementById('plan'));
	plan_option={		
		xAxis: {
			data:plan_xAxis,
			axisLabel: {
				textStyle: {
					color: '#B7E1FF',
					fontSize:24
				}
			},
			axisLine:{
				lineStyle:{
					color:'#09F'	
				}
			},
			axisTick:{
				lineStyle:{
					color:'#09F'	
				}
			}
		},
		yAxis: {			
			inverse: false,
			splitArea: {show: false},
			axisLine:  {show: false},
			axisTick:  {show: false},
			axisLabel: {
				textStyle: {
					color: '#B7E1FF',
					fontSize:24,
					fontFamily:'Arial',
				}
			},
			splitLine :{
				lineStyle:{
					color:'#09F'	
				}
			}
		},
		tooltip: {
			trigger: 'axis',
			textStyle: {
				color: '#FFF',
				fontSize:24
			}
		},
		grid: {
			left: 100
		},
		legend:{
			show:false,
			top: 'bottom',
			textStyle: {
				color: '#F00',
				fontSize:24
			},			
			data:['计划完成数','实际完成数']
		},
		series: [
			{
				name: '计划完成数',
				type: 'bar',
				itemStyle: 
				{
					normal: {color: color[1]},
					emphasis: {color: color[2]}
				},
				barWidth : 40,
				data:plan_data1
			},
			{
				name: '实际完成数',
				type: 'line',
				itemStyle: {
					normal: {
						color: '#F90',
						label: {
							 show: true,
							 position: 'top',
							 textStyle: {
								 color: '#CCC',
								 fontSize:24
							 }
						},
						lineStyle:{
							color:'#F90',
							width:4
						}				 
					},
					emphasis: {
						color: '#FF0'
					}	
				},			
				symbolSize: 24,
				data: plan_data2
			}
		]
	};
	plan_chart.setOption(plan_option);
	
	//轮番显示tips
	/*function clock(){
	  showToolTip_highlight(plan_chart);	  
	}
	setInterval(clock, 5000);*/
	
	//词云
	var cloudData=[];
	for(var i=0;i<30;i++)
	{
		cloudData.push({name:'文字云字符'+i.toString(),value:Math.random()*1000});
	}
	wordCloud=echarts.init(document.getElementById('wordCloud'));
	wordCloud_option={
		left: 'center',
        top: 'center',        
		tooltip: {
			textStyle: {
				color: '#FFF',
				fontSize:24
			}},
		series : [{  
            type : 'wordCloud',  
            shape:'smooth',  
			drawOutOfBound: true,
            gridSize : 10,  
            sizeRange : [ 10, 48 ],
			rotationRange: [0, 0],
            textStyle : {  
                normal : {  
                    color :function (d) {
						// Random color
						return 'rgba(0,153,255,'+(d.value/1000)+ ')';
					}
                },  
                emphasis : {  
                    shadowBlur : 10,  
                    shadowColor : '#333'  
                }  
            },  
            data : cloudData
        }]
	};
	
	wordCloud.setOption(wordCloud_option);
	
	//地图开始
	var map_chart = echarts.init(document.getElementById('map'));
	/*map_option = {			
		title : {show:false},
		tooltip: {
			show:function(d)
			{
				return (d.value!=null && d.value>=0);
			},
			trigger: 'item',
			formatter:function(d){
				return (d.value>=0)?d.name+'</br>战略力度：'+(d.value).toFixed(2):'';
			},
			textStyle: {
				color: '#FFF',
				fontSize:24
			}
		},
		legend: {
			show:false			
		},
		dataRange: {  
			show:false,
			min: 0,  
			max: 100,  
			text:['High','Low'],  
			realtime: false,  
			calculable : false,  
			color: ['rgba(0,51,204,0.8)','rgba(0,102,255,0.8)','rgba(0,153,255,0.8)'],
			splitList: [
				{start: 0,end: 30},
				{start: 31, end: 70},
				{start: 71, end: 100},
			]
		},		
		series: [{
			name: '布局',  
			type: 'map',  
			mapType: 'china',  
			roam: false, 
			showLegendSymbol : false,
			label: {
				show: true,
				textStyle: {
					 color: '#FFF',
					 fontSize:18
				 }
			},
			itemStyle :{
				areaColor :'rgba(0,0,0,0.2)',
				borderColor : '#09F'
			},
			emphasis:{				
				areaColor :'rgba(255,0,0,0.8)',
				borderColor : 'rgba(255,0,0,0.8)'
			},
			data:[
				{name: '北京', value:Math.random()*100},
				{name: '天津', value:Math.random()*100},
				{name: '上海', value:Math.random()*100},
				{name: '重庆', value:Math.random()*100},
				{name: '河北', value:Math.random()*100},
				{name: '河南', value:Math.random()*100},				
				{name: '辽宁', value:Math.random()*100},
				{name: '黑龙江', value:Math.random()*100},
				{name: '湖南', value:Math.random()*100},
				{name: '安徽', value:Math.random()*100},
				{name: '山东', value:Math.random()*100},				
				{name: '江苏', value:Math.random()*100},
				{name: '浙江', value:Math.random()*100},
				{name: '江西', value:Math.random()*100},
				{name: '湖北', value:Math.random()*100},
				{name: '广西', value:Math.random()*100},
				{name: '甘肃', value:Math.random()*100},
				{name: '山西', value:Math.random()*100},
				{name: '内蒙古', value:Math.random()*100},
				{name: '陕西', value:Math.random()*100},
				{name: '吉林', value:Math.random()*100},
				{name: '福建', value:Math.random()*100},
				{name: '贵州', value:Math.random()*100},
				{name: '广东', value:Math.random()*100},
				{name: '青海', value:Math.random()*100},				
				{name: '四川', value:Math.random()*100},
				{name: '宁夏', value:Math.random()*100},
				{name: '海南', value:Math.random()*100}
			]	
		}]
	};*/
	var mapData=[];
	for(key in geoCoordMap)
	{
		var geoCoord = geoCoordMap[key];
		mapData.push({name:key,value:geoCoord.concat((Math.random()*1000).toFixed(2))});
	}	

	map_option = {
		title : {show:false},
		tooltip : {
			trigger: 'item',
			formatter: function(params) {
                if (typeof(params.value)[2] == "undefined") {
                    return params.name + ' : ' + params.value;
                } else {
                    return params.name + ' : ' + params.value[2];
                }
            },
			textStyle: {
				color: '#FFF',
				fontSize:24
			}
		},
		legend: {
			show:false			
		},
		geo: {
			map: 'china',
			label: {
				normal: {show: false},
				emphasis: {show: false}
			},
			roam: false,
			itemStyle: {
				normal: {
					areaColor: 'rgba(0,153,255,0.6)',
					borderColor: '#09F'
				},
				emphasis: {
					areaColor: 'rgba(0,153,255,0.6)',
					borderColor: '#09F'
				}
			}
		},
		series: [{
			name: '战略布局点',
			type: 'scatter',
			coordinateSystem: 'geo',
			zlevel: 1,
			rippleEffect: {
				brushType: 'stroke'
			},			
			symbolSize: function (val) {
				return val[2] / 30;
			},
			label: {				
				normal: {show: false},
				emphasis: {show: false}
			},
			itemStyle: {
				normal: {color: 'rgba(255,255,0,0.8)'},
				emphasis: {color: 'rgba(246,33,87,1)'}
			},
			data: mapData
		},	
		{
			name: '战略布局TOP5',
			type: 'effectScatter',
			coordinateSystem: 'geo',
			zlevel: 2,
			hoverAnimation: true,
			showEffectOn: 'render',
			rippleEffect: {
				brushType: 'stroke'
			},
			label: {				
				normal: {
					show: true,
					fontFamily:'微软雅黑',
					fontSize: 24,
					color:'#FFF',
					formatter: '{b}',
					position: 'right',
					shadowBlur: 5,
                    shadowColor: '#000'					
				},
				emphasis: {
					show: true
				}
			},			
			symbolSize: function (val) {
				return val[2] / 30;
			},
			itemStyle: {
				normal: {
					color: 'rgba(255,255,255,1)'
				}
			},
			data: mapData.sort(function(a, b) {
                    return b.value[2] - a.value[2];
                }).slice(0, 5)
		},	
		{
			name: '点',
			type: 'scatter',
			coordinateSystem: 'geo',
			symbol: 'pin',
			symbolSize: function(val) {                    
				return val[2]/10;
			},
			label: {				
				normal: {
					show: true,
					fontFamily:'Arial, Helvetica, sans-serif',
					formatter:'{@[2]}',					
					textStyle: {
						color: '#FFF',
						fontSize: 18,
					}
				}
			},
			itemStyle: {
				normal: {
					color: 'rgba(246,33,87,1)', //标志颜色
				}
			},
			zlevel: 3,                
			data: mapData.sort(function(a, b) {
                    return b.value[2] - a.value[2];
                }).slice(0, 5)
		}]
	};
	
	map_chart.setOption(map_option, true);
	
	resresh();
	
	//开始定时刷新
	setInterval(resresh, 5*1000);
});

var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
        var dataItem = data[i];
        var fromCoord = geoCoordMap[dataItem[0].name];
        var toCoord = geoCoordMap[dataItem[1].name];
        if (fromCoord && toCoord) {
            res.push({
                fromName: dataItem[0].name,
                toName: dataItem[1].name,
                coords: [fromCoord, toCoord]
            });
        }
    }
    return res;
};

function showToolTip_highlight(mychart)
{  
  var echartObj = mychart;
  	  
  // 高亮当前图形
  var highlight =setInterval(function() 
  {
	  echartObj.dispatchAction({
		  type: 'highlight',
		  seriesIndex: 0,
		  dataIndex: indexnum
	  });
	  
	  echartObj.dispatchAction({
		  type: 'showTip',
		  seriesIndex: 0,
		  dataIndex: indexnum
	  });
	  clearInterval(highlight);
	  indexnum = indexnum + 1;
  	  if(indexnum>=7) indexnum=0;	  	  
  },1000);
}

//定时刷新数据
function resresh()
{
	var myDate = new Date();
	
	// $('#refresh').html("<img src=\"images/wait.gif\" align=\"absmiddle\"><span>数据刷新中...</span>");
	$('#currentDate').html(myDate.getFullYear()+"/"+insertZero(myDate.getMonth()+1)+"/"+insertZero(myDate.getDate()));	
	
	var maxg=Math.round(Math.random()*500)+400;
	var n1=Math.round(Math.random()*(maxg-100))+100;
	var n2=Math.round(Math.random()*(n1-50))+50;	
	var n3=(n2/maxg*100).toFixed(2);	
	
	//年进度条
	option_Progress.title.text ="计划生产";
	option_Progress.series[0].data[0].value = maxg;
	option_Progress.title.subtext =maxg+"台";
	option_Progress.series[0].data[1].value =0;
	y_gauge1.setOption(option_Progress);
	
	option_Progress.title.text ="已接订单";
	option_Progress.series[0].data[0].value = n1;
	option_Progress.title.subtext =n1+"台";
	option_Progress.series[0].data[1].value =(maxg-n1);
	y_gauge2.setOption(option_Progress);
	
	option_Progress.title.text ="已经完成";
	option_Progress.series[0].data[0].value = n2;
	option_Progress.title.subtext =n2+"台";
	option_Progress.series[0].data[1].value =(maxg-n2);
	y_gauge3.setOption(option_Progress);
	
	option_Progress.title.text ="计划完成率";
	option_Progress.series[0].data[0].value = n3;
	option_Progress.title.subtext =n3+"%";
	option_Progress.series[0].data[1].value =(100-n3);
	y_gauge4.setOption(option_Progress);
	
	//仪表盘刷新
	/*option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="水";
	$('#vg1').html(option_gauge.series[0].data[0].value);
	gauge1.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="电";
	$('#vg2').html(option_gauge.series[0].data[0].value);
	gauge2.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="天然气";
	$('#vg3').html(option_gauge.series[0].data[0].value);
	gauge3.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="压缩空气";
	$('#vg4').html(option_gauge.series[0].data[0].value);
	gauge4.setOption(option_gauge);
	option_gauge.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	option_gauge.series[0].data[0].name ="蒸汽";
	$('#vg5').html(option_gauge.series[0].data[0].value);
	gauge5.setOption(option_gauge);	*/
			
	//显示最后更新时间
	$('#refresh').html("<span id=\"refreshTime\">最后刷新时间："+myDate.toLocaleDateString()+" "+myDate.toLocaleTimeString()+"</span>");
}

//生成订单号
function getOrderNumber(n)
{
	var no="000000"+n.toString();
	return no.substring(no.length-6);
}

//前面补0
function insertZero(n)
{
	var no="000000"+n.toString();
	return no.substring(no.length-2);
}

//打开模态窗口
function openDialog(DlgName)
{		
	$('#'+DlgName).dialog('open');	
}