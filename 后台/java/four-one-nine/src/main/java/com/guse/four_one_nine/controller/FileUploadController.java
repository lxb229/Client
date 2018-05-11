package com.guse.four_one_nine.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * @ClassName: FileUpload
 * @Description: 文件上传 适配器
 * @author Fily GUSE
 * @date 2018年1月14日 下午7:14:11
 * 
 */
@Controller
@RequestMapping("file_upload")
public class FileUploadController {

	/*
	 * 采用spring提供的上传文件的方法
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/springUpload")
	@ResponseBody
	public JSONObject springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
		JSONObject json = new JSONObject();
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					// 文件名
					String fileName = file.getOriginalFilename();
					// 文件后缀
					String perfix = fileName.substring(fileName.lastIndexOf("."));
					json.put("file_name", fileName);
					json.put("path", uploadQiNiu(new Date().getTime()+perfix,file.getInputStream()));
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("运行时间：" + String.valueOf(endTime - startTime) + "ms");
		
		return json;
	}
	
	/** 
	* @Description: 上传文件到七牛云 
	* @param @param key 将要保存的文件名
	* @param @param input 数据流信息
	* @param @return
	* @return String 保存的文件名
	* @throws 
	*/
	private String uploadQiNiu(String key, InputStream input) {
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = "i-1WWsJAIvYQ7lpYxtdZUWqHwsKf9RNBwCDnm6Nl";
		String secretKey = "Qu9hN6i432YW-KeD-OOJ7Z0mUaE7-p14rgbD7xaw";
		String bucket = "four-one-nine";
		Auth auth = Auth.create(accessKey, secretKey);
		// 设置上传空间
		String upToken = auth.uploadToken(bucket, key);
		try {
		    Response response = uploadManager.put(input,key,upToken, null, null);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    System.out.println(putRet.key);
		    System.out.println(putRet.hash);
		    
		    return "http://p2l3wh5ba.bkt.clouddn.com/"+putRet.key;
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    System.out.println(r.error);
		}
		return null;
	}

}
