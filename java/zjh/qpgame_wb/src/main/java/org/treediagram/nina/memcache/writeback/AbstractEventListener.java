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

package org.treediagram.nina.memcache.writeback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treediagram.nina.memcache.IEntity;

/**
 * 抽象监听器用于简化监听器的开发
 * 
 * @author kidal
 */
public abstract class AbstractEventListener implements EventListener {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractEventListener.class);

	@Override
	public void onNotify(EventType type, boolean isSuccess, Object id, IEntity<?> entity, RuntimeException ex) {
		try {
			if (isSuccess) {
				switch (type) {
				case SAVE:
					onSaveSuccess(id, entity);
					break;
				case UPDATE:
					onUpdateSuccess(entity);
					break;
				case REMOVE:
					onRemoveSuccess(id);
					break;
				default:
					logger.error("未支持的更新事件类型[{}]", type);
					break;
				}
			} else {
				switch (type) {
				case SAVE:
					onSaveError(id, entity, ex);
					break;
				case UPDATE:
					onUpdateError(entity, ex);
					break;
				case REMOVE:
					onRemoveError(id, ex);
					break;
				default:
					logger.error("未支持的更新事件类型[{}]", type);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("队列监听器[{}]处理出现异常", new Object[] { this.getClass().getName(), e });
		}
	}

	/**
	 * 实体保存成功时的回调，需要则覆盖
	 */
	protected void onSaveSuccess(Object id, IEntity<?> entity) {

	}

	/**
	 * 实体更新成功时的回调，需要则覆盖
	 */
	protected void onUpdateSuccess(IEntity<?> entity) {

	}

	/**
	 * 实体删除成功时的回调，需要则覆盖
	 */
	protected void onRemoveSuccess(Object id) {

	}

	/**
	 * 实体保存失败时的回调，需要则覆盖
	 */
	protected void onSaveError(Object id, IEntity<?> entity, RuntimeException ex) {

	}

	/**
	 * 实体更新失败时的回调，需要则覆盖
	 */
	protected void onUpdateError(IEntity<?> entity, RuntimeException ex) {

	}

	/**
	 * 实体删除失败时的回调，需要则覆盖
	 */
	protected void onRemoveError(Object id, RuntimeException ex) {

	}

}
