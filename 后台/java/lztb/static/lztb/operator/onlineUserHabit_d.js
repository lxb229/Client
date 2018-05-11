/**
 * 在线分析- 在线习惯分析 - 玩家启动次数分布
 * Created by nbin on 2017/8/9.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
     var div_distribution_num = new Vue({
            el: "#div_distribution_num",
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
                gridFristLoad: function () { 
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        div_distribution_num.grid.data = rs.data.list;
                        div_distribution_num.grid.total = rs.recordCount;
                        div_distribution_num.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', div_distribution_num.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            div_distribution_num.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        div_distribution_num.grid.data = rs.data.list;
                        div_distribution_num.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {               
                    var options = this.filter; 
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/oper/online/startup/startupPageList", options, function (rs) {      

                        console.log(rs);
                        if(isEmpty(rs.data.list)){
                            div_distribution_num.grid.data = rs.data.list;
                            console.log("玩家启动次数分布列表数据为空");
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
                    var url = "/oper/online/startup/startupExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#distribution_num_form").attr("action", url);
                    $('#distribution_num_form').submit();
                }
                ,
                SearchDistributStartup: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/startup/startupEcharts",options, function (rs) {
                        readerDistributionNumChart(rs);
                    });
                }
            },
            created: function () {
        
                this.gridFristLoad();
                this.SearchDistributStartup();
            },
            mounted: function () {
                  var vm = this;
                  var form = layui.form();
                
                  //初始layerdate
                  var laydate = layui.laydate;
                  var start = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  
                  document.getElementById('startDate_d').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('endDate_d').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(distribution_num_form)', function (data) {
                    div_distribution_num.filter = data.field; 
                    div_distribution_num.gridFristLoad();
                    div_distribution_num.SearchDistributStartup();
                    return false; //阻止原生表单跳转
                });
                form.render();
            }

        });


     });
    
     var distributionNum = echarts.init(document.getElementById('distributionNum'));

     var colorList = [];
     var renderDistributionNumOption ={

            color: ['#00B6A4'],
            title: {
                 subtext: '玩家启动次数分布(默认7日数据)'
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
                    data: ["0~1次","2次","3次","4次","5次","5~10次","10~15次","＞15次"],
                    axisTick: {
                        show: false
                    }
                }
            ],
            series: [{
                    name: '玩家启动次数分布(%)',
                    type: 'bar',
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
     function readerDistributionNumChart(rs){
        var echarts_date;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
            echarts_date = eval('(' + rs.data + ')');
            if (echarts_date.seriesDouble.length > 0) {
                renderDistributionNumOption.series[0].data = echarts_date.seriesDouble[0];
            }
            //
            for (var i in echarts_date.seriesDouble[0]) {
                colorList[i] = echarts_date.seriesDouble[0][i] < 10 ? '#FF4552' : '#7EC0EE';
            }
        }
         distributionNum.setOption(renderDistributionNumOption);
      }



});
