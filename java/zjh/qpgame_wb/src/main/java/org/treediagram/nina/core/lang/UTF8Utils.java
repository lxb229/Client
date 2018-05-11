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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UTF8字符工具类
 * 
 * @author kidal
 */
public class UTF8Utils {
	/**
	 * 检测utf8mb4编码
	 */
	public static boolean checkUTF8MB4(String str) throws UnsupportedEncodingException {
		if (null == str || str.length() == 0) {
			return false;
		}
		byte[] bytes = str.getBytes("UTF-8");
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			// 四字节字符即utf8mb4字符
			if ((b & 0xF0) == 0xF0) {
				return true;
			}
			// 三字节字符
			if ((b & 0xE0) == 0xE0) {
				i += 2;
				continue;
			}
			// 双字节字符
			if ((b & 0xC0) == 0xC0) {
				i += 1;
				continue;
			}
			// 单字节字符(跳过)
			// if (b >> 7 == 0) {
			// continue;
			// }
		}
		return false;
	}

	private static Map<String, Pattern> replaceTables = new HashMap<String, Pattern>();

	static {
		String content = "{" //
				+ "\"1\":\"[1¹]\","// 1
				+ "\"2\":\"[2²]\","// 2
				+ "\"3\":\"[3³]\","// 3
				+ "\"A\":\"[AaªÀÁÂÃÄÅàáâãäåẢảẠạĂăẰằẮắẲẳẴẵẶặẦầẤấẨẩẪẫẬậ]\","// A
				+ "\"B\":\"[Bb]\","// B
				+ "\"C\":\"[CcÇç]\","// C
				+ "\"D\":\"[Dd]\","// D
				+ "\"E\":\"[EeÈÉÊËèéêëẺẻẼẽẸẹỀềẾếỂểỄễỆệ]\","// E
				+ "\"F\":\"[Ff]\","// F
				+ "\"G\":\"[Gg]\","// G
				+ "\"H\":\"[Hh]\","// H
				+ "\"I\":\"[IiÌÍÎÏìíîïỈỉĨĩỊị]\","// I
				+ "\"J\":\"[Jj]\","// J
				+ "\"K\":\"[Kk]\","// K
				+ "\"L\":\"[Ll]\","// L
				+ "\"M\":\"[Mm]\","// M
				+ "\"N\":\"[NnÑñ]\","// N
				+ "\"O\":\"[OoºÒÓÔÕÖòóôõöỎỏỌọỒồỐốỔổỖỗỘộƠơỜờỚớỞởỠỡỢợ]\","// O
				+ "\"P\":\"[Pp]\","// P
				+ "\"Q\":\"[Qq]\","// Q
				+ "\"R\":\"[Rr]\","// R
				+ "\"S\":\"[SsŠš]\","// S
				+ "\"T\":\"[Tt]\","// T
				+ "\"U\":\"[UuÙÚÛÜùúûüỦủŨũỤụƯưỪừỨứỬửỮữỰự]\","// U
				+ "\"V\":\"[Vv]\","// V
				+ "\"W\":\"[Ww]\","// W
				+ "\"X\":\"[Xx]\","// X
				+ "\"Y\":\"[YyŸÝýỲỳỶỷỸỹỴỵ]\","// Y
				+ "\"Z\":\"[ZzŽž]\","// Z
				+ "\"Œ\":\"[Œœ]\","// Œ
				+ "\"Æ\":\"[Ææ]\","// Æ
				+ "\"Ð\":\"[Ðð]\","// Ð
				+ "\"Ø\":\"[Øø]\","// Ø
				+ "\"Þ\":\"[Þþ]\""// Þ
				+ "}";
		Map<String, String> map = JsonUtils.string2Map(content, String.class, String.class);
		for (Entry<String, String> e : map.entrySet()) {
			String key = e.getKey();
			Pattern patten = Pattern.compile(e.getValue());
			replaceTables.put(key, patten);
		}

	}

	/**
	 * 转换为字符串唯一键
	 * 
	 * <pre>
	 * "1" : "[1¹]",
	 * "2" : "[2²]",
	 * "3" : "[3³]",
	 * "A" : "[AaªÀÁÂÃÄÅàáâãäåẢảẠạĂăẰằẮắẲẳẴẵẶặẦầẤấẨẩẪẫẬậ]",
	 * "B" : "[Bb]",
	 * "C" : "[CcÇç]",
	 * "D" : "[Dd]",
	 * "E" : "[EeÈÉÊËèéêëẺẻẼẽẸẹỀềẾếỂểỄễỆệ]",
	 * "F" : "[Ff]",
	 * "G" : "[Gg]",
	 * "H" : "[Hh]",
	 * "I" : "[IiÌÍÎÏìíîïỈỉĨĩỊị]",
	 * "J" : "[Jj]",
	 * "K" : "[Kk]",
	 * "L" : "[Ll]",
	 * "M" : "[Mm]",
	 * "N" : "[NnÑñ]",
	 * "O" : "[OoºÒÓÔÕÖòóôõöỎỏỌọỒồỐốỔổỖỗỘộƠơỜờỚớỞởỠỡỢợ]",
	 * "P" : "[Pp]",
	 * "Q" : "[Qq]",
	 * "R" : "[Rr]",
	 * "S" : "[SsŠš]",
	 * "T" : "[Tt]",
	 * "U" : "[UuÙÚÛÜùúûüỦủŨũỤụƯưỪừỨứỬửỮữỰự]",
	 * "V" : "[Vv]",
	 * "W" : "[Ww]",
	 * "X" : "[Xx]",
	 * "Y" : "[YyŸÝýỲỳỶỷỸỹỴỵ]",
	 * "Z" : "[ZzŽž]",
	 * "Œ" : "[Œœ]",
	 * "Æ" : "[Ææ]",
	 * "Ð" : "[Ðð]",
	 * "Ø" : "[Øø]",
	 * "Þ" : "[Þþ]",
	 * </pre>
	 */
	public static String toUniqueKey(String str) {
		String key = str.toUpperCase();
		for (Entry<String, Pattern> e : replaceTables.entrySet()) {
			String k = e.getKey();
			Pattern v = e.getValue();
			Matcher matcher = v.matcher(key);
			if (matcher.find()) {
				key = matcher.replaceAll(k);
			}
		}
		return key;
	}
}
