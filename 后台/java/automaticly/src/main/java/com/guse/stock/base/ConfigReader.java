package com.guse.stock.base;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;


public class ConfigReader {
	private Properties pertie = new Properties();  
	public ConfigReader() {
		readConfigFile();
	}
	
	public void readConfigFile() {
		try {
			String filePath = ConfigReader.class.getClassLoader().getResource("properties/config.properties").getPath() ;
			BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(filePath));  
			pertie.load(inStream);
	     } catch (FileNotFoundException e) {  
	    	e.printStackTrace();   
	     } catch (IOException e) {  
	        e.printStackTrace();
	     }  
	}

	public String read(String key){
		Object value = pertie.getProperty(key);
		if(value != null) {
			return value.toString();
		}
		return "" ;
	}
	
	public Map<String,String> readAll(){
		Map<String,String> map = new LinkedHashMap<String, String>();
		Enumeration<?> enume = pertie.propertyNames();
		while(enume.hasMoreElements()) {
			String key = enume.nextElement().toString();
			map.put(key, pertie.getProperty(key));
		}
		return map ;
	}
}