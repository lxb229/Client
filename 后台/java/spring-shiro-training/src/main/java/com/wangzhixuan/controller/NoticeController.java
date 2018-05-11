package com.wangzhixuan.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Map;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.wangzhixuan.commons.utils.HumpLineUtils;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.model.Notice;
import com.wangzhixuan.service.INoticeService;
import com.alibaba.fastjson.JSONObject;
import com.wangzhixuan.commons.base.BaseController;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-20
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
	
	@Autowired
	private PropertyConfigurer configurer;

    @Autowired private INoticeService noticeService;
    
    @GetMapping("/manager")
    public String manager() {
        return "admin/notice/noticeList";
    }
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Notice notice, Integer page, Integer rows, String sort,String order) {
    	if(StringUtils.isNotBlank(sort)) {
        	sort = HumpLineUtils.humpToLine(sort);
        }
    	PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(notice.getName())) {
            condition.put("name", notice.getName());
        }
        pageInfo.setCondition(condition);
        noticeService.selectDataGrid(pageInfo);
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
    public String addPage() {
        return "admin/notice/noticeAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Notice notice, HttpServletRequest request) {
        notice.setCreateTime(new Date());
        notice.setTitleImg(configurer.getProperty("basePath")+notice.getTitleImg());
        notice.setContentImg(configurer.getProperty("basePath")+notice.getContentImg());
        boolean b = noticeService.insertNotice(notice);
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
    public Object delete(int id) {
    	Notice notice = noticeService.selectById(id);
        boolean b = noticeService.deleteNotice(notice);
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
        Notice notice = noticeService.selectById(id);
        model.addAttribute("notice", notice);
        return "admin/notice/noticeEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Notice notice) {
//        notice.setUpdateTime(new Date());
        boolean b = noticeService.updateById(notice);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
}
