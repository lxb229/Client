/**
 * 付费排行分析 - 自定义排行
 * Created by nbin on 2017/08/21.
 */
require(['vue','layui','site'], function (Vue) {
           
    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {

      var history_div = new Vue({
        el: "#history_div",
        data: {
            filter: {
                startDate: '',
                endDate: '',
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
                    history_div.grid.data = rs.data.list;
                    history_div.grid.total = rs.recordCount;
                    history_div.grid.pageCount = rs.pageCount;                                 
                    gLayPageIni('pagerc', history_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                        history_div.grid.curr_page = newPage;
                    });
                });
            },
            gridReload: function () { //分页获取
                this.fetchData(function (rs) {
                    history_div.grid.data = rs.data.list;
                    history_div.grid.total = rs.recordCount;
                });
            },
            fetchData: function (cb) {  
                debugger;
                var options = this.filter;
                options.pageNo = this.grid.curr_page;
                options.pageSize = this.grid.page_size;
                $.post("/player/pay/getHistoryList", options, function (rs) {
                    layer.closeAll('loading');
                    console.log(rs);
                    if(isEmpty(rs.data)){
                        history_div.grid.data = rs.data.list;
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
                var url = "/player/pay/getHistoryExportExcel";
                $("#history_form").attr("action", url);
                $('#history_form').submit();
            }
          
        },
        created: function () {
            layer.load(0, {shade: false});
            this.gridFristLoad();
        },
        mounted: function () {
            var vm = this; 
            //初始layerdate
            var form = layui.form();
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
            var startlonginTime = { max: laydate.now(-1)};
            var endlonginTime = { max: laydate.now(-1)};
            document.getElementById('longinTimeStart_c').onclick = function(){
              startlonginTime.elem = this;
              laydate(startlonginTime);
            }
            document.getElementById('longinTimeEnd_c').onclick = function(){
              endlonginTime.elem = this
              laydate(endlonginTime);
            }

            form.on('submit(history_form)', function (data) {  
                var vm = this;
                history_div.filter = data.field; 
                history_div.gridFristLoad();
                return false; //阻止原生表单跳转
            });
            form.render();
              
          }

      });


    });

});