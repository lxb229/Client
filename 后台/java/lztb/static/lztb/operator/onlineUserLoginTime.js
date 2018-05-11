/**
 * 在线分析- 登录时段分布
 * Created by nbin on 2017/8/8.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    /*分页列表数据*/

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
        //     
        var timeLogin_div = new Vue({
            el: "#timeLogin_div",
            data: {
                filter: {
                   contrastBefourDate: '',
                },    
                grid: {
                    curr_page: 1, //当前页码
                    page_size: 10, //每页最大条数
                    total: 0, //总数
                    pageCount:0,
                    data: [] //当前页数据
                }
            },
            methods: {
                gridFristLoad: function () {  
                    var options = this.filter;
                    $.post("/oper/online/login/loginPageList", options, function (rs) {
                        debugger;
                        console.log(rs);
                        layer.closeAll('loading');
                        timeLogin_div.grid.data = rs.data;  
                        if(isEmpty(rs.data)){
                            AlertMsg("数据为空!");
                        }
                    }, "json");
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/oper/online/login/loginExportExcel?contrastBefourDate=" + vm.filter.contrastBefourDate;
                    $("#timeLogin_form").attr("action", url);
                    $('#timeLogin_form').submit();
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/login/loginEcharts", options, function (rs) {                    
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

                var laydate = layui.laydate;
                var start = {max: laydate.now()};
                document.getElementById('timeLoginDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                }
                //提交  
                form.on('submit(timeLogin_form)', function (data) {
                    timeLogin_div.filter = data.field; 
                    timeLogin_div.gridFristLoad();
                    timeLogin_div.SearchEchart();
                    return false; 
                });
                form.render();
            }

        });

     });


    
    var chart = echarts.init(document.getElementById('timeLogin'));

    var templateChartOption = {
            color: ['#3398DB'],    
            title : {
                text: '时段登录分布'
                
            },
            tooltip : {
                trigger: 'axis'
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
            legend: {
                data:['登录人数']
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    data : []
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'登录人数',
                    type:'bar',
                    data:[],
                    barMaxWidth: 50, 
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    }
                }

            ]
    }    

    /*渲染实时数据图表*/
    function renderer_echarts(rs) {
        debugger;
        var echarts_date;
        if(rs.code!='00000' ||  isEmpty(rs.data)){
            AlertMsg(rs.msg);
        }else{
            echarts_date = eval('(' + rs.data + ')');
            templateChartOption.xAxis[0].data  = echarts_date.xAxis;

            if (echarts_date.xAxis.length > 0) {
                for (var i = 0 ; i < echarts_date.seriesLong.length ;  i++) {
                   templateChartOption.series[i].data = echarts_date.seriesLong[i];
                }
            }else{
                templateChartOption.series[0].data = null;
            }
      }
        chart.setOption(templateChartOption);
    };

    

});
