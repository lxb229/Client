/**
 * 实时数据 echarts
 * Created by nbin on 2017/8/7.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    /*初始化echart*/
     var owc = echarts.init(document.getElementById('owc'));


     var labelTop = {
        normal: {
            label: {
                show: true,
                // position: 'center',
                formatter: '{b}',
                textStyle: {
                    baseline: 'bottom'
                }
            },
            labelLine: {
                show: false
            }
        }
    };
   /* var labelFromatter = {
        normal: {
            label: {
                formatter: function(params) {
                    return 100 - params.value + '%'
                },
                textStyle: {
                    baseline: 'top'
                }
            }
        }
    };*/
    var labelBottom = {
        normal: {
            color: '#ccc',
            label: {
                show: true,
                position: 'center'
            },
            labelLine: {
                show: false
            }
        },
        emphasis: {
            color: 'rgba(0,0,0,0)'
        }
    };
    var radius = [40, 55];
    
     /*初始option*/
    var templateChartOption =  {
            
        legend: {
            x: 'center',
            y: 'center',
            data: [
                '新增注册', '新增玩家', '活跃玩家', '充值人数', '充值金额', 'ARRPU', 'ARPU', '付费率', '次日留存率','7日留存率'
            ]
        },
        title: {
            text: '今日实时数据',
            x: 'center'
        },
        series: [{
            type: 'pie',
            center: ['10%', '30%'],
            radius: [40, 55],
            x: '0%', // for funnel
          
            data: [{
                name: 'other',
                value: 75,
                itemStyle: labelBottom
            }, {
                name: '新增注册',
                value: 25,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['30%', '30%'],
            radius: [40, 55],
            x: '20%', // for funnel
           
            data: [{
                name: 'other',
                value: 76,
                itemStyle: labelBottom
            }, {
                name: '新增玩家',
                value: 24,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['50%', '30%'],
            radius: [40, 55],
            x: '40%', // for funnel
          
            data: [{
                name: 'other',
                value: 86,
                itemStyle: labelBottom
            }, {
                name: '活跃玩家',
                value: 14,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['70%', '30%'],
            radius: [40, 55],
            x: '60%', // for funnel
          
            data: [{
                name: 'other',
                value: 89,
                itemStyle: labelBottom
            }, {
                name: '充值人数',
                value: 11,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['90%', '30%'],
            radius: [40, 55],
            x: '80%', // for funnel
          
            data: [{
                name: 'other',
                value: 73,
                itemStyle: labelBottom
            }, {
                name: '充值金额',
                value: 27,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['10%', '70%'],
            radius: [40, 55],
            y: '55%', // for funnel
            x: '0%', // for funnel
          
            data: [{
                name: 'other',
                value: 85,
                itemStyle: labelBottom
            }, {
                name: 'ARPPU',
                value: 15,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['30%', '70%'],
            radius: [40, 55],
            y: '55%', // for funnel
            x: '20%', // for funnel
          
            data: [{
                name: 'other',
                value: 46,
                itemStyle: labelBottom
            }, {
                name: 'ARPU',
                value: 54,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['50%', '70%'],
            radius: [40, 55],
            y: '55%', // for funnel
            x: '40%', // for funnel
          
            data: [{
                name: 'other',
                value: 74,
                itemStyle: labelBottom
            }, {
                name: '付费率',
                value: 26,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['70%', '70%'],
            radius: [40, 55],
            y: '55%', // for funnel
            x: '60%', // for funnel
          
            data: [{
                name: 'other',
                value: 75,
                itemStyle: labelBottom
            }, {
                name: '次日留存率',
                value: 25,
                itemStyle: labelTop
            }]
        }, {
            type: 'pie',
            center: ['90%', '70%'],
            radius: [40, 55],
            y: '55%', // for funnel
            x: '80%', // for funnel
          
            data: [{
                name: 'other',
                value: 72,
                itemStyle: labelBottom
            }, {
                name: '7日留存率',
                value: 28,
                itemStyle: labelTop
            }]
        }]
    };  

    
    /*init初始化页面图表*/
    $(function () {
         layer.load(0, {shade: false});
         $.get("/oper/realTime/queryEcharts", function (rs) {
            layer.closeAll('loading');
            renderer_echarts(rs);
         }, "json");
    });

    /*渲染实时数据图表*/
    function renderer_echarts(rs) {
        
        var echarts_date;
        if(rs.code!='00000' || typeof rs.data === "undefined"){
            AlertMsg(rs.msg);
            echarts_date = templateChartOption;
        } 
        var echarts_date = eval('(' + rs.data + ')');

        // templateChartOption.legend.data  = echarts_date.legend.data;

        if (echarts_date.seriesKvData.length > 1) {
            for (var i = 0 ; i < echarts_date.seriesKvData.length ;  i++) {
                for(var j in echarts_date.seriesKvData[i]){
                    templateChartOption.series[i].data[j].name = echarts_date.seriesKvData[i][j].name;
                     templateChartOption.series[i].data[j].value = echarts_date.seriesKvData[i][j].objValue;
                }

               // templateChartOption.series[i] = echarts_date.series[i];
            }
        }

        owc.setOption(templateChartOption);
    };
    

});
