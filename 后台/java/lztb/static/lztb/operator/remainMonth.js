/**
 * 留存分析 - 按月统计
 * Created by nbin on 2017/08/21.
 */

require(['vue','layui','site'], function (Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {

          var remainMonth_div = new Vue({
            el: "#remainMonth_div",
            data: {
                filter: {
                    startDate: '',
                    endDate: '',
                },    
                grid: {
                    data: [] //当前页数据
                }
            },
            methods: {
                formatDate(time) {
                   var date = new Date(time);
                  return formatDate(date, 'yyyy-MM');
                },
                gridFristLoad: function () {
                    var options = this.filter;
                    $.post("/player/persistence/getOperationRemainMonthList", options, function (rs) {
                        layer.closeAll('loading');
                        console.log(rs);
                         remainMonth_div.grid.data = rs.data;    
                        if(isEmpty(rs.data)){
                            AlertMsg("数据为空!");
                        }
                        
                    }, "json");
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/player/persistence/getOperationRemainMonthExportExcel";
                    $("#remainMonth_form").attr("action", url);
                    $('#remainMonth_form').submit();
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
                form.render('select'); 

                document.getElementById('startDate_c').onclick = function(){
                  start.elem = this;
                  laydate(start);
                }
                document.getElementById('endDate_c').onclick = function(){
                  end.elem = this
                  laydate(end);
                }

                form.on('submit(remainMonth_form)', function (data) {  
                    var vm = this;
                    remainMonth_div.filter = data.field; 
                    remainMonth_div.gridFristLoad();
                    return false; //阻止原生表单跳转
                });
                form.render();
                
            }

        });

    });

 });

