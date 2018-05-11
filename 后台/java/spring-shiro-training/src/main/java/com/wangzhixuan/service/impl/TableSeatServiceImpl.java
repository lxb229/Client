package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.TableSeat;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.TableSeatMapper;
import com.wangzhixuan.service.ITableSeatService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 桌子手牌 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-23
 */
@Service
public class TableSeatServiceImpl extends ServiceImpl<TableSeatMapper, TableSeat> implements ITableSeatService {

	@Autowired
	private PropertyConfigurer configurer;
	
	private static Logger logger = LoggerFactory.getLogger(TableChipServiceImpl.class);
	private Gson gson = new Gson();
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<TableSeat> page = new Page<TableSeat>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<TableSeat> list = new ArrayList<>();
        if(pageInfo.getCondition() != null && pageInfo.getCondition().get("roomId") != null) {
        	int roomId = Integer.parseInt(pageInfo.getCondition().get("roomId").toString());
        	list = this.getAllTableSeatBy(roomId);
        }
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}

	@Override
	public List<TableSeat> getAllTableSeatBy(int roomId) {
		
		List<TableSeat> tableSeatList = null;
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd="gm_dzpker_get_table_seat_handcards";
		lisboaAddress = lisboaAddress+cmd+"%20"+roomId;
		
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
            	JSONObject contentObj = JSONObject.parseObject(content);
            	String items = contentObj.getString("items");
            	tableSeatList = gson.fromJson(items, new TypeToken<List<TableSeat>>(){}.getType());
        		for (int i = 0; i < tableSeatList.size(); i++) {
        			TableSeat seat = tableSeatList.get(i);
					seat.setId(i+1);
					tableSeatList.set(i, seat);
				}
            	return tableSeatList;
            } else {
            	return null; 
            }
        } else {
        	return null;
        }
	}
	
}
