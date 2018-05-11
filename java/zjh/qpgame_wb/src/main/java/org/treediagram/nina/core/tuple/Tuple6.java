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
 * 六元组
 * 
 * @author kidal
 */
public final class Tuple6<T1, T2, T3, T4, T5, T6> {
	private T1 item1;
	private T2 item2;
	private T3 item3;
	private T4 item4;
	private T5 item5;
	private T6 item6;

	public Tuple6(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6) {
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
		this.item4 = item4;
		this.item5 = item5;
		this.item6 = item6;
	}

	public T1 getItem1() {
		return item1;
	}

	public T2 getItem2() {
		return item2;
	}

	public T3 getItem3() {
		return item3;
	}

	public T4 getItem4() {
		return item4;
	}

	public T5 getItem5() {
		return item5;
	}

	public T6 getItem6() {
		return item6;
	}
}
