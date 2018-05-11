/**
 * 活跃玩家结构
 * Created by Administrator on 2017/7/14.
 */

require(['echarts', 'vue'], function (echarts, Vue) {

    var structure_days_echart = echarts.init(document.getElementById('structure_days_echart'));
     
    var structureDaysEchartsOption = {
        title: {
            text: '活跃玩家结构-游戏天数'
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data:['首日','2~7天','8~14天','15~30天','31~60天','61~90天','90~180天','＞180天']
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['6-2','6-3','6-4','6-5','6-6','6-7','6-8']
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
                bottom:0,
                "start": 0, 
                "end": 80
            }
        ],
        series : [
            {
                name:'首日',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'2~7天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[120, 132, 101, 134, 90, 230, 210]
            },
            {
                name:'8~14天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[220, 182, 191, 234, 290, 330, 310]
            },
//                        '90~180天','＞180天'
            {
                name:'15~30天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[150, 232, 201, 154, 190, 330, 410]
            },
            {
                name:'31~60天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[320, 332, 301, 334, 390, 330, 320]
            },
            {
                name:'61~90天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[820, 932, 901, 934, 1290, 1330, 1320]
            },
            {
                name:'90~180天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[320, 332, 301, 334, 390, 330, 320]
            },
            {
                name:'＞180天',
                type:'line',
                stack: '总量',
                areaStyle: {normal: {}},
                data:[820, 932, 901, 934, 1290, 1330, 1320]
            }
        ]
    };
        
        /*渲染echart*/
    function structureDaysEchart(rs){
        var echarts_date;
        console.log(rs)
       
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            structureDaysEchartsOption.xAxis[0].data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                structureDaysEchartsOption.series[i].data = echarts_date.seriesInt[i];
            }
        }
      structure_days_echart.setOption(structureDaysEchartsOption);
    } 


    /*渲染列表*/
     layui.config({dir: baseUrl + '../plugs/layui/'});

     layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var structure_days_div = new Vue({
            el: "#structure_days_div",
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
                        structure_days_div.grid.data = rs.data.list;
                        structure_days_div.grid.total = rs.recordCount;
                        structure_days_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', structure_days_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            structure_days_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        structure_days_div.grid.data = rs.data.list;
                        structure_days_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/active/getOperationPlayerBriskGameDaysPageList", options, function (rs) {             
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            structure_days_div.grid.data = rs.data.list;
                            structure_days_div.grid.total = rs.recordCount;
                            structure_days_div.grid.pageCount = rs.pageCount;
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
                    var url = "/player/active/getOperationPlayerBriskGameDaysExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#structure_days_form").attr("action", url);
                    $('#structure_days_form').submit(); 
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/active/getOperationPlayerBriskGameDaysEchart", options, function (rs) {
                        structureDaysEchart(rs);
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
                  
                  document.getElementById('structureDaysStartDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('structureDaysEndDate_a').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(structure_days_form)', function (data) {
                    var vm = this;
                    structure_days_div.filter = data.field;
                    structure_days_div.gridFristLoad();
                    structure_days_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });

});