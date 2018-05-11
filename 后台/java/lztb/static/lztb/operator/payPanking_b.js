/**
 * 付费排行分析 - 今日
 * Created by nbin on 2017/08/21.
 */

require(['vue','layui'], function (Vue) {
 
      var ranking_div = new Vue({
        el: "#ranking_div",
        data: {    
            grid: {
                data: []
            }
        },
        methods: {
            gridFristLoad: function () {        
                $.get("/player/pay/getRankingList",function (rs) {
                    ranking_div.grid.data = rs.data; 
                }, "json");
            },
            onSearch: function(event){
                this.gridReload();
            },
            exportExcel: function(){
                var vm = this;
                var url = "/player/pay/getRankingExportExcel";
                $("#ranking_form").attr("action", url);
                $('#ranking_form').submit();
            }
          
        },
        created: function () {
            this.gridFristLoad();
        }
    });


 });
