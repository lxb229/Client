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

package org.treediagram.nina.core.lang;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * json工具类
 * 
 * @author kidal
 * 
 */
@SuppressWarnings("unchecked")
public final class JsonUtils {
	/**
	 * 类型工厂
	 */
	private static TypeFactory typeFactory = TypeFactory.defaultInstance();

	/**
	 * 对象映射
	 */
	private static final ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule("treediagram-nina", new Version(1, 0, 0, "RELEASE"));
		module.addSerializer(double.class, new DoubleSerializer());
		module.addSerializer(Double.class, new DoubleSerializer());
		module.addSerializer(float.class, new FloatSerializer());
		module.addSerializer(Float.class, new FloatSerializer());

		mapper.registerModule(module);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	}

	/**
	 * 
	 * @author kidal
	 *
	 */
	private static class FloatSerializer extends org.codehaus.jackson.map.ser.std.SerializerBase<Float> {
		/**
		 * 
		 */
		private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

		/**
		 * 
		 */
		static {
			DECIMAL_FORMAT.setMaximumFractionDigits(6);
		}

		/**
		 * 
		 */
		protected FloatSerializer() {
			super(Float.class);
		}

		/**
		 * 
		 */
		@Override
		public final void serializeWithType(Float value, JsonGenerator jgen, SerializerProvider provider,
				TypeSerializer typeSer) throws IOException, JsonGenerationException {
			serialize(value, jgen, provider);
		}

		/**
		 * 
		 */
		@Override
		public final void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonGenerationException {
			if (Float.isNaN(value) || Float.isInfinite(value)) {
				jgen.writeNumber(0);
			} else {
				String convertedValue = DECIMAL_FORMAT.format(value);
				convertedValue = convertedValue.replace(",", "");
				jgen.writeRawValue(convertedValue);
			}
		}
	}

	/**
	 * 
	 * @author kidal
	 *
	 */
	private static class DoubleSerializer extends org.codehaus.jackson.map.ser.std.SerializerBase<Double> {
		/**
		 * 
		 */
		private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

		/**
		 * 
		 */
		static {
			DECIMAL_FORMAT.setMaximumFractionDigits(6);
		}

		/**
		 * 
		 */
		protected DoubleSerializer() {
			super(Double.class);
		}

		/**
		 * 
		 */
		@Override
		public final void serializeWithType(Double value, JsonGenerator jgen, SerializerProvider provider,
				TypeSerializer typeSer) throws IOException, JsonGenerationException {
			serialize(value, jgen, provider);
		}

		/**
		 * 
		 */
		@Override
		public final void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonGenerationException {
			if (Double.isNaN(value) || Double.isInfinite(value)) {
				jgen.writeNumber(0);
			} else {
				String convertedValue = DECIMAL_FORMAT.format(value);
				convertedValue = convertedValue.replace(",", "");
				jgen.writeRawValue(convertedValue);
			}
		}

		/**
         * 
         */
		@Override
		public final JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
			return createSchemaNode("number", true);
		}
	}

	/**
	 * 
	 */
	private JsonUtils() {
	}

	/**
	 * 将对象转换为 JSON 的字符串格式
	 */
	public static String object2String(Object object) {
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, object);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将对象[{}]转换为JSON字符串时发生异常", object, e);
			throw new RuntimeException(message.getMessage(), e);
		}
		return writer.toString();
	}

	/**
	 * 将 map 转换为 JSON 的字符串格式
	 */
	public static String map2String(Map<?, ?> map) {
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, map);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将MAP[{}]转换为JSON字符串时发生异常", map, e);
			throw new RuntimeException(message.getMessage(), e);
		}
		return writer.toString();
	}

	/**
	 * 将 JSON 格式的字符串转换为 map
	 */
	public static Map<String, Object> string2Map(String content) {
		JavaType type = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
		try {
			return mapper.readValue(content, type);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为Map时出现异常", content);
			throw new RuntimeException(message.getMessage(), e);
		}
	}

	/**
	 * 将 JSON 格式的字符串转换为数组
	 */
	public static <T> T[] string2Array(String content, Class<T> clz) {
		@SuppressWarnings("deprecation")
		JavaType type = ArrayType.construct(typeFactory.constructType(clz));
		try {
			return (T[]) mapper.readValue(content, type);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为数组时出现异常", content, e);
			throw new RuntimeException(message.getMessage(), e);
		}
	}

	/**
	 * 将 JSON 格式的字符串转换为对象
	 */
	public static <T> T string2Object(String content, Class<T> clz) {
		JavaType type = typeFactory.constructType(clz);
		try {
			return (T) mapper.readValue(content, type);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为对象[{}]时出现异常",
					new Object[] { content, clz.getSimpleName(), e });
			throw new RuntimeException(message.getMessage(), e);
		}
	}

	/**
	 * 将 JSON 格式的字符串转换为集合
	 */
	public static <C extends Collection<E>, E> C string2Collection(String content, Class<C> collectionType,
			Class<E> elementType) {
		try {
			JavaType type = typeFactory.constructCollectionType(collectionType, elementType);
			return mapper.readValue(content, type);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为集合[{}]时出现异常", new Object[] { content,
					collectionType.getSimpleName(), e });
			throw new RuntimeException(message.getMessage(), e);
		}
	}

	/**
	 * 将字符串转换为{@link HashMap}对象实例
	 */
	public static <K, V> Map<K, V> string2Map(String content, Class<K> keyType, Class<V> valueType) {
		JavaType type = typeFactory.constructMapType(HashMap.class, keyType, valueType);
		try {
			return (Map<K, V>) mapper.readValue(content, type);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为Map时出现异常", content);
			throw new RuntimeException(message.getMessage(), e);
		}
	}

	/**
	 * 将字符串转换为特定的{@link Map}对象实例
	 */
	public static <M extends Map<K, V>, K, V> M string2Map(String content, Class<K> keyType, Class<V> valueType,
			Class<M> mapType) {
		JavaType type = typeFactory.constructMapType(mapType, keyType, valueType);
		try {
			return mapper.readValue(content, type);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("将字符串[{}]转换为Map时出现异常", content);
			throw new RuntimeException(message.getMessage(), e);
		}
	}
}
