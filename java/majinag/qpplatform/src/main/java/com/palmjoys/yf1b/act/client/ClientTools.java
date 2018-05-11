package com.palmjoys.yf1b.act.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.core.reflect.PackageScanner;
import org.treediagram.nina.core.reflect.ReflectionUtils;
import org.treediagram.nina.network.annotation.InBody;
import org.treediagram.nina.network.annotation.NetworkApi;
import org.treediagram.nina.network.annotation.NetworkFacade;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.StorageManager;
import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

/**
 * 客户端工具类
 * 
 * @author tengda
 * 
 */
@Component
@ConsoleBean
public class ClientTools implements ApplicationContextAware {
	/**
	 * 应用程序上下文
	 */
	private ApplicationContext applicationContext;

	@Autowired
	private StorageManager storageManager;
	/**
	 * {@link ApplicationContextAware#setApplicationContext(ApplicationContext)}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 定义模块号信息
	 * 
	 * @author sjz
	 * 
	 */
	private static class Facade2lua_define_module {
		String name;
		String desc;
		Object value;
	}

	/**
	 * 定义代号信息
	 * 
	 * @author tengda
	 * 
	 */
	private static class Facade2lua_define_code {
		String name;
		String desc;
		Object value;
	}

	/**
	 * 定义信息
	 * 
	 * @author tengda
	 * 
	 */
	private static class Facade2lua_define {
		String desc;
		List<Facade2lua_define_code> codes = new ArrayList<Facade2lua_define_code>();
	}

	/**
	 * 前端转lua信息
	 * 
	 * @author tengda
	 * 
	 */
	private static class Facade2LuaInfo {
		int id = 0;
		String mname = "";
		String fname = "";
		List<String> inBodys = new ArrayList<String>();
	}

	private static class FacadeMethodInfo{
		//接口Id
		int id = 0;
		//接口名称
		String name = "";
		//接口描述
		String desc;
		//参数类型列表信息,与参数从后向前对齐
		List<String> paramTypes = new ArrayList<String>();
		//参数列表信息
		List<FacadeMethodParamInfo> facadeParams = new ArrayList<FacadeMethodParamInfo>();
	}
	
	private static class FacadeMethodParamInfo{
		//参数名称
		String name="";
		//参数描述
		String desc="";
	}
	
	/**
	 * 重新加载所有的execl资源文件
	 * */
	@ConsoleCommand(name = "reloadAllExecl", description = "重新加载所有的execl资源文件")
	public void reloadAllExecl() throws IOException{
		Storage<?, ?>[] storages = storageManager.listStorages();
		for(Storage<?, ?> storage : storages){
			storage.reload();
		}
	}
	
	/**
	 * 重新指定execl资源文件
	 * */
	@ConsoleCommand(name = "reloadExecl", description = "重新指定execl资源文件")
	public void reloadExecl(String fileName) throws IOException{
		Storage<?, ?>[] storages = storageManager.listStorages();
		for(Storage<?, ?> storage : storages){
			String fileFullName = storage.getLocation();
			if(fileFullName.contains(fileName)){
				storage.reload();
			}
		}
	}
	
