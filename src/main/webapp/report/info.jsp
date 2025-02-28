<%--
  Created by IntelliJ IDEA.
  User: MyraZ
  Date: 2024/12/21
  Time: 17:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-signal"></span>&nbsp;数据报表 </div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <%-- 柱状图的容器 --%>
                <div class="col-md-12">
                    <div id="monthChart" style="width: 100%;height: 500px"></div>
                </div>=
                <%-- 百度地图的加载 --%>
                <h3 align="center">用户地区分布图</h3>
                <%-- 百度地图的容器 --%>
                <div id="baiduMap" style="width: 100%;height: 600px"></div>
            </div>
        </div>
    </div>
</div>
<%--
    关于Echarts报表的使用
        1. 下载Echarts的依赖 （JS文件）
        2. 在需要使用的页面引入Echarts的JS文件
        3. 为 ECharts 准备一个具备高宽的 DOM 容器
        4. 通过 echarts.init 方法初始化一个 echarts 实例并通过 setOption 方法生成一个报表
 --%>
<script type="text/javascript" src="statics/echarts/echarts.min.js"></script>
<%-- 引用百度地图API文件，需要申请百度地图对应ak密钥--%>
<script type="text/javascript" src="https://api.map.baidu.com/api?v=1.0&&type=webgl&ak=UFZV5Y0DKbStSZWcdbtwWN26MxC3dbl7"></script>
<script type="text/javascript">
    /**
     * 通过月份查询对应的云记数量
     */
    $.ajax({
        type:'get',
        url:'report',
        data:{
            actionName:"month"
        },
        success:function (result){
            if (result.code==1){
                //得到月份（得到X轴的数据）
                var monthArray=result.result.monthArray;
                //得到月份对应的云记数量 （得到Y轴的数据）
                var dataArray=result.result.dataArray;
                //加载柱状图
                loadMonthChart(monthArray,dataArray);
            }
        }
    });

    /**
     * 加载柱状图
     */
    function loadMonthChart(monthArray,dataArray){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('monthChart'));


        //指定图表的配置项和数据
        let dataAxis = monthArray;
        let data = dataArray;
        let yMax = 30;
        let dataShadow = [];
        for (let i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }
        var option = {
            //标题
            title: {
                text: '按月统计',//主标题
                subtext: '通过月份查询云记数量',//副标题
                left:'center'//标题对齐方式
            },
            //提示框
            tooltip:{},
            //x轴
            xAxis: {
                data: dataAxis,
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                }
            },
            //y轴
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    color: '#999'
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            series: [
                {
                    type: 'bar',//柱状图
                    data: data,//y轴的数据
                    showBackground: true,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#83bff6' },
                            { offset: 0.5, color: '#188df0' },
                            { offset: 1, color: '#188df0' }
                        ])
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                { offset: 0, color: '#2378f7' },
                                { offset: 0.7, color: '#2378f7' },
                                { offset: 1, color: '#83bff6' }
                            ])
                        }
                    }

                }
            ]
        };


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    /**
     * 通过用户发布的坐标查询云记
     */
    $.ajax({
        type:"get",
        url: "report",
        data:{
            actionName: "location"
        },
        success:function (result){
            console.log(result)
            if (result.code==1){
                //加载百度地图
                loadBaiduMap(result.result)
            }
        }
    })

    /**
     * 加载百度地图
     */
    function loadBaiduMap(markers){
        // 加载地图实例
        var map=new BMapGL.Map("baiduMap");

        //设置地图的中心点
        var point = new BMapGL.Point(116.404, 39.915);

        //地图初始化，BMapGL.Map.centerAndZoom()方法要求设置中心点坐标和地图级别
        map.centerAndZoom(point, 10);
        //开启鼠标滚轮缩放
        map.enableScrollWheelZoom(true);
        // 添加缩放控件
        var zoomCtrl = new BMapGL.ZoomControl();
        map.addControl(zoomCtrl);

        //判断是否有坐标
        if (markers!=null&&markers.length>0){// 集合中第一个坐标时用户当前所在位置，其他的事云记记录中
            //将用户所在的位置设置为中心点
            map.centerAndZoom(new BMapGL.Point(markers[0].lon, markers[0].lat),10);
            // 循环在地图上添加点
            for (var i=1;i<markers.length;i++){
                // 创建点标记
                var marker = new BMapGL.Marker(new BMapGL.Point(markers[i].lon, markers[i].lat));
                // 在地图上添加点标记
                map.addOverlay(marker);
            }
        }


    }
</script>

