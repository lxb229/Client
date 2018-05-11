/**
 * 在线分析 - 在线用户明细
 * Created by nbin on 2017/8/7.
 */

 require(['vue','layui','site'], function (Vue) {
    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer'], function () {
        var app = new Vue({
            el: "#app",
            data: {
                filter: {
                    
                },    
                grid: {
                    data: [] //当前页数据
                }
            },
            methods: {
                gridFristLoad: function () {  
                    var options = this.filter;
                    $.post("/oper/online/detail/detailPageList", options, function (rs) {
                        console.log(rs);
                        layer.closeAll('loading');
                        if(isEmpty(rs.data)){
                            AlertMsg("数据为空!");
                        }else{
                            app.grid.data = rs.data;       
                        }
                    }, "json");
                },
                exportExcel: function(){
                    var url = "/oper/online/detail/detailExportExcel";
                    $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();

                }
            },
            created: function () {
                layer.load(0, {shade: false});
                this.gridFristLoad();
            }

        });

     });

}); 
