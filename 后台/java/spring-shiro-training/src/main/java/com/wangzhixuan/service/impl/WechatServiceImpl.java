package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Wechat;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.mapper.WechatMapper;
import com.wangzhixuan.service.IWechatService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2017-12-18
 */
@Service
public class WechatServiceImpl extends ServiceImpl<WechatMapper, Wechat> implements IWechatService {
	@Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<Wechat> page = new Page<Wechat>(pageInfo.getNowpage(), pageInfo.getSize());
        
        EntityWrapper<Wechat> wrapper = new EntityWrapper<Wechat>();
        wrapper.orderBy(pageInfo.getSort(), pageInfo.getOrder().equalsIgnoreCase("ASC"));
        selectPage(page, wrapper);
        
        pageInfo.setRows(page.getRecords());
        pageInfo.setTotal(page.getTotal());
    }
}
