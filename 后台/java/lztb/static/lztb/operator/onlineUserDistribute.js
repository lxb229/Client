/**
 * 在线分析-每日在线玩家分布
 * Created by nbin on 2017/8/7.
 */

require(['echarts', 'vue', 'site'], function (echarts, Vue) {

    /*分页列表数据*/

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
        
        //     
        var gamersAre_div = new Vue({
            el: "#gamersAre_div",
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
            watch: {
                "grid.curr_page": function (val, oldVal) { //页码改变自动reload
                    if (val === oldVal) {
                        return;
                    }
                    this.gridReload();
                }
            },
            methods: {
                gridFristLoad: function () {
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        gamersAre_div.grid.data = rs.data.list;
                        gamersAre_div.grid.total = rs.recordCount;
                        gamersAre_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', gamersAre_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            gamersAre_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        gamersAre_div.grid.data = rs.data.list;
                        gamersAre_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                   
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/oper/online/distribution/distributionPageList", options, function (rs) {
                        debugger;
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data.list)){
                            gamersAre_div.grid.data = rs.data.list;
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
                    var url = "/oper/online/distribution/distributionExportExcel?contrastBefourDate=" + vm.filter.contrastBefourDate;
                    $("#gamersAre_form").attr("action", url);
                    $('#gamersAre_form').submit();
                },
                SearchEchart: function () {//时长
                    var vm = this;
                    var options = this.filter;
                    $.post("/oper/online/distribution/distributionEcharts", options, function (rs) {                    
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
                document.getElementById('gamersAreDate_a').onclick = function(){
                    start.elem = this;
                    laydate(start);
                }

                //提交  
                form.on('submit(gamersAre_form)', function (data) {
                    gamersAre_div.filter = data.field;
                    gamersAre_div.gridFristLoad();
                    gamersAre_div.SearchEchart();
                    return false; 
                });
                form.render();
            }

        });

     });


    
    var chart = echarts.init(document.getElementById('gamersAre'));

    var templateChartOption = {
        title: {
            "text": "每日在线玩家分布", 
            "subtext": "新玩家 vs 老玩家", 
        }, 
        "tooltip": {
            "trigger": "axis", 
            "axisPointer": {
                "type": "shadow"
            },
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
        "legend": {
          
            "data": ["新玩家","老玩家"]
        }, 
        "calculable": true, 
        xAxis: [
            {
                "type": "category", 
                data: []
            }
        ], 
        "yAxis": [
            {
                "type": "value", 
                "splitLine": {
                    "show": false
                }, 
                "axisLine": {
                    "show": true
                }, 
                "axisTick": {
                    "show": false
                }, 
                "splitArea": {
                    "show": false
                }
            }
        ], 
        "dataZoom": [
                {
                    type: 'inside',
                    start: 0,
                    end: 50
                }, {
                    show: true,
                    type: 'slider',
                    bottom:-5,
                    start: 0,
                    end: 50
                }
        ], 
        series: [
            {
                "name": "新玩家", 
                "type": "bar", 
                "stack": "总量", 
                "barMaxWidth": 50, 
                "barGap": "10%", 
                "itemStyle": {
                    "normal": {
                        "barBorderRadius": 0, 
                        "color": "rgba(60,169,196,0.5)", 
                        "label": {
                            "show": true, 
                            "textStyle": {
                                "color": "rgba(0,0,0,1)"
                            }, 
                            "position": "insideTop",
                            formatter : function(p) {return p.value > 0 ? (p.value ): '';}
                        }
                    }
                }, 
                data:[]
            }, 
            {
                "name": "老玩家", 
                "type": "bar", 
                "stack": "总量", 
                "itemStyle": {
                    "normal": {
                        "color": "rgba(51,204,112,1)", 
                        "barBorderRadius": 0, 
                        "label": {
                            "show": true, 
                            "position": "top",
                            formatter : function(p) {return p.value > 0 ? ('▼' + p.value + ''): ''; }
                        }
                    }
                }, 
                data:[]
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
            if (echarts_date.xAxis.length > 1) {
                for (var i = 0 ; i < echarts_date.seriesInt.length ;  i++) {
                   templateChartOption.series[i].data = echarts_date.seriesInt[i];
                }
            }
      }
        chart.setOption(templateChartOption);
    };
    

});
