/**
 * 服务分类-- 图标
 * @param data 
 * @returns {___anonymous53_1144}
 */
function getEchart(data) {
	var option = {
            color: ['#F182C7'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'直接访问',
                    type:'bar',
                    barWidth: '60%',
                    data:data
                }
            ]
        };
	return option;
}

// 模态框列表选中效果
$(document).off('click', '.modal .modal-content ul .check-box').on('click', '.modal .modal-content ul .check-box', function(){
	if(!$(this).hasClass('checked')) {
		$(this).addClass('checked');
	} else {
		$(this).removeClass('checked');
	}
});