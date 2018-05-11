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
 * 队列元素
 * 
 * @author kidalsama
 * 
 */
@SuppressWarnings("rawtypes")
public class Element {
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(Element.class);

	/**
	 * 事件类型
	 */
	private EventType type;

	/**
	 * 主键
	 */
	private final Object id;

	/**
	 * 实体
	 */
	private IEntity entity;

	/**
	 * 实体类型
	 */
	private final Class<? extends IEntity> entityClass;

	/**
	 * 创建队列元素
	 */
	public Element(EventType type, Object id, IEntity entity, Class<? extends IEntity> entityClass) {
		this.type = type;
		this.id = id;
		this.entity = entity;
		this.entityClass = entityClass;
	}

	/**
	 * 保存实体
	 */
	public static Element saveOf(IEntity entity) {
		return new Element(EventType.SAVE, entity.getId(), entity, entity.getClass());
	}

	/**
	 * 更新实体
	 */
	public static Element updateOf(IEntity entity) {
		return new Element(EventType.UPDATE, entity.getId(), entity, entity.getClass());
	}

	/**
	 * 删除实体
	 */
	public static Element removeOf(Object id, Class<? extends IEntity> entityClass) {
		return new Element(EventType.REMOVE, id, null, entityClass);
	}

	/**
	 * 获取标识
	 */
	public String getIdentity() {
		return entityClass.getName() + ":" + id;
	}

	/**
	 * 更新队列元素的状态<br/>
	 * 该方法不假设更新元素是同一个元素，因此元素判断要在使用该方法前处理
	 * 
	 * @param element 最新的元素状态
	 * @return true:需要保留,false:更新元素已经不需要保留
	 */
	public boolean update(Element element) {
		entity = element.getEntity();
		switch (type) {
		// 之前的状态为SAVE
		case SAVE:
			switch (element.getType()) {
			// 当前的状态
			case SAVE:
				logger.error("更新元素异常，实体[{}]原状态[{}]当前状态[{}]不进行修正",
						new Object[] { getIdentity(), type, element.getType() });
				break;
			case UPDATE:
				if (logger.isDebugEnabled()) {
					logger.debug("实体[{}]原状态[{}]当前状态[{}]修正后状态[{}]是否保留队列元素[{}]", new Object[] { getIdentity(),
							EventType.SAVE, element.getType(), type, true });
				}
				break;
			case REMOVE:
				if (logger.isDebugEnabled()) {
					logger.debug("实体[{}]原状态[{}]当前状态[{}]修正后状态[{}]是否保留队列元素[{}]", new Object[] { getIdentity(),
							EventType.SAVE, element.getType(), type, false });
				}
				return false;
			}
			break;
		// 之前的状态为UPDATE
		case UPDATE:
			switch (element.getType()) {
			case SAVE:
				logger.error("更新元素异常，实体[{}]原状态[{}]当前状态[{}]不进行修正",
						new Object[] { getIdentity(), type, element.getType() });
				break;
			case UPDATE:
				if (logger.isDebugEnabled()) {
					logger.debug("实体[{}]原状态[{}]当前状态[{}]修正后状态[{}]是否保留队列元素[{}]", new Object[] { getIdentity(),
							EventType.SAVE, element.getType(), type, true });
				}
				break;
			case REMOVE:
				type = EventType.REMOVE;
				if (logger.isDebugEnabled()) {
					logger.debug("实体[{}]原状态[{}]当前状态[{}]修正后状态[{}]是否保留队列元素[{}]", new Object[] { getIdentity(),
							EventType.SAVE, element.getType(), type, true });
				}
				break;
			}
			break;
		// 之前的状态为REMOVE
		case REMOVE:
			switch (element.getType()) {
			case SAVE:
				type = EventType.UPDATE;
				if (logger.isDebugEnabled()) {
					logger.debug("实体[{}]原状态[{}]当前状态[{}]修正后状态[{}]是否保留队列元素[{}]", new Object[] { getIdentity(),
							EventType.REMOVE, EventType.SAVE, type, true });
				}
				break;
			case UPDATE:
				logger.error("更新元素异常，实体[{}]原状态[{}]当前状态[{}]不进行修正",
						new Object[] { getIdentity(), type, element.getType() });
				break;
			case REMOVE:
				logger.error("更新元素异常，实体[{}]原状态[{}]当前状态[{}]不进行修正",
						new Object[] { getIdentity(), type, element.getType() });
				break;
			}
			break;
		}
		return true;
	}

	public EventType getType() {
		return type;
	}

	public Object getId() {
		return id;
	}

	public IEntity getEntity() {
		return entity;
	}

	public Class<? extends IEntity> getEntityClass() {
		return entityClass;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" ").append(type).append(":").append(entityClass.getSimpleName());
		builder.append(" Id:").append(id);
		return builder.toString();
	}
}
