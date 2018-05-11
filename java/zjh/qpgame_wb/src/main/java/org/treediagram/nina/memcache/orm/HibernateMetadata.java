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

package org.treediagram.nina.memcache.orm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Version;

import org.apache.commons.lang3.ClassUtils;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.treediagram.nina.memcache.IEntity;

/**
 * 冬眠实体原数据
 * 
 * @author kidal
 * 
 */
@SuppressWarnings("rawtypes")
public class HibernateMetadata implements EntityMetadata {

	private String entityName;
	private Map<String, String> fields = new HashMap<String, String>();
	private String name;
	private String primaryKey;
	private Collection<String> indexKeys = new HashSet<String>();
	private String versionKey;

	public HibernateMetadata() {
		
	}

	public HibernateMetadata(final ClassMetadata classMetadata) {
		this.entityName = classMetadata.getEntityName();
		try {
			final Class entityClazz = Class.forName(this.entityName);

			ReflectionUtils.doWithFields(entityClazz, new FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					if (field.isAnnotationPresent(Version.class)) {
						versionKey = field.getName();
						return;
					}
					if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
						return;
					}
					if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
						primaryKey = field.getName();
					}
					final Class<?> type = ClassUtils.primitiveToWrapper(field.getType());
					if (ClassUtils.wrapperToPrimitive(type) != null || String.class == type) {
						fields.put(field.getName(), type.getName());
					} else if (type.isEnum()) {
						fields.put(field.getName(), type.getName());
					} else if (Collection.class.isAssignableFrom(type) || type.isArray()) {
						fields.put(field.getName(), List.class.getName());
					} else if (Date.class.isAssignableFrom(type)) {
						fields.put(field.getName(), Date.class.getName());
					} else {
						fields.put(field.getName(), Map.class.getName());
					}
				}
			});

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new IllegalArgumentException("Hibernate 实体类" + this.entityName + "不存在");
		}

		if (this.primaryKey == null) {
			throw new IllegalArgumentException("Hibernate 实体类" + this.entityName + "缺少主键");
		}

		this.name = this.entityName.substring(this.entityName.lastIndexOf('.'));
	}

	public String getEntityName() {
		return entityName;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public String getName() {
		return this.name;
	}

	public String getPrimaryKey() {
		return this.primaryKey;
	}

	public Collection<String> getIndexKeys() {
		return indexKeys;
	}

	public String getVersionKey() {
		return this.versionKey;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setIndexKeys(Collection<String> indexKeys) {
		this.indexKeys = indexKeys;
	}

	public void setVersionKey(String versionKey) {
		this.versionKey = versionKey;
	}

	public String toString() {
		return this.getName();
	}

	@Override
	public <PK extends Serializable> Class<PK> getPrimaryKeyClass() {
		try {
			@SuppressWarnings("unchecked")
			Class<PK> clz = (Class<PK>) Class.forName(this.fields.get(this.primaryKey));
			return clz;
		} catch (ClassNotFoundException exception) {
			return null;
		}
	}

	@Override
	public <T extends IEntity> Class<T> getEntityClass() {
		try {
			@SuppressWarnings("unchecked")
			Class<T> clz = (Class<T>) Class.forName(this.entityName);
			return clz;
		} catch (ClassNotFoundException exception) {
			return null;
		}
	}
}
