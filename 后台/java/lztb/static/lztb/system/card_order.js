require(['vue', 'layui', 'site'], function (Vue) {

	var layuiForm;
	layui.config({ dir: baseUrl + '../plugs/layui/' });
	layui.use(['layer', 'laypage', 'form', 'laydate'], function () {
		layuiForm = layui.form();
		var app = new Vue({
			el: "#addstudent",
			data: {
				grid: {
					curr_page: 1, //当前页码
					page_size: 10, //每页最大条数
					total: 0, //总数
					pageCount: 0,
					data: [] //当前页数据
				},
				filter: {}
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
				gridFristLoad: function () {
					this.countOrder();
					this.curr_page = 1;
					this.fetchData(function (rs) {
						app.grid.data = rs.data.list;
						app.grid.total = rs.recordCount;
						app.grid.pageCount = rs.pageCount;
						gLayPageIni('pager', app.grid.curr_page, rs.data.pageCount, function (newPage) {
							app.grid.curr_page = newPage;
						});
					});
				},
				gridReload: function () { //分页获取
					this.countOrder();
					this.fetchData(function (rs) {
						app.grid.data = rs.data.list;
						app.grid.total = rs.recordCount;
					});
					
				},
				fetchData: function (cb) {
					var options = this.filter;
					options.pageNo = this.grid.curr_page;
					options.pageSize = this.grid.page_size;
					$.post("/systemOrderController/queryPagelist", options, function (rs) {
						cb(rs);
					}, "json");
				},
				onSearch: function (event) {
					this.gridReload();
				},
				countOrder: function () {
					$.ajax({
						url: '/systemOrderController/countAmount',
						type: 'POST',
						data: this.filter,
						dataType: 'json',
						timeout: 5000,
						success: function (rs) {
							if (rs.code == '00000') {
								console.log(rs.data);
								$("#orderCount").html(rs.data.orderNum);
								$("#orderAmount").html(rs.data.orderAmount);
								$("#agencyCount").html(rs.data.agencyNum);
								$("#agencyAmount").html(rs.data.agencyAmount);
							} else {
								console.error(rs);
								AlertMsg(rs.msg);
							}
						},
						error: function (xhr, status, ex) {
							console.error(xhr);
							console.error(status);
							console.error(ex);
							AlertMsg("请求失败：" + xhr.statusText);
						}
					});
				},
				cash: function () {
					ConfirmBox("您确定提现吗？", buy)
					function buy() {
						$.ajax({
							url: '/systemOrderController/cash',
							type: 'POST',
							data: this.filter,
							dataType: 'json',
							timeout: 10000,
							success: function (rs) {
								if (rs.code == '00000') {
									SuccessBox("提现成功!", "提示", 2000);
									this.gridReload();
								} else {
									AlertMsg(rs.msg);
									console.error(rs);
								}
							},
							error: function (xhr, status, ex) {
								AlertMsg("请求失败：" + xhr.statusText);
								console.error(ex);
							}
						});
					}
				}
				
			},
			created: function () {
				this.gridFristLoad();
			}
		});
	});
});