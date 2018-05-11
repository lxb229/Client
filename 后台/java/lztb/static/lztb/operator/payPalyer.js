/**
 * 付费玩家趋势
 * Created by Administrator on 2017/8/21
 */

require(['echarts', 'vue'], function (echarts, Vue) {

   var payPlayers = echarts.init(document.getElementById('payPlayers'));

   var payPlayersEchartOption = {
        
        tooltip: {
            "trigger": "axis",
            "axisPointer": {
                "type": "shadow",
                textStyle: {
                    color: "#fff"
                }

            },
        },
        toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
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
            x: '30%',
            //top: '2%',
            textStyle: {
                color: '#90979c',
            },
            "data": ['新增充值人数', '老玩家充值人数',  '付费率']
        },
        calculable: true,
        xAxis: [{
            "type": "category",
            "axisLine": {
                lineStyle: {
                    color: '#90979c'
                }
            },
            "splitLine": {
                "show": false
            },
            "axisTick": {
                "show": false
            },
            "splitArea": {
                "show": false
            },
            "axisLabel": {
                "interval": 0,
            },
            "data": [],
        }],
        yAxis: [{
            "type": "value",
            "splitLine": {
                "show": false
            },
            "axisLine": {
                lineStyle: {
                    color: '#90979c'
                }
            },
            "axisTick": {
                "show": false
            },
            "axisLabel": {
                "interval": 0,

            },
            "splitArea": {
                "show": false
            },

        }],
        dataZoom: [
            {
                "show": true, 
                "height": 35, 
                "xAxisIndex": [
                    0
                ], 
                bottom:-10,
                "start": 0, 
                "end": 80
            }
        ],
        series: [{
                "name": "新增充值人数",
                "type": "bar",
                "stack": "总数",
                "barMaxWidth": 25,
                "barGap": "20%",
                "itemStyle": {
                    "normal": {
                        "color": "#1F77B4",
                        "label": {
                            "show": true,
                            "textStyle": {
                                "color": "#fff"
                            },
                            "position": "insideTop",
                            formatter: function(p) {
                                return p.value > 0 ? (p.value) : '';
                            }
                        }
                    }
                },
                "data": [],
            },{
                "name": "老玩家充值人数",
                "type": "bar",
                "stack": "总数",
                "itemStyle": {
                    "normal": {
                        "color": "#FF7F0E",
                        "barBorderRadius": 0,
                        "label": {
                            "show": true,
                            "position": "inside",
                            formatter: function(p) {
                                return p.value > 0 ? (p.value) : '';
                            }
                        }
                    }
                },
                "data": []
            },{
                "name": "付费率",
                "type": "line",
                "stack": "总数",
                symbolSize:8,
                symbol:'circle',
                "itemStyle": {
                    "normal": {
                        "color": "#6ca7e2",
                        "barBorderRadius": 0,
                        "label": {
                            "show": true,
                            "position": "top",
                            formatter: function(p) {
                                return p.value > 0 ? (p.value)+'%' : '';
                            }
                        }
                    }
                },
                "data": []
            },
        ]
    } 

     /*渲染echart*/
    function payPlayersEchartChart(rs){
        debugger;
        var echarts_date;
        console.log(rs)
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            payPlayersEchartOption.xAxis[0].data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                    payPlayersEchartOption.series[i].data = echarts_date.seriesInt[i];
            }
        }
      payPlayers.setOption(payPlayersEchartOption);
    }



     /*渲染列表*/
     layui.config({dir: baseUrl + '../plugs/layui/'});

     layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var payPlayers_div = new Vue({
            el: "#payPlayers_div",
            data: {
                filter: {
                    startDate: '',
                    endDate: ''
                },    
                grid: {
                    curr_page: 1, 
                    page_size: 10, 
                    total: 0, 
                    pageCount:0,
                    data: [] 
                }
            },
            watch: {
                "grid.curr_page": function (val, oldVal) { 
                    if (val === oldVal) {
                        return;
                    }
                    this.gridReload();
                }
            },
            methods: {
                formatDate(time) {
                   var date = new Date(time);
                  return formatDate(date, 'yyyy-MM-dd');
                },
                gridFristLoad: function () { 
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        payPlayers_div.grid.data = rs.data.list;
                        payPlayers_div.grid.total = rs.recordCount;
                        payPlayers_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', payPlayers_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            payPlayers_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        payPlayers_div.grid.data = rs.data.list;
                        payPlayers_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/pay/getOperationMoneyTrendPageList", options, function (rs) {            
                        debugger; 
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            payPlayers_div.grid.data = rs.data.list;
                            payPlayers_div.grid.total = rs.recordCount;
                            payPlayers_div.grid.pageCount = rs.pageCount;
                            AlertMsg("列表数据为空!");
                        }else{  
                            cb(rs);
                        }
                    }, "json");
                },
                onSearch: function(event){
                    this.gridReload();
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/player/pay/getOperationMoneyTrendExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#payPlayers_form").attr("action", url);
                    $('#payPlayers_form').submit(); 
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/pay/getOperationMoneyTrendEchart", options, function (rs) {
                        payPlayersEchartChart(rs);
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
                
                  //初始layerdate
                  var laydate = layui.laydate;
                  var start = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  
                  document.getElementById('moneyTrendStartDate').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('moneyTrendEndDate').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(payPlayers_form)', function (data) {
                    var vm = this;
                    payPlayers_div.filter = data.field;
                    payPlayers_div.gridFristLoad();
                    payPlayers_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });



});