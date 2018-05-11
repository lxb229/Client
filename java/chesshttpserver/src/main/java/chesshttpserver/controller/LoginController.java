package chesshttpserver.controller;

import java.net.URLEncoder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;

@Controller
@Scope("prototype")
@RequestMapping("login")
public class LoginController {

	@RequestMapping
	public @ResponseBody String index(String data) throws Exception {

		data = new String(Base64.decode(data.getBytes()), "UTF-8");
		System.out.println(data);

		JSONObject obj = new JSONObject();
		obj.put("wsUrl", "ws://192.168.199.115:8989/websocket");
		obj.put("pid", "1089231");
		obj.put("key", "87473920");
		
		String jString = JSON.toJSONString(obj);
//		jString = URLEncoder.encode(jString, "UTF-8");
		byte[] byteString = jString.getBytes("UTF-8");
		// base64一行不能超过76字符，超过则添加回车换行符.用字符串对象的replaceAll方法替换掉\r和\n
		return Base64.encode(byteString).replaceAll("[\\s*\t\n\r]", "");
	}
}
