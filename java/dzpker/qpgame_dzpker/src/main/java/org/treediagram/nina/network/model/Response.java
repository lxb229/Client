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

/**
 * 答复
 * 
 * @author kidal
 * 
 */
public class Response<T> {
	/**
	 * 编码方式
	 */
	private int code;

	/**
	 * 时间戳
	 */
	private int timestamp;

	/**
	 * 请求标识
	 */
	private int id;

	/**
	 * 答复体
	 */
	private T body;

	/**
	 * 附件
	 */
	private String attachment;

	/**
	 * 创建答复对象
	 */
	public static <T> Response<T> valueOf(T body) {
		Response<T> response = new Response<T>();
		response.code = 0;
		response.timestamp = 0;
		response.id = 0;
		response.body = body;
		return response;
	}

	// getters and setters

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Deprecated
	public String getAttchment() {
		return attachment;
	}

	@Deprecated
	public void setAttchment(String attchment) {
		this.attachment = attchment;
	}
}
