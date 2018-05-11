/**
 * 玩家分析 -流失回流
 * Created by nbin on 2017/8/19.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    var lossBack_echart = echarts.init(document.getElementById('lossBack_echart'));

    var lossBackEchartOption = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['流失玩家','回流玩家','流失率']
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
        xAxis: [
            {
                type: 'category',
                data: ['2017-08-15','2017-08-16','2017-08-17','2017-08-18','2017-08-19','2017-08-20','2017-08-21']
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '玩家',
                interval: 50,
                axisLabel: {
                    formatter: '{value} '
                }
            },
            {
                type: 'value',
                name: '流失率',
                interval: 30,
                axisLabel: {
                    formatter: '{value} %'
                }
            }
        ],
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
        series: [
            {
                name:'流失玩家',
                type:'bar',
                barMaxWidth: 50, 
                data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6]
            },
            {
                name:'回流玩家',
                type:'bar',
                barMaxWidth: 50, 
                data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6]
            },
            {
                name:'流失率',
                type:'line',
                yAxisIndex: 1,
                data:[20, 22, 33, 45, 63, 10, 123]
            }
        ]
    };

     /*渲染echart*/
    function lossBackEchartChart(rs){
        var echarts_date;
        debugger;
        console.log(rs)
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            lossBackEchartOption.xAxis[0].data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                    lossBackEchartOption.series[i].data = echarts_date.seriesInt[i];
            }
        }
      lossBack_echart.setOption(lossBackEchartOption);
    }

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var lossBack_div = new Vue({
            el: "#lossBack_div",
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
                gridFristLoad: function () {
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        lossBack_div.grid.data = rs.data.list;
                        lossBack_div.grid.total = rs.recordCount;
                        lossBack_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', lossBack_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            lossBack_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        lossBack_div.grid.data = rs.data.list;
                        lossBack_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    debugger;
                    $.post("/player/active/getOperationPlayerLossBackPageList", options, function (rs) {             
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            lossBack_div.grid.data = rs.data.list;
                            lossBack_div.grid.total = rs.recordCount;
                            lossBack_div.grid.pageCount = rs.pageCount;
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
                    var url = "/player/active/getOperationPlayerLossBackExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#lossBack_form").attr("action", url);
                    $('#lossBack_form').submit();
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/active/getOperationPlayerLossBackEchart", options, function (rs) {
                        lossBackEchartChart(rs);
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
                  
                  document.getElementById('lossBackStartDate').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('lossBackEndDate').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(lossBack_form)', function (data) {
                    var vm = this;
                    lossBack_div.filter = data.field; 
                    lossBack_div.gridFristLoad();
                    lossBack_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();
            }

        });

     });


});
