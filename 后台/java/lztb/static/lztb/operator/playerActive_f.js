/**
 * 活跃玩家付费贡献
 * Created by Administrator on 2017/7/14.
 */

require(['echarts', 'vue'], function (echarts, Vue) {

    var structure_pay_echart = echarts.init(document.getElementById('structure_pay_echart'))
     
    var structurePayOption = {
        tooltip : {  
            trigger: 'axis',
            hideDelay: 50,
            transitionDuration:0,
            padding: 10,    // [5, 10, 15, 20]
        },
        legend: {
             data:['免费','V1','V2','V3','V4','V5','V6','V7','V8','V9','V10','V11','V12','V13','V14','V15']
        },
        calculable : true,
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : {
            data : []
        },
        yAxis : {
            type : 'value'
        },
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
                    name:'免费',
                    type:'bar',
                    tooltip : {             
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    barGap: "10%", 
                    itemStyle: {
                        normal: {
                            barBorderRadius: 0, 
                            color: "rgba(60,169,196,0.5)", 
                            
                        }
                    }, 
                    data:[]
                },
                {
                    name:'V1',
                    type:'bar',
                    tooltip : {             
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                },
                {
                    name:'V2',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V3',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V4',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V5',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V6',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V7',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V8',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V9',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V10',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V11',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V12',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V13',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V14',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
                ,
                {
                    name:'V15',
                    type:'bar',
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} : {c}"
                    },
                    barMaxWidth: 50, 
                    stack: '数据项',
                    data:[]
                }
          ]
    };

        /*渲染echart*/
    function structurePayEchart(rs){
        var echarts_date;
        console.log(rs)
       debugger;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            structurePayOption.xAxis.data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                structurePayOption.series[i].data = echarts_date.seriesInt[i];
            }
        }
      structure_pay_echart.setOption(structurePayOption);
    } 
  
    /*渲染列表*/
     layui.config({dir: baseUrl + '../plugs/layui/'});

     layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var structure_pay_div = new Vue({
            el: "#structure_pay_div",
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
                        structure_pay_div.grid.data = rs.data.list;
                        structure_pay_div.grid.total = rs.recordCount;
                        structure_pay_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', structure_pay_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            structure_pay_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        structure_pay_div.grid.data = rs.data.list;
                        structure_pay_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/active/getOperationPlayerBriskPayPageList", options, function (rs) {             
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            structure_pay_div.grid.data = rs.data.list;
                            structure_pay_div.grid.total = rs.recordCount;
                            structure_pay_div.grid.pageCount = rs.pageCount;
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
                    var url = "/player/active/getOperationPlayerBriskPayExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#structure_pay_form").attr("action", url);
                    $('#structure_pay_form').submit(); 
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/active/getOperationPlayerBriskPayEchart", options, function (rs) {
                        structurePayEchart(rs);
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
                  
                  document.getElementById('structurePayStartDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('structurePayEndDate_a').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(structure_pay_form)', function (data) {
                    var vm = this;
                    structure_pay_div.filter = data.field;
                    structure_pay_div.gridFristLoad();
                    structure_pay_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });

});