package chesshttpserver.controller;
  
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.chessgame.dao.bean.Users;
import org.springframework.context.annotation.Scope;  
import org.springframework.stereotype.Controller;  
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.ResponseBody;  

import chesshttpserver.bo.UserBo;

import com.alibaba.fastjson.JSON;  
import com.guse.chessgame.service.LoginService;
import com.guse.chessgame.service.impl.LoginServiceImpl;

/** 
* @ClassName: IndexController 
* @Description: Demo
* @author Fily GUSE
* @date 2017年8月16日 下午3:42:54 
*  
*/
@Controller  
@Scope("prototype")  
@RequestMapping("indexController")  
public class IndexController {  
	
	LoginService loginService = new LoginServiceImpl();
  
        //访问地址: http://127.0.0.1:8080/mongodb-demo/indexController/index.do  
      
//        @Autowired  
//        private UserService userService;  
	
	 @RequestMapping("login")  
     public @ResponseBody String login(ModelMap modelMap,HttpServletRequest request) { 
		 
		 String data = request.getParameter("data");
		 
		 String create_ip = request.getRemoteAddr();
		 JSONObject json = JSONObject.fromObject(data);
		 String uid = json.getString("uid");
		 String nickname = json.getString("nickname");// 昵称，昵称为空时，是游客登录
		 String head_portrait = json.getString("headimgurl");
		 int sex = json.getInt("sex");// 1男2女 0不显示
		 
		 Users user = loginService.login(uid, nickname, head_portrait, sex, create_ip);
		 
         return JSON.toJSONString(user);  
     }  
      
        @RequestMapping("index")  
        public @ResponseBody String index(ModelMap modelMap,HttpServletRequest request) { 
        	String create_ip = request.getRemoteAddr();
            UserBo userBo = new UserBo();  
            userBo.setPassword("junlenet");  
            userBo.setPhone("130279814XX");  
            userBo.setSex("男");  
            userBo.setUserName("www.junlenet.com");  
            userBo.setUserNo("1000524100250");  
//            userBo = userService.save(userBo);  
//            Set<String> collections = userService.getCollectionNames();  
//            for (String str : collections) {  
//                System.out.println(str);  
//            }  
//            Pager pager = userService.selectPage(userBo, new Pager());  
//            List<UserBo> users = pager.getResult();  
//            for (UserBo user : users) {  
//                System.out.println(JSONObject.toJSONString(user));  
//            }  
            return JSON.toJSONString(userBo);  
        }  
}