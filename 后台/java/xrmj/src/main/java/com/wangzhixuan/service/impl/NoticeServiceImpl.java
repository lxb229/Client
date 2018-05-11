package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Notice;
import com.wangzhixuan.mapper.NoticeMapper;
import com.wangzhixuan.service.INoticeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏公告 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {
	
}
