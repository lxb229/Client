package com.palmjoys.yf1b.act.framework.common.manager;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.treediagram.nina.core.reflect.PackageScanner;
import org.treediagram.nina.core.reflect.ReflectionUtils;
import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;

@Component
public class ErrorCodeManager {
	final Map<Object, String> errCodeMap = new HashMap<Object, String>();
	final PackageScanner packageScanner = new PackageScanner("com.palmjoys.yf1b.act");
	@PostConstruct
	protected void init() {
		final Collection<Class<?>> classes = packageScanner.getClazzCollection();
		for (Class<?> cls : classes) {
			if (!cls.isInterface()) {
				continue;
			}
			SocketDefine socketDefine = cls.getAnnotation(SocketDefine.class);
			if (socketDefine == null) {
				continue;
			}
			ReflectionUtils.doWithFields(cls, new FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					SocketCode socketCode = field.getAnnotation(SocketCode.class);
					if (socketCode != null) {
						String errDesc = socketCode.value();
						Object errCode = field.get(null);
						errCodeMap.put(errCode, errDesc);
					}
				}
			});
		}
	}
	
	public String Error2Desc(int errCode){
		String errDesc = errCodeMap.get(errCode);
		if(null == errDesc){
			return "未知错误";
		}
		return errDesc;
	}
}
