/**
 * 玩家分析 -首次在线时长
 * Created by nbin on 2017/8/10.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        //     
        var durationAnalysis_div = new Vue({
            el: "#durationAnalysis_div",
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
                        durationAnalysis_div.grid.data = rs.data.list;
                        durationAnalysis_div.grid.total = rs.recordCount;
                        durationAnalysis_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', durationAnalysis_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            durationAnalysis_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        durationAnalysis_div.grid.data = rs.data.list;
                        durationAnalysis_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/analysis/getOperationPlayerNewFirstOnlineList", options, function (rs) {   
                                  
                        console.log(rs);
                        if(isEmpty(rs.data.list)){
                            durationAnalysis_div.grid.data = rs.data.list;
                            durationAnalysis_div.grid.total = rs.recordCount;
                            durationAnalysis_div.grid.pageCount = rs.pageCount;
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
                    var url = "/player/analysis/operationPlayerNewFirstOnlineExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#durationAnalysis_form").attr("action", url);
                    $('#durationAnalysis_form').submit();
                    // $("#durationAnalysis_form").attr("action", '');
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/player/analysis/getOperationPlayerNewFirstOnlineEchart", options, function (rs) {
                        
                        durationAnalysisChart(rs);
                    });
                }
            },
            created: function () {
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
                  
                  document.getElementById('startDate_b').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('endDate_b').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }

                //提交  
                form.on('submit(durationAnalysis_form)', function (data) {
                    var vm = this;
                    durationAnalysis_div.filter = data.field; 
                    durationAnalysis_div.gridFristLoad(); 
                    return false; //阻止原生表单跳转
                });
                form.render();

            }

        });

     });
    



    var durationAnalysis = echarts.init(document.getElementById('durationAnalysis'));

    var durationAnalysisChartOption = {

            title: {
                // text: '首次在线时长',
                subtext: '首次在线时长',
                x: 'center',
                top: 30
            },
            backgroundColor: "#F2F2F2",
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            color: ['#009999', '#29497F', '#5CCCCC', '#006363', '#1B3D54', '#23245C', '#195B3D', '#092437'],
            /*legend: {
                orient: 'vertical',
                left: '3%',
                top: 50,
                 data: ['1mins以下','1~5mins','6~10mins','10~30mins','30~60mins','1~2hours','2~4hours','4hours以上'] // x轴  时间段  （例：['半小时内', '1-2小时', '2-3小时', '3-4小时', '4-小时', '5-6小时', '6-7小时', '7-8小时', '8-9小时', '9-10小时']）
            },*/
            series: [
                {
                    name: '在线时长',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                    { value: 100, name: '1mins以下' },
                    { value: 90, name: '1~5mins' },
                    { value: 66, name: '6~10mins' },
                    { value: 44, name: '10~30mins' },
                    { value: 77, name: '30~60mins' },
                    { value: 55, name: '1~2hours' },
                    { value: 1111, name: '2~4hours' },
                    { value: 333, name: '4hours以上' }],  
                    // 时间段的值   （例：[{ value: 100, name: '半小时内' },{ value: 90, name: '1-2小时' }]）
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            label: {
                                show: true,
                                formatter: '{b}({d}%)'
                            },
                            labelLine: { show: true }
                        }
                    }
                }
            ]
                    
    }

   /*渲染echart*/
    function durationAnalysisChart(rs){
        debugger;
        var echarts_date;
        if(rs.code=='00000' &&  !isEmpty(rs.data)){
          echarts_date = eval('(' + rs.data + ')');
          durationAnalysisChartOption.series[0].data = echarts_date.seriesData;
        }
      durationAnalysis.setOption(durationAnalysisChartOption);
    }
    

});
