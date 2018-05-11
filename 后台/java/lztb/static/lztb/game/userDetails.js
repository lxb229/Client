
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
					$.post("/backstage/game/queryUserDetails", options, function (rs) {
						console.log(rs);
						cb(rs);
					}, "json");
				}
			},
			created: function () {
				this.gridFristLoad();
			},
			mounted: function () {
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