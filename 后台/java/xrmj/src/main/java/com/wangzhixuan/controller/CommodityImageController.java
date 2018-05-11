package com.wangzhixuan.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Map;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.shiro.ShiroUser;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Commodity;
import com.wangzhixuan.model.CommodityImage;
import com.wangzhixuan.model.vo.CommodityImageVo;
import com.wangzhixuan.service.ICommodityImageService;
import com.wangzhixuan.service.ICommodityService;
import com.alibaba.fastjson.JSONObject;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 商品图片 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@Controller
@RequestMapping("/commodityImage")
public class CommodityImageController extends BaseController {

	@Autowired
	private PropertyConfigurer configurer;
    @Autowired 
    private ICommodityImageService commodityImageService;
    @Autowired
    private ICommodityService commodityService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/commodityImage/commodityImageList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(CommodityImageVo vo, Integer page, Integer rows, String sort,String order) {
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(vo.getCommodityName())) {
			condition.put("commodityName", vo.getCommodityName());
		}
		pageInfo.setCondition(condition);
		commodityImageService.selectDataGrid(pageInfo);
		return pageInfo;
    }
    
    @PostMapping("/uploadImage")
    public void uploadImage(@RequestParam("file")MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("进入上传");
    	try {  
            // 获取图片原始文件名  
            String originalFilename = file.getOriginalFilename();  
          
            // 文件名使用当前时间  
            String name = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());  
          
            // 获取上传图片的扩展名(jpg/png/...)  
            String extension = FilenameUtils.getExtension(originalFilename);  
              
            // 图片上传的相对路径（因为相对路径放到页面上就可以显示图片）  
            String path = "/"+configurer.getProperty("stuff")+"/" + name + "." + extension;  
  
            // 图片上传的绝对路径  
//            String url = request.getSession().getServletContext().getRealPath("") + path;  
            String url = configurer.getProperty("uploadPath") +"/" + name + "." + extension;
            
            // 上传图片  
            file.transferTo(new File(url));  
          
            // 将相对路径写回（json格式）  
            JSONObject jsonObject = new JSONObject();  
            // 将图片上传到本地  
            jsonObject.put("path", path);  
          
            // 设置响应数据的类型json  
            response.setContentType("application/json; charset=utf-8");  
            // 写回  
            response.getWriter().write(jsonObject.toString());  
  
        } catch (Exception e) {  
        	e.printStackTrace();
            throw new RuntimeException("服务器繁忙，上传图片失败");  
        } 
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
    	
        return "admin/commodityImage/commodityImageAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid CommodityImage commodityImage) {
    	ShiroUser user = getShiroUser();
    	if(user != null) {
    		commodityImage.setUserId(user.getId().intValue());
    	}
    	commodityImage.setCreateTime(new Date());
    	commodityImage.setImageUrl(configurer.getProperty("basePath")+commodityImage.getImageUrl());
        boolean b = commodityImageService.insert(commodityImage);
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
        boolean b = commodityImageService.deleteById(id);
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
    	
        CommodityImage commodityImage = commodityImageService.selectById(id);
        model.addAttribute("commodityImage", commodityImage);
        return "admin/commodityImage/commodityImageEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid CommodityImage commodityImage) {
    	commodityImage.setImageUrl(configurer.getProperty("basePath")+commodityImage.getImageUrl());
        boolean b = commodityImageService.updateById(commodityImage);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
