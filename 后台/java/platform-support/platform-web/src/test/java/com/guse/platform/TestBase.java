package com.guse.platform;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 
 * @author nbin
 * @date 2017年7月18日 上午9:35:14 
 * @version V1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {
                "classpath*:/config/spring.xml",
                "classpath*:/config/mybatis-config.xml",
        })
public class TestBase {
	 	@Test
	    public void contextLoads() {
	 		
	    }
}
