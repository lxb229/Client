/**
 * 实时数据数据列表
 * Created by nbin on 2017/8/7.
 */

 require(['vue','layui','site'], function (Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {
    
        var realTime_div = new Vue({
            el: "#realTime_div",
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
                        realTime_div.grid.data = rs.data.list;
                        realTime_div.grid.total = rs.recordCount;
                        realTime_div.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pager', realTime_div.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            realTime_div.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        realTime_div.grid.data = rs.data.list;
                        realTime_div.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/oper/realTime/queryPagelist", options, function (rs) {
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
                    var url = "/oper/realTime/exportExcel?startDate=" + vm.filter.startDate + "&endDate=" + vm.filter.endDate;
                    $("#realTime_form").attr("action", url);
                    $('#realTime_form').submit();
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
                  var start = { max: laydate.now(1)};
                  var end = { max: laydate.now(1)};
                  
                  document.getElementById('startAt').onclick = function(){
                    start.elem = this;
                    laydate(start);
                  }
                  document.getElementById('endAt').onclick = function(){
                    end.elem = this
                    laydate(end);
                  }
                
                //提交  
                form.on('submit(realTime_form)', function (data) {
                    
                    var vm = this;
                    realTime_div.filter = data.field; 
                    realTime_div.gridFristLoad();
                    return false; //阻止原生表单跳转
                });
                form.render();
            }

        });

     });

}); 
