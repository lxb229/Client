package com.guse.platform.controller.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.guse.platform.common.base.Result;


/**
 * 编辑器
 * @author liyang
 *
 */
@Controller
@RequestMapping("/ueditor")
public class UeditorController {
	private static final Logger logger = LoggerFactory.getLogger(UeditorController.class);
	
	
	
	 @RequestMapping("/upload")
	  @ResponseBody
	  public String imageUpload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,
	      HttpServletResponse response) {
	    logger.info("WebsiteController.imageUpload()");
	    System.out.println(JSON.toJSONString(file));
	    response.reset();
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Request-Method", "POST");
	    response.setHeader("Pragma", "No-cache");
	    response.setHeader("Cache-Control", "no-cache");
	    response.setContentType("text/" + "html" + "; charset=utf-8");
	    Result<Object> result = new Result<Object>();
	    if (null == file) {
	    	
	    }
	    return "";
	  }

	
	
	  @RequestMapping("/config")
	  public void config(HttpServletRequest request,HttpServletResponse response) {
		  
		  response.setContentType("application/json");
	        String rootPath = request.getSession()
	                .getServletContext().getRealPath("/");
	 
	       /* try {
	            String exec = new ActionEnter(request, rootPath).exec();
	            PrintWriter writer = response.getWriter();
	            writer.write(exec);
	            writer.flush();
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }*/
		/*  InputStream in = this.getClass().getResourceAsStream("/conf/ueditorConfig.json");
		  String stream2String = stream2String(in, "utf-8");
		  return stream2String;*/
	  }
	
	/**
	   * 将输入流的内容读成String,以\n为换行符
	   * 
	   * @param is
	   * @param charset
	   * @return
	   */
	  private static  String stream2String(InputStream is, String charset) {
	    StringBuilder sb = new StringBuilder();
	    try {
	      BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
	      String s;
	      while ((s = br.readLine()) != null) {
	        sb.append(s).append("\n");
	      }
	      return sb.toString();
	    } catch (Exception e) {
	    	logger.error(e.getMessage());
	    }
	    return null;
	  }

}
