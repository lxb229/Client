package com.wangzhixuan.controller;

import javax.validation.Valid;

import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.CommodityLv;
import com.wangzhixuan.model.SilverCommodity;
import com.wangzhixuan.model.vo.SilverCommodityVo;
import com.wangzhixuan.service.ICommodityLvService;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.ISilverCommodityService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 银币抽奖商品 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@Controller
@RequestMapping("/silverCommodity")
public class SilverCommodityController extends BaseController {

    @Autowired 
    private ISilverCommodityService silverCommodityService;
    @Autowired
    private ICommodityLvService lvService;
    @Autowired
    private ICommodityService commodityService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/silverCommodity/silverCommodityList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(SilverCommodityVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		silverCommodityService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model) {
    	/**获取所有的商品*/
    	List<Commodity> commodityList = commodityService.getAllCommodity();
    	model.addAttribute("commodityList", commodityList);
    	/**获取所有的有效商品等级*/
    	List<CommodityLv> lvList = lvService.getAllLv();
    	model.addAttribute("commodityLvList", lvList);
    	
        return "admin/silverCommodity/silverCommodityAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SilverCommodity silverCommodity) {
        boolean b = silverCommodityService.insert(silverCommodity);
        if (b) {
            return renderSuccess("添加成功！");
        } else {
            return renderError("添加失败！");
        }
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Integer id) {
        boolean b = silverCommodityService.deleteById(id);
        if (b) {
            return renderSuccess("删除成功！");
        } else {
            return renderError("删除失败！");
        }
    }
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
    	/**获取所有的商品*/
    	List<Commodity> commodityList = commodityService.getAllCommodity();
    	model.addAttribute("commodityList", commodityList);
    	/**获取所有的有效商品等级*/
    	List<CommodityLv> lvList = lvService.getAllLv();
    	model.addAttribute("commodityLvList", lvList);
    	
        SilverCommodity silverCommodity = silverCommodityService.selectById(id);
        model.addAttribute("silverCommodity", silverCommodity);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**转换上架时间*/
        String issuedTime = sdf.format(silverCommodity.getIssuedTime());
        model.addAttribute("issuedTime", issuedTime);
        /**转换下架时间*/
        String soldoutTime = sdf.format(silverCommodity.getSoldoutTime());
        model.addAttribute("soldoutTime", soldoutTime);
        
        return "admin/silverCommodity/silverCommodityEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SilverCommodity silverCommodity) {
        boolean b = silverCommodityService.updateById(silverCommodity);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
