package com.wangzhixuan.test.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.plugins.Page;
import com.wangzhixuan.mapper.PlayerMapper;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class PlayerServiceTest extends BaseTest {
	
	@Autowired
	private PlayerMapper playerMapper;
	/*
	 * 分页查询用户
	 */
	@Test
	public void selectPlayerPage(){
		Page<Player> page = new Page<Player>(0, 20);
        page.setOrderByField("id");
        page.setAsc(true);
        List<Player> list = playerMapper.selectPlayerPage(page, null);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getPlayId()+"    "+list.get(i).getPlayNick());
		}
		
	}
	
}
