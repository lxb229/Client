var Tool= new Object();

/*
* lestenClass: 触发模态框元素的class
* modelId: 模态框ID
* callList: {
*   parameter:  any, //回调参数设置
*   callback: function, //确定回调
* }
*/
Tool.model= function(lestenClass, modelId, callList){
    $(lestenClass).click(function(){
        $(modelId).modal('show');
        callList && Tool.model_call(lestenClass, modelId, callList, $(this));
    })
}
Tool.model_call= function(lestenClass, modelId, callList, $_el){
    //模态框确定回调-------------触发元素上*必须*要有特殊类名：J_btn-primary
    callList.callback && $(modelId).find(".J_btn-primary").off("click").click(function(){
        callList.callback({
            parameter: callList.parameter || null,
            el: $_el,
        });
    })
    //模态框其他回调
}