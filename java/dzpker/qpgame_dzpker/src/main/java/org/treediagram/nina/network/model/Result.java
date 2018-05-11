/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treediagram.nina.network.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 结果
 * 
 * @author kidal
 * 
 */
public class Result<T> implements Serializable {
	/**
	 * 序列化编号
	 */
	private static final long serialVersionUID = -7519682086668724500L;

	/**
	 * 错误编号
	 */
	private int code;

	/**
	 * 内容
	 */
	private T content;

	/**
	 * 附件
	 */
	private String attachment;

	/**
	 * 创建结果
	 */
	public Result() {

	}

	/**
	 * 创建结果
	 */
	public Result(int code) {
		this.code = code;
		this.content = null;
		this.attachment = null;
	}

	/**
	 * 创建结果
	 */
	public Result(int code, T content) {
		this.code = code;
		this.content = content;
		this.attachment = null;
	}

	/**
	 * 创建结果
	 */
	public Result(int code, T content, String attachment) {
		this.code = code;
		this.content = content;
		this.attachment = attachment;
	}

	/**
	 * 创建结果
	 */
	public static <T> Result<T> valueOfSuccess() {
		return new Result<T>(Code.OK);
	}

	/**
	 * 创建结果
	 */
	public static <T> Result<T> valueOfSuccess(T content) {
		return new Result<T>(Code.OK, content);
	}

	/**
	 * 创建结果
	 */
	public static <T> Result<T> valueOfSuccess(T content, String attachment) {
		return new Result<T>(Code.OK, content, attachment);
	}

	/**
	 * 创建结果
	 */
	public static <T> Result<T> valueOfError(int code) {
		return new Result<T>(code);
	}

	/**
	 * 创建结果
	 */
	public static <T> Result<T> valueOfError(int code, String attachment) {
		return new Result<T>(code, null, attachment);
	}

	public static <T> Result<T> valueOfError(int code, T content, String attachment) {
		return new Result<T>(code, content, attachment);
	}
	/**
	 * 转换为字典
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("content", content);
		return map;
	}

	/**
	 * 是否成功
	 */
	public boolean isSuccess() {
		return (code == Code.OK);
	}

	// getters and setters

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
}
