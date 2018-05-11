/**
 * 玩家分析 -渠道分布
 * Created by nbin on 2017/8/10.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {


    layui.config({dir: baseUrl + '../plugs/layui/'});

    layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var channel_div = new Vue({
            el: "#channel_div",
            data: {
                filter: {
                    startDate: '',
                    endDate: ''
                },    
                grid: {
                    data: [] 
                }
            },
            methods: {
                formatDate(time) {
                  var date = new Date(time);
                  return formatDate(date, 'yyyy-MM-dd');
                },
                gridFristLoad: function () {  
                    var options = this.filter;
                    $.post("/player/analysis/getOperationPlayerNewChannelList", options, function (rs) {
                        console.log(rs);
                        
                        if(isEmpty(rs.data)){
                            AlertMsg("数据为空!");
                        }else{
                            channel_div.grid.data = rs.data;            
                        }
                    }, "json");
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/player/analysis/operationPlayerNewChannelExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#channel_form").attr("action", url);
                    $('#channel_form').submit();
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    
                    $.post("/player/analysis/getOperationPlayerNewChannelEchart", options, function (rs) {
                        channelChart(rs);
                    });
                }
            },
            created: function () {
                this.gridFristLoad();
                this.SearchEchart();
            },
            mounted: function () {
                  var vm = this;
                  var form = layui.form();
                
                  //初始layerdate
                  var laydate = layui.laydate;
                  var start = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  document.getElementById('startDate_e').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('endDate_e').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(channel_form)', function (data) {
                    
                    var vm = this;
                    channel_div.filter = data.field; 
                    channel_div.gridFristLoad(); 
                    channel_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     }); 
    

    /*  echart模板*/
    var channel = echarts.init(document.getElementById('channel'));
  
    var channelChartOption = {
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            orient: 'vertical',
            right: '-6%',
            data:['ios官网','IOS个人(龙头版)','IOS个人版(att)','IOS个人版(疯狂电玩城)','IOS个人版(街机电玩城)','IOS个人版(连环夺宝)','IOS个人版(水浒传)', 'IOS个人版(糖果派对)',
                'IOS个人版(娱乐电玩城)','android官网', '阿里游戏','百度游戏中心','百度', '二维码推广(ewm.lztb.cn)','华为','老系统导入', '木蚂蚁','应用宝','未知']
        }   ,
        grid: {
            borderWidth: 0,
            left: '2%',
            right: '10%',
            bottom: '5%',
            textStyle: {
                color: "#fff"
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['06-06','06-07']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        dataZoom: [
            {
                "show": true, 
                "height": 30, 
                "xAxisIndex": [
                    0
                ], 
                bottom:10,
                "start": 0, 
                "end": 60
            }
        ],
        series : [
            {
                name:'ios官网',
                type:'bar',
                barMaxWidth: 50, 
                data:[81.0, 54.9],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人(龙头版)',
                type:'bar',
                barMaxWidth: 50,
                barMaxWidth: 50, 
                data:[2.0, 4.9],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人版(att)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人版(疯狂电玩城)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人版(街机电玩城)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人版(连环夺宝)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人版(水浒传)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人版(糖果派对)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'IOS个人(娱乐电玩城)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'android官网',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'阿里游戏',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'百度游戏中心',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'百度',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'二维码推广(ewm.lztb.cn)',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'华为',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'老系统导入',
                type:'bar',
                barMaxWidth: 50,
                data:[25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'木蚂蚁',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'应用宝',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            },
            {
                name:'未知',
                type:'bar',
                barMaxWidth: 50,
                data:[ 25.6, 76.7],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            }

        ]
    };
        
    /*渲染echart*/
    function channelChart(rs){
        
        var echarts_date;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            channelChartOption.xAxis[0].data  = echarts_date.xAxis;
            if (echarts_date.xAxis.length > 1) {
                for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                   channelChartOption.series[i].data = echarts_date.seriesInt[i];
                }
            }
        }
        channel.setOption(channelChartOption);
    }


});
