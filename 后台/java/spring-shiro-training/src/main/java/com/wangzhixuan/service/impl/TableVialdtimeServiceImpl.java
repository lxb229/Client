package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.TableVialdtime;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.TableVialdtimeMapper;
import com.wangzhixuan.service.ITableVialdtimeService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房间解散时长设置 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Service
public class TableVialdtimeServiceImpl extends ServiceImpl<TableVialdtimeMapper, TableVialdtime> implements ITableVialdtimeService {

	@Autowired
	private PropertyConfigurer configurer;
	private static Logger logger = LoggerFactory.getLogger(TableChipServiceImpl.class);
	private Gson gson = new Gson();
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<TableVialdtime> page = new Page<TableVialdtime>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<TableVialdtime> list = this.getAllTableVialdtime();
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
        
	}
	
	@Override
	public List<TableVialdtime> getAllTableVialdtime() {
		List<TableVialdtime> tableVialdtimeList = new ArrayList<>();
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_dzpker_get_table_vialdtime_cfg";
		lisboaAddress = lisboaAddress+cmd;
		
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success == 0) {
            	String content = obj.getString("content");
            	List<Integer> timeList = gson.fromJson(content, new TypeToken<List<Integer>>(){}.getType());
        		for (int i = 0; i < timeList.size(); i++) {
        			TableVialdtime vialdtime = new TableVialdtime();
        			vialdtime.setId(i+1);
        			vialdtime.setVildTimes(timeList.get(i));
					tableVialdtimeList.add(vialdtime);
				}
            	return tableVialdtimeList;
            } else {
            	return null; 
            }
        } else {
        	return null;
        }
	}

	@Override
	public TableVialdtime getTableVialdtimeById(int id) {
		TableVialdtime tableVialdtime = null;
		List<TableVialdtime> list = getAllTableVialdtime();
		for (int i = 0; i < list.size(); i++) {
			TableVialdtime vialdtime = list.get(i);
			if(vialdtime.getId() == id) {
				tableVialdtime = vialdtime;
				break;
			}
		}
		return tableVialdtime;
	}

	@Override
	public Result setTableVialdtime(TableVialdtime tableVialdtime, int type) throws UnsupportedEncodingException {
		List<TableVialdtime> list = getAllTableVialdtime();
		Result result = new Result();
		switch (type) {
			/**新增筹码设置*/ 
			case 1:
				if(list != null && list.size() < 10) {
					list.add(tableVialdtime);
				} else {
					result.setMsg("不能超10");
					return result;
				}
				break;
				/**更改筹码配置*/
			case 2:
				for (int i = 0; i < list.size(); i++) {
					TableVialdtime vialdtime = list.get(i);
					if(vialdtime.getId() == tableVialdtime.getId()) {
						list.set(i, tableVialdtime);
						break;
					}
				}
				break;
				/**删除筹码配置*/
			case 3:
				for (int i = 0; i < list.size(); i++) {
					TableVialdtime vialdtime = list.get(i);
					if(vialdtime.getId() == tableVialdtime.getId()) {
						list.remove(i);
						break;
					}
				}
				break;
		default:
			break;
		}
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_dzpker_set_table_vialdtime_cfg";
		List<Integer> jsonList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			TableVialdtime vialdtime = list.get(i);
			jsonList.add(vialdtime.getVildTimes());
		}
		String jsonTableVialdtime = URLEncoder.encode(JSON.toJSONString(jsonList), "UTF-8");
		String chipResult = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			chipResult = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+jsonTableVialdtime);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (chipResult != null && chipResult != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(chipResult);
           int success = obj.getIntValue("code");
           if(success == 0) {
				result.setSuccess(true);
				result.setMsg("设置成功");
				return result;
           } else {
        	   	result.setMsg(obj.getString("content"));
           		return result; 
           }
           
       } else {
    	   result.setMsg("返回结果超时!");
      		return result; 
       }
	}
	
}
