package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本库对象(表)
 * @author 不能
 *
 */
public class Versions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4824707051112180914L;
	
	/**
	 * 版本编号
	 */
	private int version_serial_no;
	
	/**
	 * 版本号
	 */
	private String version_code;
	
	/**
	 * 最低版本对象
	 */
	private Versions min_version;
	
	/**
	 * Android下载地址
	 */
	private String apk_url;
	
	/**
	 * ISO下载地址
	 */
	private String ipa_url;
	
	/**
	 * 版本说明
	 */
	private String version_content;
	
	/**
	 * 版本上线时间
	 */
	private Date create_time;

	public int getVersion_serial_no() {
		return version_serial_no;
	}

	public void setVersion_serial_no(int version_serial_no) {
		this.version_serial_no = version_serial_no;
	}

	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}

	public Versions getMin_version() {
		return min_version;
	}

	public void setMin_version(Versions min_version) {
		this.min_version = min_version;
	}

	public String getApk_url() {
		return apk_url;
	}

	public void setApk_url(String apk_url) {
		this.apk_url = apk_url;
	}

	public String getIpa_url() {
		return ipa_url;
	}

	public void setIpa_url(String ipa_url) {
		this.ipa_url = ipa_url;
	}

	public String getVersion_content() {
		return version_content;
	}

	public void setVersion_content(String version_content) {
		this.version_content = version_content;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
}
