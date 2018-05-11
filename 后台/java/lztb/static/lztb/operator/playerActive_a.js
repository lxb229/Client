/**
 * 活跃玩家趋势
 * Created by Administrator on 2017/7/14.
 */

require(['echarts', 'vue'], function (echarts, Vue) {

   var active_echart_a = echarts.init(document.getElementById('active_echart_a'));

   var activeEchartOption = {
        //backgroundColor: "#344b58",
        
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
            "data": ['新玩家', '老玩家',  '活跃玩家']
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
                "end": 50
            }
        ],
        series: [{
                "name": "新玩家",
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
                "name": "老玩家",
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
                "name": "活跃玩家",
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
                                return p.value > 0 ? (p.value) : '';
                            }
                        }
                    }
                },
                "data": []
            },
        ]
    } 

     /*渲染echart*/
    function activeEchartChart(rs){
        var echarts_date;
        console.log(rs)
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            activeEchartOption.xAxis[0].data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                    activeEchartOption.series[i].data = echarts_date.seriesInt[i];
            }
        }
      active_echart_a.setOption(activeEchartOption);
    }



     /*渲染列表*/
     layui.config({dir: baseUrl + '../plugs/layui/'});

     layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var active_div_a = new Vue({
            el: "#active_div_a",
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
                        active_div_a.grid.data = rs.data.list;
                        active_div_a.grid.total = rs.recordCount;
                        active_div_a.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', active_div_a.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            active_div_a.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        active_div_a.grid.data = rs.data.list;
                        active_div_a.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/active/getOperationPlayerBriskTrendPageList", options, function (rs) {             
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            active_div_a.grid.data = rs.data.list;
                            active_div_a.grid.total = rs.recordCount;
                            active_div_a.grid.pageCount = rs.pageCount;
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
                    var url = "/player/active/getOperationPlayerBriskTrendExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#active_form_a").attr("action", url);
                    $('#active_form_a').submit(); 
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/active/getOperationPlayerBriskTrendEchart", options, function (rs) {
                        activeEchartChart(rs);
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
                  
                  document.getElementById('activeStartDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('activeEndDate_a').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(active_form_a)', function (data) {
                    var vm = this;
                    active_div_a.filter = data.field;
                    active_div_a.gridFristLoad();
                    active_div_a.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });



});