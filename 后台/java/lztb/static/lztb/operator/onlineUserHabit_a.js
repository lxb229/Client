/**
 * 在线分析- 在线习惯分析 - 游戏时长/游戏次数
 * Created by nbin on 2017/8/9.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
        //     
        var div_duration_time = new Vue({
            el: "#div_duration_time",
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
                        div_duration_time.grid.data = rs.data.list;
                        div_duration_time.grid.total = rs.recordCount;
                        div_duration_time.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', div_duration_time.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            div_duration_time.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        div_duration_time.grid.data = rs.data.list;
                        div_duration_time.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/oper/online/time/playTimePageList", options, function (rs) {
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                             div_duration_time.grid.data = rs.data.list;
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
                    var url = "/oper/online/time/playTimeExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#duration_time_sub").attr("action", url);
                    $('#duration_time_sub').submit();
                },
                playTimeEcharts: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/time/playTimeEcharts", options, function (rs) {
                       readerPlayTimeChart(rs);
                    });
                },
                palyStartEcharts: function () {//次数
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/time/palyStartEcharts", options, function (rs) {
                        readerPlayStartChart(rs);                       
                    });

                }
            },
            created: function () {
                layer.load(0, {shade: false});
                this.gridFristLoad();
                this.playTimeEcharts();
                this.palyStartEcharts();
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
                form.on('submit(duration_time_sub)', function (data) {
                    var vm = this;
                    div_duration_time.filter = data.field; //data.field为当前容器的全部表单字段，名值对形式：{name: value}
                    div_duration_time.gridFristLoad();
                    div_duration_time.playTimeEcharts();
                    div_duration_time.palyStartEcharts();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });
    
    //  /*初始化*/
    // $(function () {
    //      $.post("/oper/online/duration/durationEcharts", function (rs) {
    //         
    //         readerGameTimeChart(rs); //游戏时长
    //      }, "json");

    //      $.post("/oper/online/time/timeEcharts", function (rs) {
    //           
    //         readerGameNumberChart(rs); //游戏次数
    //      }, "json");
    // });

    
    /*初始echarts模板*/
    var gameTime = echarts.init(document.getElementById('gameTime'));
    var gameNumber = echarts.init(document.getElementById('gameNumber'));
   
    var gameTimeChartOption = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['单次平均游戏时长','累计平均游戏时长']
            },
            grid: {          
                bottom: '6%',
            },
            xAxis: [
                {
                    type: 'category',
                    data : []
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '游戏时长(分钟)',
                    interval: 30,
                    axisLabel: {
                        formatter: '{value} '
                    }
                }
            ],
            dataZoom: [
                {
                    "show": true, 
                    "height": 30, 
                    "xAxisIndex": [
                        0
                    ], 
                    bottom:-5,
                    "start": 0, 
                    "end": 50
                }
            ], 
            series: [
                 {
                    name:'单次平均游戏时长',
                    type:'bar',
                    barMaxWidth: 50, 
                    stack: '单次平均游戏时长',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data:[]
                 },
                 {
                    name:'累计平均游戏时长',
                    type:'bar',
                    barMaxWidth: 50, 
                    stack: '累计平均游戏时长',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data:[]
                 }
            ]

    };

    var gameNumberChartOption = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {         
                    type : 'shadow'     
                }
            },
            legend: {
               data:['新玩家平均启动次数','老玩家平均启动次数','玩家平均启动次数']
            },
            grid: {          
                bottom: '6%',
            },
            xAxis: [
                {
                    type: 'category',
                    data : []
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '启动次数',
                    interval: 30,
                    axisLabel: {
                        formatter: '{value} '
                    }
                }
            ],
            dataZoom: [
                {
                    "show": true, 
                    "height": 30, 
                    "xAxisIndex": [
                        0
                    ], 
                    bottom:-5,
                    "start": 0, 
                    "end": 50
                }
            ],
            series: [
                {
                    name:'新玩家平均启动次数',
                    type:'bar',
                    barMaxWidth: 50, 
                    stack: '新玩家平均启动次数',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data:[]
                },
                {
                    name:'老玩家平均启动次数',
                    type:'bar',
                    barMaxWidth: 50, 
                    stack: '老玩家平均启动次数',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data:[]
                },
                {
                    name:'玩家平均启动次数',
                    type:'bar',
                    barMaxWidth: 50,      
                    stack: '玩家平均启动次数',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    data:[]
                }
            ]

    };

     /*渲染echart*/
     //时长
    function readerPlayTimeChart(rs){
        var echarts_date;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            gameTimeChartOption.xAxis[0].data  = echarts_date.xAxis;
            if (echarts_date.xAxis.length > 0) {
                for (var i = 0 ; i < echarts_date.seriesLong.length ;  i++) {
                   gameTimeChartOption.series[i].data = echarts_date.seriesLong[i];
                }
            }
        }
        gameTime.setOption(gameTimeChartOption);
      }
    //启动
    function readerPlayStartChart(rs){
       var echarts_date;
       if(rs.code=='00000' &&  !isEmpty(rs.data)){
            echarts_date = eval('(' + rs.data + ')');
            gameNumberChartOption.xAxis[0].data  = echarts_date.xAxis;
            if (echarts_date.xAxis.length > 0) {
                for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                   gameNumberChartOption.series[i].data = echarts_date.seriesInt[i];
                }
            }
        }
        gameNumber.setOption(gameNumberChartOption);
    }
    

});

