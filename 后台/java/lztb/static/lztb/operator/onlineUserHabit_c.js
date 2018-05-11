/**
 * 在线分析- 在线习惯分析 - 游戏时长分布
 * Created by nbin on 2017/8/9.
 */


require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
     var div_distribution_time = new Vue({
            el: "#div_distribution_time",
            data: {
                filter: {
                    startDate: '',
                    endDate: '',
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
                        div_distribution_time.grid.data = rs.data.list;
                        div_distribution_time.grid.total = rs.recordCount;
                        div_distribution_time.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', div_distribution_time.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            div_distribution_time.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        div_distribution_time.grid.data = rs.data.list;
                        div_distribution_time.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) { 
                    debugger;       
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/oper/online/scatter/scatterPageList", options, function (rs) {
                        console.log(rs);
                        if(isEmpty(rs.data.list)){
                            div_distribution_time.grid.data = rs.data.list;
                            console.log("游戏时长分布列表数据为空");
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
                    var url = "/oper/online/scatter/scatterExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#distribution_time_form").attr("action", url);
                    $('#distribution_time_form').submit();
                },
                SearchDistributionTime: function () {//时长          
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/scatter/scatterEcharts",options, function (rs) {
                        readerDistributionTimeChart(rs);
                    });
                }
            },
            created: function () {
        
                this.gridFristLoad();
                this.SearchDistributionTime();
            },
            mounted: function () {
                  var vm = this;
                  var form = layui.form();
                
                  //初始layerdate
                  var laydate = layui.laydate;
                  var start = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  
                  document.getElementById('startDate_c').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('endDate_c').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(distribution_time_form)', function (data) {
                    div_distribution_time.filter = data.field;
                    div_distribution_time.gridFristLoad();
                    div_distribution_time.SearchDistributionTime();
                    return false; 
                });
                form.render();
            }

        });


     });
    
     var distributionTime = echarts.init(document.getElementById('distribution'));
     var colorList = [];
     var renderDistributionOption ={

            color: ['#00B6A4'],
            title: {
                 subtext: '游戏时长分布(默认7日数据)'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            xAxis: [{
               type: 'value'
            }],
            yAxis: [{
                    type: 'category',
                    data: ["0-1min","1~5mins","6~10mins","10~30mins","30~60mins","1-2hours","2-4hours","4hours"],
                    axisTick: {
                        show: false
                    }
                }
            ],
            series: [{
                    name: '玩家启动次数分布(%)',
                    type: 'bar',

                   /* barWidth: 16,*/
                    itemStyle: {
                        normal: {
                            color: function(params) {
                                return colorList[params.dataIndex]
                            }
                        }
                    },
                    label: {
                        normal: {
                            show: true,
                            position: 'right',
                            formatter: '{c}%',
                        }
                    },
                    data:[]
                }
            ]

      };

     /*渲染echart*/
     function readerDistributionTimeChart(rs){
        var echarts_date;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
            echarts_date = eval('(' + rs.data + ')');
            if (echarts_date.seriesDouble.length > 0) {
                renderDistributionOption.series[0].data = echarts_date.seriesDouble[0];
            }
            //
            for (var i in echarts_date.seriesDouble[0]) {
                colorList[i] = echarts_date.seriesDouble[0][i] < 10 ? '#FF4552' : '#7EC0EE';
            }
        }
         distributionTime.setOption(renderDistributionOption);
      }



});
