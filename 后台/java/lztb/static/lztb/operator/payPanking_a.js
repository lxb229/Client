/**
 * 付费排行分析 -实时订单
 * Created by nbin on 2017/08/21.
 */

require(['vue','layui','site'], function (Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {

          var recharge_div = new Vue({
            el: "#recharge_div",
            data: {
                filter: {
                    startDate: '',
                    endDate: '',
                },    
                grid: {
                    curr_page: 1, //当前页码
                    page_size: 20, //每页最大条数
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
                        recharge_div.grid.data = rs.data.list;
                        recharge_div.grid.total = rs.recordCount;
                        recharge_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', recharge_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            recharge_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        recharge_div.grid.data = rs.data.list;
                        recharge_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/pay/getRechargeList", options, function (rs) {
                        layer.closeAll('loading');
                        if(isEmpty(rs.data)){
                            recharge_div.grid.data = rs.data.list;
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
                    var url = "/player/pay/getRechargeExportExcel";
                    $("#recharge_form").attr("action", url);
                    $('#recharge_form').submit();
                }
              
            },
            created: function () {
                layer.load(0, {shade: false});
                this.gridFristLoad();
            },
            mounted: function () {
                var vm = this; 
                var form = layui.form();
                var laydate = layui.laydate;
                var start = { max: laydate.now(-1)};
                var end = { max: laydate.now(-1)};
                var end = { max: laydate.now(-1)};
                form.render('select'); 

                document.getElementById('startDate_a').onclick = function(){
                  start.elem = this;
                  laydate(start);
                }
                document.getElementById('endDate_a').onclick = function(){
                  end.elem = this
                  laydate(end);
                }

                form.on('submit(recharge_form)', function (data) {  
                    var vm = this;
                    recharge_div.filter = data.field; 
                    recharge_div.gridFristLoad();
                    return false; //阻止原生表单跳转
                });
                form.render();
                
            }

        });

    });

 });

