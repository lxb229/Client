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
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.GoldCommodity;
import com.wangzhixuan.model.vo.GoldCommodityVo;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.IGoldCommodityService;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 金币兑换商品 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@Controller
@RequestMapping("/goldCommodity")
public class GoldCommodityController extends BaseController {

    @Autowired 
    private IGoldCommodityService goldCommodityService;
    @Autowired
    private ICommodityService commodityService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/goldCommodity/goldCommodityList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(GoldCommodityVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		goldCommodityService.selectDataGrid(pageInfo);
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
    	
        return "admin/goldCommodity/goldCommodityAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid GoldCommodity goldCommodity) {
    	goldCommodity.setResidue(goldCommodity.getAmount());
    	Result result = goldCommodityService.insertGoldCommodity(goldCommodity);
        if (result != null && result.isSuccess()) {
            return renderSuccess("添加成功！");
        } else {
            return renderError(result.getMsg());
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
    	Result result = goldCommodityService.deleteGoldCommodity(id);
        if (result != null && result.isSuccess()) {
            return renderSuccess("删除成功！");
        } else {
            return renderError(result.getMsg());
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
    	
        GoldCommodity goldCommodity = goldCommodityService.selectById(id);
        model.addAttribute("goldCommodity", goldCommodity);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**转换上架时间*/
        String issuedTime = sdf.format(goldCommodity.getIssuedTime());
        model.addAttribute("issuedTime", issuedTime);
        /**转换下架时间*/
        String soldoutTime = sdf.format(goldCommodity.getSoldoutTime());
        model.addAttribute("soldoutTime", soldoutTime);
        
        return "admin/goldCommodity/goldCommodityEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid GoldCommodity goldCommodity) {
        boolean b = goldCommodityService.updateById(goldCommodity);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