	/**
	 * 使用前端方法生成lua文件
	 */
	@ConsoleCommand(name = "facade2lua", description = "使用前端方法生成lua文件")
	public void facade2lua() throws IOException {
		// 收集定义信息
		final List<Facade2lua_define_module> modules = new ArrayList<ClientTools.Facade2lua_define_module>();
		final Map<String, Facade2lua_define> defines = new HashMap<String, ClientTools.Facade2lua_define>();
		final PackageScanner packageScanner = new PackageScanner("com.palmjoys.yf1b.act");
		final Collection<Class<?>> classes = packageScanner.getClazzCollection();
		for (Class<?> cls : classes) {
			if (!cls.isInterface()) {
				continue;
			}
			SocketDefine socketDefine = cls.getAnnotation(SocketDefine.class);
			if (socketDefine == null) {
				continue;
			}
			final Facade2lua_define define = new Facade2lua_define();
			define.desc = socketDefine.value();
			defines.put(cls.getSimpleName().toUpperCase(), define);
			ReflectionUtils.doWithFields(cls, new FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					SocketCode socketCode = field.getAnnotation(SocketCode.class);
					if (socketCode != null) {
						Facade2lua_define_code define_code = new Facade2lua_define_code();
						define_code.name = field.getName().toUpperCase();
						define_code.desc = socketCode.value();
						define_code.value = field.get(null);
						define.codes.add(define_code);
					}

					SocketModule socketModule = field.getAnnotation(SocketModule.class);
					if (socketModule != null) {
						Facade2lua_define_module define_module = new Facade2lua_define_module();
						define_module.name = field.getName().toUpperCase();
						define_module.desc = socketModule.value();
						define_module.value = field.get(null);
						modules.add(define_module);
					}
				}
			});
		}

		// 收集前端信息
		final Map<String, List<Facade2LuaInfo>> infos = new HashMap<String, List<Facade2LuaInfo>>();
		final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(NetworkFacade.class);
		for (Object bean : beans.values()) {
			Class<? extends Object> cls = bean.getClass();
			Class<?>[] clsInterfaces = cls.getInterfaces();
			for (final Class<?> clsInterface : clsInterfaces) {
				if (clsInterface.getAnnotation(NetworkFacade.class) != null) {
					final List<Facade2LuaInfo> infoList = new ArrayList<Facade2LuaInfo>();

					ReflectionUtils.doWithMethods(clsInterface, new MethodCallback() {
						@Override
						public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
							NetworkApi command = method.getAnnotation(NetworkApi.class);
							if (command == null) {
								return;
							}

							Facade2LuaInfo info = new Facade2LuaInfo();
							info.id = command.value();
							info.mname = method.getName();

							Annotation[][] parameterAnnotations = method.getParameterAnnotations();
							for (Annotation[] parameterAnnotation : parameterAnnotations) {
								for (Annotation annotation : parameterAnnotation) {
									if (annotation instanceof InBody) {
										String value = ((InBody) annotation).value();

										info.inBodys.add(value);
									}
								}
							}

							infoList.add(info);
						}
					});

					infos.put(clsInterface.getSimpleName(), infoList);
				}
			}
		}

		StringBuilder builder = new StringBuilder();
		builder.append("module('cg.facade',package.seeall)\n");
		builder.append("\n");

		// 生成常量
		builder.append("----------------------------------------------------\n");
		builder.append("-- contants\n");
		builder.append("----------------------------------------------------\n");
		builder.append("\n");

		// 生成模块号
		// 按照模块编号排序
		Collections.sort(modules, new Comparator<Facade2lua_define_module>() {

			@Override
			public int compare(Facade2lua_define_module o1, Facade2lua_define_module o2) {
				Integer value1 = (Integer) o1.value;
				Integer value2 = (Integer) o2.value;
				return value1.compareTo(value2);
			}
		});

		builder.append("--模块编号\n");
		builder.append("MODULES = {\n");
		for (Facade2lua_define_module module : modules) {
			builder.append("	").append(module.name).append(" = ").append(module.value).append(", --")
					.append(module.desc).append("\n");
		}
		builder.append("}\n\n");

		// 错误码
		for (Entry<String, Facade2lua_define> e : defines.entrySet()) {
			String name = e.getKey();
			Facade2lua_define define = e.getValue();

			builder.append("--").append(define.desc).append("\n");
			builder.append(name).append(" = {\n");
			builder.append("    CODE = {\n");
			for (Facade2lua_define_code define_code : define.codes) {
				builder.append("        ").append(define_code.name).append(" = ").append(define_code.value)
						.append(", --").append(define_code.desc).append("\n");
			}
			builder.append("    },\n");
			builder.append("}\n");
			builder.append("\n");
		}

		// 生成类
		builder.append("----------------------------------------------------\n");
		builder.append("-- class\n");
		builder.append("----------------------------------------------------\n");
		builder.append("class = cs.class.class(_NAME)\n");
		builder.append("\n");
		builder.append("----------------------------------------------------\n");
		builder.append("-- init\n");
		builder.append("----------------------------------------------------\n");
		builder.append("function class:init(callbacks,timeout)\n");
		builder.append("    self.flag = {}\n");
		builder.append("    self.attachment = ''\n");
		builder.append("    self.callbacks = callbacks or {}\n");
		builder.append("    self.timeout = timeout or 15\n");
		builder.append("end\n");
		builder.append("\n");
		builder.append("----------------------------------------------------\n");
		builder.append("-- set flag\n");
		builder.append("----------------------------------------------------\n");
		builder.append("function class:setFlag(flag)\n");
		builder.append("    self.flag = flag\n");
		builder.append("end\n");
		builder.append("\n");
		builder.append("----------------------------------------------------\n");
		builder.append("-- set attachment\n");
		builder.append("----------------------------------------------------\n");
		builder.append("function class:setAttachment(attachment)\n");
		builder.append("    self.attachment = attachment\n");
		builder.append("end\n");
		builder.append("\n");
		builder.append("----------------------------------------------------\n");
		builder.append("-- set serverdomain\n");
		builder.append("----------------------------------------------------\n");
		builder.append("function class:setServerDomain(domain)\n");
		builder.append("    self.client = cs.client.get(domain)\n");
		builder.append("end\n");
		builder.append("\n");
		
		for (Entry<String, List<Facade2LuaInfo>> e : infos.entrySet()) {
			String name = e.getKey();
			List<Facade2LuaInfo> infoList = e.getValue();

			for (Facade2LuaInfo info : infoList) {
				// 注释
				// builder.append("------------------------------------------------------------\n");
				// builder.append("-- @brief ").append(info.mdesc).append("\n");
				// for (Tuple2<String, String> tuple : info.inBodys) {
				// builder.append("-- @param ").append(tuple.getItem1()).append(" ").append(tuple.getItem2())
				// .append("\n");
				// }
				builder.append("------------------------------------------------------------\n");

				// 方法
				builder.append("function class:").append(name).append("_").append(info.mname);
				builder.append("(");
				for (String value : info.inBodys) {
					builder.append(value).append(",");
				}
				builder.append("successCallback,").append("timeoutCallback,").append("hideLoading");
				builder.append(")\n");

				builder.append("    local data = {\n");
				for (String value : info.inBodys) {
					builder.append("        ").append(value).append("=").append(value).append(",\n");
				}
				builder.append("    }\n");
				builder.append("    local callbacks = nil\n");
				builder.append("    if successCallback or timeoutCallback then\n");
				builder.append("		callbacks = {\n");
				builder.append("			success = function(...)\n");
				builder.append("                if not (hideLoading == true) then\n");
				builder.append("				    cs.util.call(self.callbacks.afterSend)\n");
				builder.append("                end\n");
				builder.append("				cs.util.call(successCallback,...)\n");
				builder.append("			end,\n");
				builder.append("			timeout = function()\n");
				builder.append("                if not (hideLoading == true) then\n");
				builder.append("				    cs.util.call(self.callbacks.afterSend)\n");
				builder.append("                end\n");
				builder.append("				cs.util.call(timeoutCallback)\n");
				builder.append("			end,\n");
				builder.append("		}\n");
				builder.append("        if not (hideLoading == true) then\n");
				builder.append("    	    cs.util.call(self.callbacks.beforeSend)\n");
				builder.append("        end\n");
				builder.append("    end\n");
				builder.append("    self.client:sendjson(").append(info.id)
						.append(",self.flag,data,self.attachment,self.timeout,callbacks)\n");
				builder.append("end\n");
				builder.append("\n");
			}
		}

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			File file = new File("facade.lua");
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos);
			osw.write(builder.toString());
		} finally {
			osw.close();
			fos.close();
		}
	}	

	/**
	 * 使用前端方法生成js文件
	 */
	@ConsoleCommand(name = "facade2js", description = "使用前端方法生成js文件")
	public void facade2js() throws IOException {
		List<FacadeMethodInfo> infoList = new ArrayList<FacadeMethodInfo>();
		// 收集接口方法
		final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(NetworkFacade.class);
		for (Object bean : beans.values()) {
			Class<? extends Object> cls = bean.getClass();
			Class<?>[] clsInterfaces = cls.getInterfaces();
			for (final Class<?> clsInterface : clsInterfaces) {
				if (clsInterface.getAnnotation(NetworkFacade.class) != null) {
					ReflectionUtils.doWithMethods(clsInterface, new MethodCallback() {
						@Override
						public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
							NetworkApi command = method.getAnnotation(NetworkApi.class);
							if (command == null) {
								return;
							}
							FacadeMethodInfo methodInfo = new FacadeMethodInfo();
							methodInfo.id = command.value();
							methodInfo.desc = command.desc();
							methodInfo.name = StringUtils.upperCase(method.getName());
							
							Class []paramTypes = method.getParameterTypes();
							for(int i=0; i<paramTypes.length; i++){
								String sType = paramTypes[i].getName();
								String[] sTypes = sType.split("\\.");
								methodInfo.paramTypes.add(sTypes[sTypes.length-1]);
							}							
							Annotation[][] parameterAnnotations = method.getParameterAnnotations();
							for (Annotation[] parameterAnnotation : parameterAnnotations) {
								for (Annotation annotation : parameterAnnotation) {
									if (annotation instanceof InBody) {
										FacadeMethodParamInfo paramInfo = new FacadeMethodParamInfo();
										paramInfo.name = ((InBody) annotation).value();
										paramInfo.desc = ((InBody) annotation).desc();
										methodInfo.facadeParams.add(paramInfo);
									}
								}
							}
							infoList.add(methodInfo);
						}
					});
				}
			}
		}

		StringBuilder builder = new StringBuilder();
		//按接口编号从小到大排序
		infoList.sort(new Comparator<FacadeMethodInfo>(){
			@Override
			public int compare(FacadeMethodInfo o1, FacadeMethodInfo o2) {
				if(o1.id > o2.id){
					return 1;
				}else{
					if(o1.id < o2.id){
						return -1;
					}else{
						return 0;
					}
				}
			}
		});
		
		builder.append("/**\n");
		builder.append(" * 通信协议枚举\n");
		builder.append(" *\n");
		builder.append(" * @export\n");
		builder.append(" * @enum {number}\n");
		builder.append(" */\n");
		
		builder.append("export enum Protocol {\n");
		int index1 = 0;
		for (FacadeMethodInfo methodInfo : infoList) {
			builder.append("\n");
			builder.append("    /**\n");
			builder.append("     * ").append(methodInfo.desc).append("\n");
			int start = methodInfo.paramTypes.size() - methodInfo.facadeParams.size();
			int index2 = 0;
			for(FacadeMethodParamInfo paramInfo : methodInfo.facadeParams){
				String sType = methodInfo.paramTypes.get(start);
				builder.append("     * ").append(paramInfo.name).append(" [");
				builder.append(sType).append("] ").append(paramInfo.desc);				
				start++;
				if(index2 <= methodInfo.facadeParams.size()-1){
					builder.append("\n");
				}
				index2++;
			}			
			builder.append("     */\n").append("    ");
			builder.append(methodInfo.name).append(" = ").append(methodInfo.id);
			if(index1 < infoList.size()-1){
				builder.append(",\n");
			}
			index1++;
		}
		builder.append("\n}");
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			File file = new File("Protocol.ts");
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos);
			osw.write(builder.toString());
		} finally {
			osw.close();
			fos.close();
		}
	}
	
	@ConsoleCommand(name = "toExcel", description = "将文件内容写入excel")
	public void toExcel(String dirPath) {
		try {

			File newDir = new File(dirPath + "/result");
			if (!newDir.exists()) {
				if (!newDir.mkdir()) {
					System.err.println("创建[result]文件夹失败");
					return;
				}
			}

			HSSFWorkbook wb = new HSSFWorkbook();

			// 添加Worksheet（不添加sheet时生成的xls文件打开时会报错）
			@SuppressWarnings("unused")
			Sheet sheet = wb.createSheet("sheet");

			File root = new File(dirPath);
			File[] files = root.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					continue;
				}

				String fileName = file.getName();
				if (!fileName.endsWith(".txt")) {
					continue;
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file.getAbsolutePath())));

				int row = 0;
				for (String line = br.readLine(); line != null; line = br.readLine()) {
					int col = 0;

					Row sheetRow = wb.getSheet("sheet").createRow(row++);

					String find = "/";

					int start = 0;
					int end = 0;
					while (true) {
						end = line.indexOf(find, end);
						if (end != -1) {
							String value = line.substring(start, end);

							Cell cell = sheetRow.createCell(col++);
							cell.setCellValue(value);

							start = ++end;
						} else {
							if (start != line.length()) {
								String value = line.substring(start, line.length());
								Cell cell = sheetRow.createCell(col++);
								cell.setCellValue(value);
							}

							break;
						}
					}
				}

				br.close();

				// 保存为Excel文件
				FileOutputStream out = null;

				try {
					String name = fileName.substring(0, fileName.length() - 4);
					String newName = newDir.getCanonicalPath() + "\\" + name + ".xls";

					out = new FileOutputStream(newName);
					wb.write(out);
				} catch (IOException e) {
					System.out.println(e.toString());
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						System.out.println(e.toString());
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
