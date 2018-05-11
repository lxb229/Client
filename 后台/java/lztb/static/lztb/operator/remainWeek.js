/**
 * 留存分析 - 按周统计
 * Created by nbin on 2017/08/21.
 */

require(['vue','layui','site'], function (Vue) {

    layui.config({dir: baseUrl + '../plugs/layui/'});
    layui.use(['layer','laypage','form','laydate'], function () {

          var remainWeek_div = new Vue({
            el: "#remainWeek_div",
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
                  return formatDate(date, 'yyyy-MM-dd');
                },
                gridFristLoad: function () {
                    var options = this.filter;
                    $.post("/player/persistence/getOperationRemainWeekList", options, function (rs) {
                        layer.closeAll('loading');
                        console.log(rs);
                         remainWeek_div.grid.data = rs.data;    
                        if(isEmpty(rs.data)){
                            AlertMsg("数据为空!");
                        }
                        
                    }, "json");
                },
                exportExcel: function(){
                    var vm = this;
                    var url = "/player/persistence/getOperationRemainWeekExportExcel";
                    $("#remainWeek_form").attr("action", url);
                    $('#remainWeek_form').submit();
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

                document.getElementById('startDate_b').onclick = function(){
                  start.elem = this;
                  laydate(start);
                }
                document.getElementById('endDate_b').onclick = function(){
                  end.elem = this
                  laydate(end);
                }

                form.on('submit(remainWeek_form)', function (data) {  
                    var vm = this;
                    remainWeek_div.filter = data.field; 
                    remainWeek_div.gridFristLoad();
                    return false; //阻止原生表单跳转
                });
                form.render();
                
            }

        });

    });

 });


