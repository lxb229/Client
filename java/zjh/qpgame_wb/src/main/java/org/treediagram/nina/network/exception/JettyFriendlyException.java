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

package org.treediagram.nina.network.exception;

/**
 * Jetty友善的异常
 * 
 * @author kidal
 *
 */
public class JettyFriendlyException extends SocketException {
	private static final long serialVersionUID = -5822381090884249034L;
	private int statusCode;
	private byte[] responseData;

	public JettyFriendlyException(int statusCode, byte[] responseData) {
		super();
		this.statusCode = statusCode;
		this.responseData = responseData;
	}

	public JettyFriendlyException(int statusCode, byte[] responseData, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
		this.responseData = responseData;
	}

	public JettyFriendlyException(int statusCode, byte[] responseData, String message) {
		super(message);
		this.statusCode = statusCode;
		this.responseData = responseData;
	}

	public JettyFriendlyException(int statusCode, byte[] responseData, Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
		this.responseData = responseData;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public byte[] getResponseData() {
		return responseData;
	}

	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}
}
