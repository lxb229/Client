/**
 * 付费数据分析
 * Created by nbin on 2017/8/21.
 */

 require(['vue','layui','site'], function (Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
    
        var payData_div = new Vue({
            el: "#payData_div",
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
                formatDate(time) {
                   var date = new Date(time);
                  return formatDate(date, 'yyyy-MM-dd');
                },
                gridFristLoad: function () { 
                    this.curr_page = 1;
                    this.fetchData(function (rs) {
                        payData_div.grid.data = rs.data.list;
                        payData_div.grid.total = rs.recordCount;
                        payData_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', payData_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            payData_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        payData_div.grid.data = rs.data.list;
                        payData_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/pay/getOperationPlayerPayPageList", options, function (rs) {
                        layer.closeAll('loading');
                        console.log(rs);
                        if(isEmpty(rs.data)){
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
                    var url = "/player/pay/getOperationPlayerPayExportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#payData_form").attr("action", url);
                    $('#payData_form').submit();
                }
              
            },
            created: function () {
                layer.load(0, {shade: false});
                this.gridFristLoad();
            },
            mounted: function () {
                var vm = this;
                var form = layui.form();
                
                //初始layerdate
                  var laydate = layui.laydate;
                  var start = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  var end = { max: laydate.now(-1)};
                  
                  document.getElementById('payDate_start').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('payDate_end').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }
                
                //提交  
                form.on('submit(payData_form)', function (data) {
                    
                    var vm = this;
                    payData_div.filter = data.field; 
                    payData_div.gridFristLoad();
                    return false; //阻止原生表单跳转
                });
                form.render();
            }

        });

     });

}); 
