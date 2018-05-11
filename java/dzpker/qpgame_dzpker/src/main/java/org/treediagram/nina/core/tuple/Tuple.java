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

package org.treediagram.nina.core.tuple;

/**
 * 元组工具类
 * 
 * @author kidal
 * 
 */
public final class Tuple {
	public static <T1, T2> Tuple2<T1, T2> create(T1 item1, T2 item2) {
		return new Tuple2<T1, T2>(item1, item2);
	}

	public static <T1, T2, T3> Tuple3<T1, T2, T3> create(T1 item1, T2 item2, T3 item3) {
		return new Tuple3<T1, T2, T3>(item1, item2, item3);
	}

	public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> create(T1 item1, T2 item2, T3 item3, T4 item4) {
		return new Tuple4<T1, T2, T3, T4>(item1, item2, item3, item4);
	}

	public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> create(T1 item1, T2 item2, T3 item3, T4 item4,
			T5 item5) {
		return new Tuple5<T1, T2, T3, T4, T5>(item1, item2, item3, item4, item5);
	}

	public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> create(T1 item1, T2 item2, T3 item3,
			T4 item4, T5 item5, T6 item6) {
		return new Tuple6<T1, T2, T3, T4, T5, T6>(item1, item2, item3, item4, item5, item6);
	}

	public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> create(T1 item1, T2 item2, T3 item3,
			T4 item4, T5 item5, T6 item6, T7 item7) {
		return new Tuple7<T1, T2, T3, T4, T5, T6, T7>(item1, item2, item3, item4, item5, item6, item7);
	}

	private Tuple() {

	}
}
