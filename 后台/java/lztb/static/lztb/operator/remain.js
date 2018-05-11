/**
 * 留存分析 - echart
 * Created by nbin on 2017/08/21.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    var daysLeft = echarts.init(document.getElementById('daysLeft'));

    var daysLeftoption = {
        tooltip: {
            trigger: 'axis'
        },
        color: ["#FF0000", "#00BFFF", "#FF00FF"],
        legend: {
            data: []
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
        },
        yAxis: [{
            type: 'value',
            axisLabel: {
                formatter: '{value} '
            },
        }],
        dataZoom: [{
            type: 'inside',
            start: 0,
            end: 50
        }, {
            show: true,
            type: 'slider',
            y: '90%',
            start: 0,
            end: 50
        }],
        series: [{
            name: '2日存留率',
            type: 'line',
            smooth: true,
            lineStyle: {
                normal: {
                    width: 2,
                }
            },
            data: []
        },
        {
            name: '3日存留率',
            type: 'line',
            smooth: true,
            lineStyle: {
                normal: {
                    width: 2,
                }
            },
            data: []
        }
        ,
        {
            name: '7日存留率',
            type: 'line',
            smooth: true,
            lineStyle: {
                normal: {
                    width: 2,
                }
            },
            data: []
        }
        ]
    };
    

    /*渲染echart*/
    function remainChart(rs){
        var echarts_date;
        console.log(rs)
        debugger;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
            echarts_date = eval('(' + rs.data + ')');
            daysLeftoption.legend.data = echarts_date.legend
            daysLeftoption.xAxis.data = echarts_date.xAxis
            for (var i = 0 ; i < echarts_date.seriesObj.length ;  i++) {
                    daysLeftoption.series[i].data = echarts_date.seriesObj[i];
            }
        }
      daysLeft.setOption(daysLeftoption);
    }


    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
         var remain_div = new Vue({
            el: "#remain_div",
            data: {
                filter: {
                    startDate: '',
                    endDate: '',
                }
            },
            methods: {
                 queryEcharts: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/persistence/getOperationRemainDayEchart", options, function (rs) {
                        layer.closeAll('loading');
                        remainChart(rs);                       
                    });

                }
            },
            created: function () {
                layer.load(0, {shade: false});
                this.queryEcharts();
            },
            mounted: function () {
                var vm = this; 
                var form = layui.form();
                var laydate = layui.laydate;
                var start = { max: laydate.now(-1)};
                var end = { max: laydate.now(-1)};
                form.render('select'); 

                document.getElementById('startDate').onclick = function(){
                  start.elem = this;
                  laydate(start);
                }
                document.getElementById('endDate').onclick = function(){
                  end.elem = this
                  laydate(end);
                }
                form.on('submit(remain_form)', function (data) {  
                    var vm = this;
                    remain_div.filter = data.field; 
                    remain_div.queryEcharts();
                    return false; //阻止原生表单跳转
                });
                form.render();
                
            }

        });



    });


 });

