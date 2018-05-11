/**
 * 活跃玩家
 * Created by Administrator on 2017/7/14.
 */

require(['echarts', 'vue'], function (echarts, Vue) {

    var dwm_echarts_a = echarts.init(document.getElementById('dwm_echarts_a'));
    var dwm_echarts_b = echarts.init(document.getElementById('dwm_echarts_b'));
     
    var dwmEchartsOption = {
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['日活跃(DAU)','周活跃(WAU)','月活跃(MAU)']
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : []
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
                "end": 80
            }
        ],
        series : [
            {
                name:'日活跃(DAU)',
                type:'line',
                smooth:true,
                data:[]
            },
            {
                name:'周活跃(WAU)',
                type:'line',
                smooth:true,
                data:[]
            },
            {
                name:'月活跃(MAU)',
                type:'line',
                smooth:true,
                data:[]
            }
        ]
    };

    var dwmEchartsRateOption = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:['DAU/MAU']
            },
            xAxis:  {
                type: 'category',
                boundaryGap: false,
                data : []
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value} %'
                }
            },
            dataZoom: [
            {
                "show": true, 
                "height": 30, 
                "xAxisIndex": [
                    0
                ], 
                bottom:10,
                "start": 0, 
                "end": 80
            }
            ],
            series: [
                {
                    name:'DAU/MAU',
                    type:'line',
                    data:[],
                    markPoint: {
                        data: [
                            {type: 'max', name: '最大值'},
                            {type: 'min', name: '最小值'}
                        ]
                    }
                }
            ]
   };


     /*渲染echart*/
    function dwmEchartChart(rs){
        var echarts_date;
        console.log(rs)
       
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            dwmEchartsOption.xAxis[0].data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesLong.length ;  i++) {
                dwmEchartsOption.series[i].data = echarts_date.seriesLong[i];
            }
        }
      dwm_echarts_a.setOption(dwmEchartsOption);
    }    
    function dwmRateEchartChart(rs){
        var echarts_date;
        console.log(rs)
        
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
            echarts_date = eval('(' + rs.data + ')');
            dwmEchartsRateOption.xAxis.data  = echarts_date.xAxis;     
            dwmEchartsRateOption.series[0].data = echarts_date.seriesDouble[0];
        }
      dwm_echarts_b.setOption(dwmEchartsRateOption);
    }


      /*渲染列表*/
    layui.config({dir: baseUrl + '../plugs/layui/'});

    layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var dwm_div = new Vue({
            el: "#dwm_div",
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
                        dwm_div.grid.data = rs.data.list;
                        dwm_div.grid.total = rs.recordCount;
                        dwm_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', dwm_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            dwm_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        dwm_div.grid.data = rs.data.list;
                        dwm_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/active/getOperationPlayerBriskPageList", options, function (rs) {             
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            dwm_div.grid.data = rs.data.list;
                            dwm_div.grid.total = rs.recordCount;
                            dwm_div.grid.pageCount = rs.pageCount;
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
                    var url = "/player/active/getOperationPlayerBriskExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#dwm_form").attr("action", url);
                    $('#dwm_form').submit(); 
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/active/getOperationPlayerBriskEchart", options, function (rs) {
                        dwmEchartChart(rs);
                    });
                },
                SearchRateEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/active/getOperationPlayerBriskRatioEchart", options, function (rs) {
                        dwmRateEchartChart(rs);
                    });
                }
            },
            created: function () {
                layer.load(0, {shade: false});
                this.gridFristLoad();
                this.SearchEchart();
                this.SearchRateEchart();
            },
            mounted: function () {
                  var vm = this;
                  var form = layui.form();
                
                  //初始layerdate
                  var laydate = layui.laydate;
                  var start = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  
                  document.getElementById('dwmStartDate').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('dwmEndDate').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(dwm_form)', function (data) {
                    var vm = this;
                    dwm_div.filter = data.field;
                    dwm_div.gridFristLoad();
                    dwm_div.SearchEchart();
                    dwm_div.SearchRateEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });


});