package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.TableChip;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.TableChipMapper;
import com.wangzhixuan.service.ITableChipService;
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
 * 桌子配置 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Service
public class TableChipServiceImpl extends ServiceImpl<TableChipMapper, TableChip> implements ITableChipService {

	@Autowired
	private PropertyConfigurer configurer;
	private static Logger logger = LoggerFactory.getLogger(TableChipServiceImpl.class);
	private Gson gson = new Gson();
	
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<TableChip> page = new Page<TableChip>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<TableChip> list = this.getAllTableChip();
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public List<TableChip> getAllTableChip() {
		List<TableChip> tableChipList = null;
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_dzpker_get_table_chip_cfg";
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
            	tableChipList = gson.fromJson(content, new TypeToken<List<TableChip>>(){}.getType());
        		for (int i = 0; i < tableChipList.size(); i++) {
        			TableChip chip = tableChipList.get(i);
					chip.setId(i+1);
					tableChipList.set(i, chip);
				}
            	return tableChipList;
            } else {
            	return null; 
            }
        } else {
        	return null;
        }
	}

	@Override
	public Result setTableChip(TableChip tableChip, int type) throws UnsupportedEncodingException {
		List<TableChip> list = getAllTableChip();
		Result result = new Result();
		switch (type) {
			/**新增筹码设置*/ 
			case 1:
				if(list != null && list.size() < 10) {
					if(tableChip.getBig()/tableChip.getSmall()<2) {
						result.setMsg("大小盲错误");
						return result;
					}
					list.add(tableChip);
				} else {
					result.setMsg("不能超10");
					return result;
				}
				break;
				/**更改筹码配置*/
			case 2:
				for (int i = 0; i < list.size(); i++) {
					TableChip chip = list.get(i);
					if(chip.getId() == tableChip.getId()) {
						if(tableChip.getBig()/tableChip.getSmall()<2) {
							result.setMsg("大小盲错误");
							return result;
						}
						list.set(i, tableChip);
						break;
					}
				}
				break;
				/**删除筹码配置*/
			case 3:
				for (int i = 0; i < list.size(); i++) {
					TableChip chip = list.get(i);
					if(chip.getId() == tableChip.getId()) {
						list.remove(i);
						break;
					}
				}
				break;
		default:
			break;
		}
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_dzpker_set_table_chip_cfg";
		for (int i = 0; i < list.size(); i++) {
			TableChip chip = list.get(i);
			chip.setId(null);
			list.set(i, chip);
		}
		String jsonTableChip = URLEncoder.encode(JSON.toJSONString(list), "UTF-8");
		String chipResult = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			chipResult = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+jsonTableChip);
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

	@Override
	public TableChip getTableById(int id) {
		TableChip tableChip = null;
		List<TableChip> list = getAllTableChip();
		for (int i = 0; i < list.size(); i++) {
			TableChip chip = list.get(i);
			if(chip.getId() == id) {
				tableChip = chip;
				break;
			}
		}
		return tableChip;
	}
	
}
