/**
 * 龙珠探宝
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
				checkedItem:[],
				grid: {
					curr_page: 1, //当前页码
					page_size: 10, //每页最大条数
					total: 0, //总数
					pageCount:0,
					data: [], //当前页数据
				},
				filter: {
					
				},
				gpodPropType:''
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
						app.grid.data = rs.page_list;
						app.grid.total = rs.page;
						app.grid.pageCount = rs.total_page;		
						gLayPageIni('pager', app.grid.curr_page ,rs.total_page , function (newPage) {
							app.grid.curr_page = newPage;
						});
					});
				},
				gridReload: function () { //分页获取
					this.fetchData(function (rs) {
						app.grid.data = rs.page_list;
						app.grid.total = rs.page;
					});
				},
				fetchData: function (cb) {
					var options = this.filter;
					options.pageNo = this.grid.curr_page;
					options.pageSize = this.grid.page_size;
					options.startAt = this.grid.startAt;
					options.endAt = this.grid.endAt;
					$.post("/gm/forbid/lztbGameWords", options, function (rs) {
						console.log(rs);
						cb(rs);
					}, "json");
				},
				//全选，反选
				selectAll: function () {
					$("#list :checkbox").prop("checked", true);
				},
				reverse: function () { 
					$("#list :checkbox").each(function () {
						$(this).prop("checked", !$(this).prop("checked"));
					});
				},
				delBatch:function() {
					$('input:checkbox[name=userCheck]:checked').each(function(i){
						app.checkedItem.push($(this).val()) ;
					});
					if(app.checkedItem.length < 1){
						AlertMsg('请选择评论');
						return;
					}
					ConfirmBox("您确定要删除？", function(){
						app.delBatchExcute();
					});
                },
				delBatchExcute(){
					console.log(app.checkedItem);
					var ids = '';
					for (var i = 0; i < app.checkedItem.length; i++) {
						   ids = ids + app.checkedItem[i]+",";
					}
					console.log(ids);
					$.ajax({
						url: '/gm/forbid/delLztbWords',
						type: 'POST',
						data: {'delIds':ids},
						dataType: 'json',
						timeout: 100000,
						traditional: true,
						success: function (rs) {
							if (rs.code == '00000') {
								//alert(rs.msg);
								app.gridReload();
                            } else {
                                console.log(rs.msg);
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
				delData: function (gmdId) {
					var msg = "确定要删除吗？";
					var title = "删除邮件流水！";
					ConfirmBox(msg, function () {
						$.ajax({
							url: '/gm/forbid/delLztbWords',
							type: 'POST',
							data: {'delIds':gmdId},
							dataType: 'json',
							timeout: 10000,
							success: function (rs) {
								if (rs.code == '00000') {
									app.gridReload();
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
					}, title, function(){});
				}
			},
			created: function () {
				this.gridFristLoad();
			},
			mounted: function () {
				var vm = this;
				var form = layui.form();
				//提交  
				form.on('submit', function (data) {
					app.filter = data.field; //data.field为当前容器的全部表单字段，名值对形式：{name: value}
					app.gridFristLoad();
					return false; //阻止原生表单跳转
				});
			}
		});

	 });
});		