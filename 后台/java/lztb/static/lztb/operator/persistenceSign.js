/**
 * 留存分析 - 注册阶段分析
 * Created by nbin on 2017/08/21.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    var registrationPhase = echarts.init(document.getElementById('registrationPhase'));

    var registrationPhaseOption = {
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            borderWidth: 0,
            left: '3%',
            right: '4%',
            bottom: '5%',
            textStyle: {
                color: "#fff"
            }
        },
        legend: {
            data:['完成玩家','流失玩家','流失率']
        },
        xAxis: [
            {
                type: 'category',
                data:['激活','加载','新增注册','新增玩家','新增有效用户 ','游客角色']
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '玩家',
                interval: 100,
                axisLabel: {
                    formatter: '{value} '
                }
            },
            {
                type: 'value',
                name: '流失率',

                axisLabel: {
                    formatter: '{value} %'
                }
            }
        ],
        series: [
            {
                name:'完成玩家',
                type:'bar',
                barMaxWidth: 50, 
                "itemStyle": {
                    "normal": {
                        "barBorderRadius": 0, 
                        "color": "rgba(60,169,196,0.5)", 
                        "label": {
                            "show": true, 
                            "textStyle": {
                                "color": "rgba(0,0,0,1)"
                            }, 
                        }
                    }
                },
                data:[]
            },
            {
                name:'流失玩家',
                type:'bar',
                barMaxWidth: 50, 
                "itemStyle": {
                    "normal": {
                        "barBorderRadius": 0, 
                        "color": "rgba(51,204,112,1)", 
                        "label": {
                            "show": true, 
                            "textStyle": {
                                "color": "rgba(0,0,0,1)"
                            }, 
                        }
                    }
                },
                data:[]
            },
            {
                name:'流失率',
                type:'line',
                yAxisIndex: 1,
                data:[]
            }
        ]
    };


    /*渲染echart*/
    function registrationPhaseChart(rs){
        var echarts_date;
        console.log(rs)
        debugger;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
            echarts_date = eval('(' + rs.data + ')');
            for (var i = 0 ; i < echarts_date.seriesObj.length ;  i++) {
                    registrationPhaseOption.series[i].data = echarts_date.seriesObj[i];
            }
        }
      registrationPhase.setOption(registrationPhaseOption);
    }


    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {

          var registrationPhase_div = new Vue({
                el: "#registrationPhase_div",
                data: {
                filter: {
                    startDate: '',
                    endDate: '',
                },    
                grid: {
                    data: [] //当前页数据
                }
            },
            methods: {
                gridFristLoad: function () {
                    var options = this.filter;
                    $.post("/player/persistence/getOperationRemainRegisterList", options, function (rs) {
                        layer.closeAll('loading');
                        console.log(rs);
                         registrationPhase_div.grid.data = rs.data;
                        if(isEmpty(rs.data)){
                            AlertMsg("数据为空!");
                        }
                        
                    }, "json");
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/player/persistence/getOperationRemainRegisterExportExcel";
                    $("#registrationPhase_form").attr("action", url);
                    $('#registrationPhase_form').submit();
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/persistence/getOperationRemainRegisterEchart", options, function (rs) {
                        registrationPhaseChart(rs);
                    });
                }
              
            },
            created: function () {
                layer.load(0, {shade: false});
                this.gridFristLoad();
                this.SearchEchart();
            },
            mounted: function () {
                var vm = this; 
                var form = layui.form();
                var laydate = layui.laydate;
                var start = { max: laydate.now()};
                var end = { max: laydate.now()};

                document.getElementById('startDate_a').onclick = function(){
                  start.elem = this;
                  laydate(start);
                }
                document.getElementById('endDate_a').onclick = function(){
                  end.elem = this
                  laydate(end);
                }

                form.on('submit(registrationPhase_form)', function (data) {  
                    var vm = this;
                    registrationPhase_div.filter = data.field; 
                    registrationPhase_div.gridFristLoad();
                    registrationPhase_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();
                
            }

        });

    });


    

 });

