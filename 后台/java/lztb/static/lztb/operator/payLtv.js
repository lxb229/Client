/**
 * 付费玩家 LTV
 * Created by Administrator on 2017/8/21
 */

require(['echarts', 'vue'], function (echarts, Vue) {

    var payLtv = echarts.init(document.getElementById('payLtv'));
    var payLtvOption = {
            title: {
                subtext: '新增玩家价值'
            },
            tooltip: {
                trigger: 'axis'
            },
            color: ["#00BFFF"],
            legend: {
                data: []
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
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
                },
            }],
            dataZoom: [{
                type: 'inside',
                start: 50,
                end: 100
            }, {
                show: true,
                type: 'slider',
                y: '90%',
                start: 50,
                end: 100
            }],
            series: [{
                name: 'LTV',
                type: 'line',
                smooth: true,
                lineStyle: {
                    normal: {
                        width: 2,
                    }
                },
                data: []
            }]
        };


     /*渲染echart*/
    function payLtvEchartChart(rs){
        debugger;
        var echarts_date;
        console.log(rs)
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
            payLtvOption.xAxis.data  = echarts_date.xAxis;
            for (var i = 0 ; i < echarts_date.seriesDouble.length ;  i++) {
                    payLtvOption.series[i].data = echarts_date.seriesDouble[i];
            }
        }
      payLtv.setOption(payLtvOption);
    }



     /*渲染列表*/
     layui.config({dir: baseUrl + '../plugs/layui/'});

     layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var payLtv_div = new Vue({
            el: "#payLtv_div",
            data: {
                filter: {
                    startDate: null,
                    endDate: null
                },    
                grid: {
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
                    var options = this.filter;
                    $.post("/player/pay/getOperationMoneyLtvList", options, function (rs) {
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data)){
                            payLtv_div.grid.data = rs.data;       
                            AlertMsg("数据为空!");
                        }else{
                            payLtv_div.grid.data = rs.data;       
                        }
                    }, "json");
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/player/pay/getOperationMoneyLtvExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#payLtv_form").attr("action", url);
                    $('#payLtv_form').submit(); 
                },
                SearchEchart: function () {
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/pay/getOperationMoneyLtvEchart", options, function (rs) {
                        payLtvEchartChart(rs);
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
                  
                  document.getElementById('ltvStartDate').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('ltvEndDate').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(payLtv_form)', function (data) {
                    var vm = this;
                    payLtv_div.filter = data.field;
                    payLtv_div.gridFristLoad();
                    payLtv_div.SearchEchart();
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });



});