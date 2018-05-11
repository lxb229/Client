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

package org.treediagram.nina.resource.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.treediagram.nina.resource.exception.DecodeException;

/**
 * JSON 资源读取器
 * 
 * @author kidal
 */
public class JsonReader implements ResourceReader {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

	public JsonReader()
	{
	}
	
	public <E> Iterator<E> read(InputStream input, Class<E> clz, String startMark) {
		try {
			JavaType type = typeFactory.constructCollectionType(ArrayList.class, clz);
			// JavaType type = TypeFactory.collectionType(ArrayList.class, clz);
			List<E> list = mapper.readValue(input, type);
			return list.iterator();
		} catch (Exception e) {
			throw new DecodeException(e);
		}
	}

	@Override
	public String getFormat() {
		return "json";
	}

	@Override
	public void config(String config) {

	}
}
