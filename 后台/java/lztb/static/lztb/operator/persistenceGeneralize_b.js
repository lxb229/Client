/**
 * 留存分析 -推广阶段-二维码推广
 * Created by nbin on 2017/08/21.
 */

require(['vue','layui','site'], function (Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {

          var persistenceGeneralize_div_b = new Vue({
            el: "#persistenceGeneralize_div_b",
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
                        persistenceGeneralize_div_b.grid.data = rs.data.list;
                        persistenceGeneralize_div_b.grid.total = rs.recordCount;
                        persistenceGeneralize_div_b.grid.pageCount = rs.pageCount;                                 
                        gLayPageIni('pagerc', persistenceGeneralize_div_b.grid.curr_page ,rs.data.pageCount , function (newPage) {
                            persistenceGeneralize_div_b.grid.curr_page = newPage;
                        });
                    });
                },
                gridReload: function () { //分页获取
                    this.fetchData(function (rs) {
                        persistenceGeneralize_div_b.grid.data = rs.data.list;
                        persistenceGeneralize_div_b.grid.total = rs.recordCount;
                    });
                },
                fetchData: function (cb) {  
                    var options = this.filter;
                    options.pageNo = this.grid.curr_page;
                    options.pageSize = this.grid.page_size;
                    $.post("/player/persistence/getOperationRemainChannelPageList", options, function (rs) {
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
                    var url = "/player/persistence/getOperationRemainChannelExportExcel";
                    $("#persistenceGeneralize_form_b").attr("action", url);
                    $('#persistenceGeneralize_form_b').submit();
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
                document.getElementById('startDate_b').onclick = function(){
                  start.elem = this;
                  laydate(start);
                }
                document.getElementById('endDate_b').onclick = function(){
                  end.elem = this
                  laydate(end);
                }
                form.render('select'); 

                form.on('submit(persistenceGeneralize_form_b)', function (data) {  
                    var vm = this;
                    persistenceGeneralize_div_b.filter = data.field; 
                    persistenceGeneralize_div_b.gridFristLoad();
                    return false; //阻止原生表单跳转
                });
                form.render();
                
            }

        });

    });

 });

