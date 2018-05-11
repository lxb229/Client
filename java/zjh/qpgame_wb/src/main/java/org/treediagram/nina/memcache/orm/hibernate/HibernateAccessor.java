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

package org.treediagram.nina.memcache.orm.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.orm.Accessor;
import org.treediagram.nina.memcache.orm.EntityMetadata;
import org.treediagram.nina.memcache.orm.HibernateMetadata;

/**
 * {@link Accessor} 的 Hibernate 实现
 * 
 * @author kidal
 */
public class HibernateAccessor extends HibernateDaoSupport implements Accessor {

	private final Map<String, EntityMetadata> entityMetadataMap = new HashMap<String, EntityMetadata>();

	@PostConstruct
	public void postConstruct() {
		Map<String, ClassMetadata> classMetadataMap = this.getSessionFactory().getAllClassMetadata();
		for (ClassMetadata classMetadata : classMetadataMap.values()) {
			entityMetadataMap.put(classMetadata.getEntityName(), new HibernateMetadata(classMetadata));
		}
	}

	@Override
	public <PK, T extends IEntity<PK>> T load(Class<T> clz, PK id) {
		return getHibernateTemplate().get(clz, (Serializable) id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <PK, T extends IEntity<PK>> PK save(Class<T> clz, T entity) {
		return (PK) getHibernateTemplate().save(entity);
	}

	@Override
	public <PK, T extends IEntity<PK>> void remove(final Class<T> clz, final PK id) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final StringBuilder stringBuilder = new StringBuilder();
				final String name = clz.getSimpleName();
				final String primary = entityMetadataMap.get(clz.getName()).getPrimaryKey();

				stringBuilder.append("DELETE ").append(name).append(" ").append(name.charAt(0));
				stringBuilder.append(" WHERE ");
				stringBuilder.append(name.charAt(0)).append(".").append(primary).append("=:").append(primary);

				final Query query = session.createQuery(stringBuilder.toString());
				query.setParameter(primary, id);
				query.executeUpdate();

				return null;
			}
		});
	}

	@Override
	public <PK, T extends IEntity<PK>> void remove(Class<T> clz, T entity) {
		getHibernateTemplate().delete(entity);
	}

	@Override
	public <PK, T extends IEntity<PK>> void removeAll(final Class<T> clazz) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final StringBuilder stringBuilder = new StringBuilder();
				final String name = clazz.getSimpleName();

				stringBuilder.append("DELETE FROM ").append(name);

				final Query query = session.createQuery(stringBuilder.toString());
				query.executeUpdate();

				return null;
			}
		});
	}

	@Override
	public <PK, T extends IEntity<PK>> T update(Class<T> clz, T entity) {
		//System.out.println("record log  entity name is " +entity.getClass().getCanonicalName());
		getHibernateTemplate().update(entity);
		return entity;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <PK, T extends IEntity<PK>> void listAll(final Class<T> clz, final Collection<T> entities,
			final Integer offset, final Integer size) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria entityCriteria = session.createCriteria(clz);
				if (offset != null && size != null) {
					entityCriteria.setFirstResult(offset);
					entityCriteria.setMaxResults(size);
				}
				entities.addAll(entityCriteria.list());

				return null;
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public <PK, T extends IEntity<PK>> void listIntersection(final Class<T> clz, final Collection<T> entities,
			final Map<String, Object> keyValue, final Integer offset, final Integer size) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria entityCriteria = session.createCriteria(clz);
				final Iterator<Entry<String, Object>> iterator = keyValue.entrySet().iterator();
				Criterion left = null, right = null;
				if (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					left = Restrictions.eq(entry.getKey(), entry.getValue());
				}
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					right = Restrictions.eq(entry.getKey(), entry.getValue());
					left = Restrictions.and(left, right);
				}
				if (left != null) {
					entityCriteria.add(left);
				}
				if (offset != null && size != null) {
					entityCriteria.setFirstResult(offset);
					entityCriteria.setMaxResults(size);
				}
				entities.addAll(entityCriteria.list());

				return null;
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public <PK, T extends IEntity<PK>> void listUnion(final Class<T> clz, final Collection<T> entities,
			final Map<String, Object> keyValue, final Integer offset, final Integer size) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria entityCriteria = session.createCriteria(clz);
				final Iterator<Entry<String, Object>> iterator = keyValue.entrySet().iterator();
				Criterion left = null, right = null;
				if (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					left = Restrictions.eq(entry.getKey(), entry.getValue());
				}
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					right = Restrictions.eq(entry.getKey(), entry.getValue());
					left = Restrictions.or(left, right);
				}
				if (left != null) {
					entityCriteria.add(left);
				}
				if (offset != null && size != null) {
					entityCriteria.setFirstResult(offset);
					entityCriteria.setMaxResults(size);
				}
				entities.addAll(entityCriteria.list());

				return null;
			}
		});
	}

	@Override
	public Collection<EntityMetadata> getAllMetadata() {
		return entityMetadataMap.values();
	}

	/**
	 * 获取实体记录总数(具体实现扩展方法)
	 */
	public <PK, T extends IEntity<PK>> long countAll(final Class<T> clz) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria countCriteria = session.createCriteria(clz);
				countCriteria.setProjection(Projections.rowCount());
				final Object object = countCriteria.uniqueResult();
				return Long.class.cast(object);
			}
		});
	}

	/**
	 * 获取实体记录交集总数(具体实现扩展方法)
	 */
	public <PK, T extends IEntity<PK>> long countIntersection(final Class<T> clz, final Map<String, Object> keyValue) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria countCriteria = session.createCriteria(clz);
				final Iterator<Entry<String, Object>> iterator = keyValue.entrySet().iterator();
				Criterion left = null, right = null;
				if (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					left = Restrictions.eq(entry.getKey(), entry.getValue());
				}
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					right = Restrictions.eq(entry.getKey(), entry.getValue());
					left = Restrictions.and(left, right);
				}
				;
				if (left != null) {
					countCriteria.add(left);
				}

				countCriteria.setProjection(Projections.rowCount());
				final Object object = countCriteria.uniqueResult();
				return Number.class.cast(object).longValue();
			}
		});
	}

	/**
	 * 获取实体记录并集总数(具体实现扩展方法)
	 */
	public <PK, T extends IEntity<PK>> long countUnion(final Class<T> clz, final Map<String, Object> keyValue) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria countCriteria = session.createCriteria(clz);
				final Iterator<Entry<String, Object>> iterator = keyValue.entrySet().iterator();
				Criterion left = null, right = null;
				if (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					left = Restrictions.eq(entry.getKey(), entry.getValue());
				}
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					right = Restrictions.eq(entry.getKey(), entry.getValue());
					left = Restrictions.or(left, right);
				}
				;
				if (left != null) {
					countCriteria.add(left);
				}

				countCriteria.setProjection(Projections.rowCount());
				final Object object = countCriteria.uniqueResult();
				return Long.class.cast(object);
			}
		});
	}

	/**
	 * 按照指定条件遍历实体(具体实现扩展方法)
	 */
	public <PK, T extends IEntity<PK>> void cursor(final Class<T> clz, final Criterion criterion,
			final CursorCallback<T> callback) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final Criteria entityCriteria = session.createCriteria(clz);
				if (criterion != null) {
					entityCriteria.add(criterion);
				}
				final ScrollableResults scrollableResults = entityCriteria.scroll(ScrollMode.FORWARD_ONLY);
				try {
					while (scrollableResults.next()) {
						try {
							final T entity = clz.cast(scrollableResults.get(0));
							callback.call(entity);
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					}
				} finally {
					scrollableResults.close();
				}

				return null;
			}
		});
	}

	/**
	 * 删除指定实体记录并集(具体实现扩展方法)
	 */
	public <PK, T extends IEntity<PK>> Collection<T> removeUnion(Class<T> clz, Map<String, Object> keyValue) {
		final LinkedList<T> entities = new LinkedList<T>();
		this.listUnion(clz, entities, keyValue, null, null);
		getHibernateTemplate().deleteAll(entities);
		return entities;
	}

	/**
	 * 删除指定实体记录交集(具体实现扩展方法)
	 */
	public <PK, T extends IEntity<PK>> void removeIntersection(final Class<T> clz, final Map<String, Object> keyValue) {
		getHibernateTemplate().execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				final StringBuilder stringBuilder = new StringBuilder();
				final String name = clz.getSimpleName();

				stringBuilder.append("DELETE ").append(name).append(" ").append(name.charAt(0));
				if (!keyValue.isEmpty()) {
					stringBuilder.append(" WHERE ");
					for (Entry<String, Object> entry : keyValue.entrySet()) {
						stringBuilder.append(name.charAt(0)).append(".").append(entry.getKey()).append("=:")
								.append(entry.getKey());
						stringBuilder.append(" AND ");
					}
					stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length());
				}

				final Query query = session.createQuery(stringBuilder.toString());
				for (Entry<String, Object> entry : keyValue.entrySet()) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
				query.executeUpdate();

				return null;
			}
		});
	}
}
