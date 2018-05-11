/**
 * 邮件排行
 * Created by yal on 2017/8/16.
 */
require(['vue','layui','site'], function (Vue) {
	var layuiForm;
	layui.config({dir: baseUrl + '../plugs/layui/'});
	layui.use(['layer','laypage','form', 'laydate'], function () {
		layuiForm = layui.form();
		var app = new Vue({
			el: "#app",
			data: {
				grid: {
					curr_page: 1, //当前页码
					page_size: 10, //每页最大条数
					total: 0, //总数
					pageCount:0,
					data: [], //当前页数据
				},
				filter: {
					
				},
				selected:'1'
			},
			watch: {
				"grid.curr_page": function (val, oldVal) { //页码改变自动reload
					debugger;
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
				gridFristLoad: function () { //表格首次加载(fetch data of first page -> set data -> initialize pager)
					this.curr_page = 1;
					this.fetchData(function (rs) {
						app.grid.data = rs.data.list;
						app.grid.total = rs.recordCount;
						app.grid.pageCount = rs.pageCount;		
						gLayPageIni('pager', app.grid.curr_page ,rs.data.pageCount , function (newPage) {
							app.grid.curr_page = newPage;
						});
					});
				},
				gridReload: function () { //分页获取
					this.fetchData(function (rs) {
						app.grid.data = rs.data.list;
						app.grid.total = rs.recordCount;
					});
				},
				fetchData: function (cb) {
					var options = this.filter;
					options.pageNo = this.grid.curr_page;
					options.pageSize = this.grid.page_size;
					options.startAt = this.grid.startAt;
					options.endAt = this.grid.endAt;
					$.post("/backstage/game/queryEconomicProfilePageList", options, function (rs) {
						console.log(rs);
						cb(rs);
					}, "json");
				}
			},
			created: function () {
				this.gridFristLoad();
			},
			mounted: function () {
				var vm = this;
				var form = layui.form();
				//初始layerdate
				var laydate = layui.laydate;
				var start = {
					min: '1977-06-16 23:59:59'
					,max: '2099-06-16 23:59:59'
					,istoday: false
					,choose: function(datas){
					end.min = datas; //开始日选好后，重置结束日的最小日期
					end.start = datas //将结束日的初始值设定为开始日
					}
				};
				var end = {
					min: '1977-06-16 23:59:59'
					,max: '2099-06-16 23:59:59'
					,istoday: false
					,choose: function(datas){
						start.max = datas; //结束日选好后，重置开始日的最大日期
					}
				};
				document.getElementById('startAt').onclick = function(){
					start.elem = this;
					laydate(start);
				}
				document.getElementById('endAt').onclick = function(){
					end.elem = this
					laydate(end);
				}
				//提交  
				form.on('submit', function (data	) {
					app.filter = data.field; //data.field为当前容器的全部表单字段，名值对形式：{name: value}
					app.gridFristLoad();
					//return false; //阻止原生表单跳转
				});
			}
		});

	 });
});		