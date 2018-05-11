
package com.guse.platform.controller.doudou;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.service.doudou.MQService;


@Controller
@RequestMapping("/mqController")
public class MQController extends BaseController {
	@Autowired
	private MQService mqService;
	
	@RequestMapping(value = "/starConsumer", method = RequestMethod.GET)
    public @ResponseBody Object starConsumer()throws Exception  {
		mqService.startConsumer();
		return new AjaxResponse(Constant.CODE_SUCCESS, "消费者启动！");
	}

}
