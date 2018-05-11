/**
 * 玩家分析 -新增玩家分析
 * Created by nbin on 2017/8/10.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var playerAnalyze_div = new Vue({
            el: "#playerAnalyze_div",
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
                "grid.curr_page": function (val, oldVal) { //页码改变自动reload
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
                gridFristLoad: function () { //表格首次加载(fetch data of first page -> set data -> initialize pager)
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        playerAnalyze_div.grid.data = rs.data.list;
                        playerAnalyze_div.grid.total = rs.recordCount;
                        playerAnalyze_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', playerAnalyze_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            playerAnalyze_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        playerAnalyze_div.grid.data = rs.data.list;
                        playerAnalyze_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/analysis/getOperationPlayerNewList", options, function (rs) {             
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            playerAnalyze_div.grid.data = rs.data.list;
                            playerAnalyze_div.grid.total = rs.recordCount;
                            playerAnalyze_div.grid.pageCount = rs.pageCount;
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
                    var url = "/player/analysis/operationPlayerNewExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#playerAnalyze_form").attr("action", url);
                    $('#playerAnalyze_form').submit();
                    // $("#playerAnalyze_form").attr("action", '');
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/analysis/getOperationPlayerNewEchart", options, function (rs) {
                         
                        playerAnalyzeChart(rs);
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
                  
                  document.getElementById('startDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('endDate_a').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(playerAnalyze_form)', function (data) {
                    var vm = this;
                    playerAnalyze_div.filter = data.field; //data.field为当前容器的全部表单字段，名值对形式：{name: value}
                    playerAnalyze_div.gridFristLoad();
                    playerAnalyze_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });
    
    

    var playerAnalyze = echarts.init(document.getElementById('playerAnalyze'));

    var  playerAnalyzeChartOption = {
            title: {
                text: '新增玩家'
                //                        subtext: '纯属虚构'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['新增设备', '新增注册', '新增玩家']
            },
            grid: {
                borderWidth: 0,
                top: '10%',
                left: '3%',
                right: '4%',
                bottom: '5%',
                textStyle: {
                    color: "#fff"
                }
            },
            calculable: true,
            xAxis: [
                {
                    type: 'category',
                    data: []
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            dataZoom: [
            {
                "show": true, 
                "height": 30, 
                "xAxisIndex": [
                    0
                ], 
                bottom:-10,
                "start": 0, 
                "end": 40
            }
            ],
            series: [
                {
                    name: '新增设备',
                    type: 'bar',
                    barMaxWidth: 50, 
                    data: [],
                    stack: '新增设备',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    name: '新增注册',
                    type: 'bar',
                    barMaxWidth: 50, 
                    data: [],
                    stack: '新增注册',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    name: '新增玩家',
                    type: 'bar',
                    barMaxWidth: 50, 
                    data: [],
                    stack: '新增玩家',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                }

            ]

    }

    
    /*渲染echart*/
    function playerAnalyzeChart(rs){
        var echarts_date;
        console.log(rs)
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            playerAnalyzeChartOption.xAxis[0].data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                    playerAnalyzeChartOption.series[i].data = echarts_date.seriesInt[i];
            }
        }
      playerAnalyze.setOption(playerAnalyzeChartOption);
    }

});
