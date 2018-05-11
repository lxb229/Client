package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.TableChip;
import com.wangzhixuan.model.TableInsurance;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.TableInsuranceMapper;
import com.wangzhixuan.service.ITableInsuranceService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 桌子保险配置 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Service
public class TableInsuranceServiceImpl extends ServiceImpl<TableInsuranceMapper, TableInsurance> implements ITableInsuranceService {

	@Autowired
	private PropertyConfigurer configurer;
	
	private static Logger logger = LoggerFactory.getLogger(TableChipServiceImpl.class);
	private Gson gson = new Gson();
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<TableChip> page = new Page<TableChip>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<TableInsurance> list = this.getAllTableInsurance();
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
        
	}

	@Override
	public List<TableInsurance> getAllTableInsurance() {
		List<TableInsurance> tableInsuranceList = null;
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_dzpker_get_table_insuranceList_cfg";
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
            	tableInsuranceList = gson.fromJson(content, new TypeToken<List<TableInsurance>>(){}.getType());
        		for (int i = 0; i < tableInsuranceList.size(); i++) {
        			TableInsurance insurance = tableInsuranceList.get(i);
					insurance.setId(i+1);
					tableInsuranceList.set(i, insurance);
				}
            	return tableInsuranceList;
            } else {
            	return null; 
            }
        } else {
        	return null;
        }
		
	}

	@Override
	public TableInsurance getTableInsuranceById(int id) {
		TableInsurance tableInsurance = null;
		List<TableInsurance> list = getAllTableInsurance();
		for (int i = 0; i < list.size(); i++) {
			TableInsurance insurance = list.get(i);
			if(insurance.getId() == id) {
				tableInsurance = insurance;
				break;
			}
		}
		return tableInsurance;
	}

	@Override
	public Result setTableInsurance(TableInsurance tableInsurance, int type) throws UnsupportedEncodingException {
		List<TableInsurance> list = getAllTableInsurance();
		Result result = new Result();
		switch (type) {
			/**新增筹码设置*/ 
			case 1:
				if(list != null && list.size() < 40) {
					list.add(tableInsurance);
				} else {
					result.setMsg("不能超40");
					return result;
				}
				break;
				/**更改筹码配置*/
			case 2:
				for (int i = 0; i < list.size(); i++) {
					TableInsurance insurance = list.get(i);
					if(insurance.getId() == tableInsurance.getId()) {
						list.set(i, tableInsurance);
						break;
					}
				}
				break;
				/**删除筹码配置*/
			case 3:
				for (int i = 0; i < list.size(); i++) {
					TableInsurance insurance = list.get(i);
					if(insurance.getId() == tableInsurance.getId()) {
						list.remove(i);
						break;
					}
				}
				break;
		default:
			break;
		}
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_dzpker_set_table_insuranceList_cfg";
		for (int i = 0; i < list.size(); i++) {
			TableInsurance insurance = list.get(i);
			insurance.setId(null);
			list.set(i, insurance);
		}
		String jsonTableInsurance = URLEncoder.encode(JSON.toJSONString(list), "UTF-8");
		String chipResult = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			chipResult = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+jsonTableInsurance);
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
