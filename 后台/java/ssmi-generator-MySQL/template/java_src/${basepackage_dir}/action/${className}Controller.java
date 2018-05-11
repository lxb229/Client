<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>

package ${basepackage}.controller.${branchpackage};
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;


@Controller
@RequestMapping("/")
public class ${className}Controller extends BaseController {
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist() throws Exception {
    	
    	return AjaxResponse.success();
    }
}
