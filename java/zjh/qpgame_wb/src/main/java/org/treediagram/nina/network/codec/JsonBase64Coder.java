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

package org.treediagram.nina.network.codec;

import org.apache.commons.codec.binary.Base64;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.exception.CoderDecodeException;
import org.treediagram.nina.network.exception.CoderEncodeException;

/**
 * Json编码后使用Base64编码
 * 
 * @author kidal
 *
 */
public class JsonBase64Coder extends JsonCoder {
	/**
	 * {@link Coder#decode(byte[])}
	 */
	@Override
	public Object decode(byte[] bytes) {
		try {
			final byte[] jsonBytes = Base64.decodeBase64(bytes);
			final String jsonString = new String(jsonBytes, "UTF-8");
			return JsonUtils.string2Map(jsonString.replace("[]", "null"));
		} catch (Exception e) {
			throw new CoderDecodeException(e);
		}
	}

	/**
	 * {@link Coder#encode(Object)}
	 */
	@Override
	public byte[] encode(Object object) {
		try {
			final byte[] bodyBytes = JsonUtils.object2String(object).replace("[]", "null").getBytes("UTF-8");
			final byte[] base64Bytes = Base64.encodeBase64(bodyBytes);
			return base64Bytes;
		} catch (Exception e) {
			throw new CoderEncodeException(e);
		}
	}
}
