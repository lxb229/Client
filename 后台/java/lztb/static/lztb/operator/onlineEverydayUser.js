/**
 * 在线分析-玩家趋势
 * Created by nbin on 2017/8/7.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
        //     
        var onilneEvery_div = new Vue({
            el: "#onilneEvery_div",
            data: {
                filter: {
                    contrastBefourDate: '',
                    contrastAfterDate: '',
                },    
                grid: {
                    curr_page: 1, //当前页码
                    page_size: 10, //每页最大条数
                    total: 0, //总数
                    pageCount:0,
                    data: [] //当前页数据
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
                gridFristLoad: function () { //表格首次加载(fetch data of first page -> set data -> initialize pager)
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        onilneEvery_div.grid.data = rs.data.list;
                        onilneEvery_div.grid.total = rs.recordCount;
                        onilneEvery_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', onilneEvery_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            onilneEvery_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        onilneEvery_div.grid.data = rs.data.list;
                        onilneEvery_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/oper/online/tren/trenPageList", options, function (rs) {
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            onilneEvery_div.grid.data = rs.data.list;
                            AlertMsg("数据为空!");
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
                    var url = "/oper/online/tren/trenExportExcel?contrastBefourDate=" + vm.filter.contrastBefourDate + "&contrastAfterDate=" + vm.filter.contrastAfterDate;
                    $("#onilneEvery_form").attr("action", url);
                    $('#onilneEvery_form').submit();
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/tren/trenEcharts", options, function (rs) {                    
                        renderer_echarts(rs);
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
                  var start = {
                     min: laydate.now()
                    ,max: laydate.now()
                    ,istoday: false
                  };
                  var end = { 
                    max: laydate.now(-1)
                  };
                  
                  document.getElementById('contrastBefourDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('contrastAfterDate_a').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }
                
                //提交  
                form.on('submit(onilneEvery_form)', function (data) {
                    
                    var vm = this;
                    onilneEvery_div.filter = data.field; 
                    onilneEvery_div.gridFristLoad();
                    onilneEvery_div.SearchEchart();
                    return false; 
                });
                form.render();
            }

        });

     });

    /*初始化echart*/
    var chart = echarts.init(document.getElementById('chart_sinFo'));

    var templateChartOption =  {
            title: {
                text: '每日在线玩家趋势'
            },
            tooltip: {
                trigger: 'axis'
            },
            color: ["#FF0000", "#00BFFF"],
            legend: {
                data: ['今日', '昨日']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
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
                }
            }],
            dataZoom: [
                {
                    type: 'inside',
                    start: 50,
                    end: 100
                }, {
                    show: true,
                    type: 'slider',
                    y: '90%',
                    start: 50,
                    end: 100
                }
            ],
            series: [{
                name: '今日',
                type: 'line',
                stack: '今日',
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                    }
                },
                lineStyle: {
                    normal: {
                        width: 2,
                    }
                },
                data: []
            }, {
                name: '昨日',
                type: 'line',
                stack: '昨日',
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                    }
                },
                lineStyle: {
                    normal: {
                        width: 2,
                    }
                },
                data: []
            }]
    }
  

    /*渲染实时数据图表*/
    function renderer_echarts(rs) {
        var echarts_date;
        debugger;
        if(rs.code!='00000' ||  isEmpty(rs.data)){
            AlertMsg(rs.msg);
        }else{
            echarts_date = eval('(' + rs.data + ')');
            templateChartOption.xAxis.data  = echarts_date.xAxis;
            if (echarts_date.xAxis.length > 0) {
                for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                     templateChartOption.series[i].data = echarts_date.seriesInt[i];
                }
            }
      }
        chart.setOption(templateChartOption);
    };


});
