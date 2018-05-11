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
 * 请求对象
 * 
 * @author kidal
 * 
 */
public class Request<T> {
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
	 * 请求体
	 */
	private T body;

	/**
	 * 附件
	 */
	private String attachment;

	/**
	 * 是否压缩
	 */
	private boolean compress;

	/**
	 * 创建请求对象
	 */
	public static <T> Request<T> valueOf(int id, T body) {
		Request<T> request = new Request<T>();
		request.code = 0;
		request.timestamp = 0;
		request.id = id;
		request.body = body;
		request.attachment = null;
		request.compress = false;
		return request;
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

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}
}
