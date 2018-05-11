// 搜索选项卡切换
$('.table .nav-tabs a').click(function (){
	$(this).parent().siblings().removeClass('active');
	$(this).parent().addClass('active');
	// 设置type值
	$('.table #type').val($(this).attr('data'));
	
	search(getUrl(), getParams());
});

// 全选效果
$('.table .check-box-group .check-box').click(function() {
	if(!$(this).hasClass('checked')) {
		$(this).addClass('checked');
		// 列表联动效果
		$('.table .table_data .check-box').addClass('checked');
	} else {
		$(this).removeClass('checked');
		// 列表联动效果
		$('.table .table_data .check-box').removeClass('checked');
	}
});

// 列表数据选中效果
$(document).off('click', '.table .table_data .check-box').on('click', '.table .table_data .check-box', function(){
	if(!$(this).hasClass('checked')) {
		$(this).addClass('checked');
	} else {
		$(this).removeClass('checked');
		// 只要有取消选中，上面的全选清空
		$('.table .check-box-group .check-box').removeClass('checked');
	}
});
/**
 * 获取列表选中id集合
 */
function getCheckedIds() {
	var ids = '';
	$('.table .table_data .checked').each(function() {
		ids += ',' + $(this).attr('data');
	});
	if(ids.length == 0) {
		$('#base_show_message_modal .modal-body').html('请选择信息');
		$('#base_show_message_modal').modal('show');
	} else {
		ids = ids.substring(1);
	}
	return ids;
}

/**
 * 获取当前行id信息(多选框支持)
 * @param 行元素
 */
function getLineId(element) {
	var id = $(element).closest('tr').find('td').first().find('span').attr('data');
	return id;
}

//列表搜索
function search(url, data) {
	$('.table .check-box-group .check-box').removeClass('checked');
	$('.form table .table_data').empty();
	$('.table .pages').empty();
	$.post(url, data, function(response) {
		console.log(response);
		var data = response.data;
		if(data != null && data != undefined) {
			// 设置列表数据
			var trStr = '';
			for(var r = 0; r < data.length; r ++) {
				trStr += '<tr class="tr-content">';
				$('.form table th').each(function () {
					var tag_class = $(this).attr('class'); // 属性标签
					var tag_field = $(this).attr('field'); // 数据列名
					var tag_type = $(this).attr('type'); // 数据类型
					var tag_data = $(this).attr('data'); // 自定义变量数据
					var colspan = $(this).attr('colspan'); // 单元格合并
					trStr += '<td';
						if(tag_class != undefined) { trStr += ' class="'+tag_class+'"'; }
						if(colspan != null) { trStr += ' colspan="'+colspan+'"'; }
					trStr += '>';
					
					// 前置图片
					var befourImg = $(this).attr('befour_img');
					if(befourImg != null) {
						trStr += '<img class="user-head-30" src="'+data[r][befourImg]+'" />';
					}
					
					// 自定义数据
					if(tag_data != undefined) {
						trStr += eval(tag_data);
					} else if(tag_field != undefined){
						if(data[r][tag_field] != undefined) {
							// 字段数据
							if(tag_type == 'id') {  // id类型
								trStr += '<span class="check-box" data="'+data[r][tag_field]+'"></span>&nbsp;&nbsp;'+data[r][tag_field];
							} else if(tag_type == 'date') { // 日期类型
								trStr +=  fmtDate(data[r][tag_field]);
							} else if(tag_type == 'map') { // 键值对类型
								var map = eval('('+$(this).attr('map')+')');
								// 首先获取键值对
								var value = map[data[r][tag_field]];
								if(value == undefined) {
									// 没有键值对时获取默认值
									value = map['defaultVale'];
									if(value == undefined) {
										// 未定义默认值时，显示本身
										value = data[r][tag_field];
									}
								}
								trStr += value;
							} else if(tag_type == 'img') { // 图片类型
								var css = $(this).attr('img_style');
								css = css==undefined ? 'user-head-30' : css;
								trStr += '<img class="'+css+'" src="'+data[r][tag_field]+'" />';
							} else {
								var value = data[r][tag_field];
								if(value != undefined) {
									trStr += value;
								}
							}
						}
					}
					trStr += '</td>';
					
				});
				trStr += '</tr>';
			}
			$('.form table .table_data').html(trStr);
			
			// 翻页信息设置
			setPages(response.nowPage, response.pages, response.total);
		} 
	});
}

// 设置分页信息
function setPages(nowPage, pages, total) {
	// 设置当前页码
	$('.form input[name=page]').val(nowPage);
	if(pages > 1) {
		// 设置分页信息
		var divStr = '<div class="col-xs-11 page">';
		// 获取5个快捷翻页
		var startShow = nowPage - 2;
		var endShow = nowPage + 2;
		if (startShow < 1) {
			endShow += (1 - startShow);
			startShow = 1;
		}
		if (endShow > pages) {
			endShow = pages;
		}
		// 开始初始化快捷页码
		if (nowPage > 1) {
			divStr += '<span class="pointer prev" onclick="upPage()">上一页</span> ';
		}
		for (var r = startShow; r <= endShow; r++) {
			divStr += '<span class="pointer number showNum';
			if (r === nowPage) {
				divStr += ' active';
			}
			divStr += '">' + r + '</span>';
		}
		if (endShow < pages) {
			divStr += '<span class="more">...</span>';
		}
		// 下一页
		if (pages > nowPage) {
			divStr += '<span class="pointer next" onclick="nextPage()">下一页</span> ';
		}
		//divStr += '<span class="jump"> 跳转到：<input type="text" ></span>';
		divStr += '</div>';
		$('.table .pages').html(divStr);
	}
	$('.table .pages').append('<div class="col-xs-1 total text-right">共' + total + '条</div>');
}

// 上一页
function upPage() {
	var page = $('.table input[name=page]').val();
	page = page=='' ? 1 : page;
	page = page -1;
	$('.table input[name=page]').val(page);
	
	search(getUrl(), getParams());
}
// 下一页
function nextPage() {
	var page = $('.table input[name=page]').val();
	console.log("1--->"+page);
	page = page =='' ? 1 : page;
	console.log("2--->"+page);
	page = parseInt(page) +1;
	console.log("3--->"+page);
	$('.table input[name=page]').val(page);
	
	search(getUrl(), getParams());
}
// 跳转到指定页面
$(document).off('click', '.form .pages .showNum').on('click', '.form .pages .showNum', function(){
	$('.table input[name=page]').val($(this).text());
	
	search(getUrl(), getParams());
});



/**
 * @param 日期格式转换
 * @returns {String}
 */
function fmtDate(obj){
    var date =  new Date(obj);
    var y = 1900+date.getYear();
    var m = "0"+(date.getMonth()+1);
    var d = "0"+date.getDate();
    return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
}