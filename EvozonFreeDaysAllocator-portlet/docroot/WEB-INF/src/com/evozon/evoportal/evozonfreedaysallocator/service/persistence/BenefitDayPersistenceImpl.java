/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.evozon.evoportal.evozonfreedaysallocator.service.persistence;

import com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException;
import com.evozon.evoportal.evozonfreedaysallocator.model.BenefitDay;
import com.evozon.evoportal.evozonfreedaysallocator.model.impl.BenefitDayImpl;
import com.evozon.evoportal.evozonfreedaysallocator.model.impl.BenefitDayModelImpl;

import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the benefit day service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Cristina Kiss
 * @see BenefitDayPersistence
 * @see BenefitDayUtil
 * @generated
 */
public class BenefitDayPersistenceImpl extends BasePersistenceImpl<BenefitDay>
	implements BenefitDayPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link BenefitDayUtil} to access the benefit day persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = BenefitDayImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] { Long.class.getName() },
			BenefitDayModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_TYPE = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByType",
			new String[] {
				String.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TYPE = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByType",
			new String[] { String.class.getName() },
			BenefitDayModelImpl.TYPE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_TYPE = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByType",
			new String[] { String.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID_TYPE =
		new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId_Type",
			new String[] {
				Long.class.getName(), String.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_TYPE =
		new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId_Type",
			new String[] { Long.class.getName(), String.class.getName() },
			BenefitDayModelImpl.USERID_COLUMN_BITMASK |
			BenefitDayModelImpl.TYPE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_TYPE = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId_Type",
			new String[] { Long.class.getName(), String.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, BenefitDayImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	/**
	 * Caches the benefit day in the entity cache if it is enabled.
	 *
	 * @param benefitDay the benefit day
	 */
	public void cacheResult(BenefitDay benefitDay) {
		EntityCacheUtil.putResult(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayImpl.class, benefitDay.getPrimaryKey(), benefitDay);

		benefitDay.resetOriginalValues();
	}

	/**
	 * Caches the benefit daies in the entity cache if it is enabled.
	 *
	 * @param benefitDaies the benefit daies
	 */
	public void cacheResult(List<BenefitDay> benefitDaies) {
		for (BenefitDay benefitDay : benefitDaies) {
			if (EntityCacheUtil.getResult(
						BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
						BenefitDayImpl.class, benefitDay.getPrimaryKey()) == null) {
				cacheResult(benefitDay);
			}
			else {
				benefitDay.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all benefit daies.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(BenefitDayImpl.class.getName());
		}

		EntityCacheUtil.clearCache(BenefitDayImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the benefit day.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(BenefitDay benefitDay) {
		EntityCacheUtil.removeResult(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayImpl.class, benefitDay.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<BenefitDay> benefitDaies) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (BenefitDay benefitDay : benefitDaies) {
			EntityCacheUtil.removeResult(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
				BenefitDayImpl.class, benefitDay.getPrimaryKey());
		}
	}

	/**
	 * Creates a new benefit day with the primary key. Does not add the benefit day to the database.
	 *
	 * @param entryId the primary key for the new benefit day
	 * @return the new benefit day
	 */
	public BenefitDay create(long entryId) {
		BenefitDay benefitDay = new BenefitDayImpl();

		benefitDay.setNew(true);
		benefitDay.setPrimaryKey(entryId);

		return benefitDay;
	}

	/**
	 * Removes the benefit day with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the benefit day
	 * @return the benefit day that was removed
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay remove(long entryId)
		throws NoSuchBenefitDayException, SystemException {
		return remove(Long.valueOf(entryId));
	}

	/**
	 * Removes the benefit day with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the benefit day
	 * @return the benefit day that was removed
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public BenefitDay remove(Serializable primaryKey)
		throws NoSuchBenefitDayException, SystemException {
		Session session = null;

		try {
			session = openSession();

			BenefitDay benefitDay = (BenefitDay)session.get(BenefitDayImpl.class,
					primaryKey);

			if (benefitDay == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchBenefitDayException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(benefitDay);
		}
		catch (NoSuchBenefitDayException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected BenefitDay removeImpl(BenefitDay benefitDay)
		throws SystemException {
		benefitDay = toUnwrappedModel(benefitDay);

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.delete(session, benefitDay);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		clearCache(benefitDay);

		return benefitDay;
	}

	@Override
	public BenefitDay updateImpl(
		com.evozon.evoportal.evozonfreedaysallocator.model.BenefitDay benefitDay,
		boolean merge) throws SystemException {
		benefitDay = toUnwrappedModel(benefitDay);

		boolean isNew = benefitDay.isNew();

		BenefitDayModelImpl benefitDayModelImpl = (BenefitDayModelImpl)benefitDay;

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, benefitDay, merge);

			benefitDay.setNew(false);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !BenefitDayModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((benefitDayModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(benefitDayModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] {
						Long.valueOf(benefitDayModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}

			if ((benefitDayModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TYPE.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						benefitDayModelImpl.getOriginalType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TYPE, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TYPE,
					args);

				args = new Object[] { benefitDayModelImpl.getType() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TYPE, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TYPE,
					args);
			}

			if ((benefitDayModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_TYPE.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(benefitDayModelImpl.getOriginalUserId()),
						
						benefitDayModelImpl.getOriginalType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_TYPE,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_TYPE,
					args);

				args = new Object[] {
						Long.valueOf(benefitDayModelImpl.getUserId()),
						
						benefitDayModelImpl.getType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_TYPE,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_TYPE,
					args);
			}
		}

		EntityCacheUtil.putResult(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
			BenefitDayImpl.class, benefitDay.getPrimaryKey(), benefitDay);

		return benefitDay;
	}

	protected BenefitDay toUnwrappedModel(BenefitDay benefitDay) {
		if (benefitDay instanceof BenefitDayImpl) {
			return benefitDay;
		}

		BenefitDayImpl benefitDayImpl = new BenefitDayImpl();

		benefitDayImpl.setNew(benefitDay.isNew());
		benefitDayImpl.setPrimaryKey(benefitDay.getPrimaryKey());

		benefitDayImpl.setEntryId(benefitDay.getEntryId());
		benefitDayImpl.setType(benefitDay.getType());
		benefitDayImpl.setUserId(benefitDay.getUserId());
		benefitDayImpl.setDaysNo(benefitDay.getDaysNo());
		benefitDayImpl.setAllocatedDate(benefitDay.getAllocatedDate());
		benefitDayImpl.setComment(benefitDay.getComment());

		return benefitDayImpl;
	}

	/**
	 * Returns the benefit day with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the benefit day
	 * @return the benefit day
	 * @throws com.liferay.portal.NoSuchModelException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public BenefitDay findByPrimaryKey(Serializable primaryKey)
		throws NoSuchModelException, SystemException {
		return findByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the benefit day with the primary key or throws a {@link com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException} if it could not be found.
	 *
	 * @param entryId the primary key of the benefit day
	 * @return the benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByPrimaryKey(long entryId)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByPrimaryKey(entryId);

		if (benefitDay == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + entryId);
			}

			throw new NoSuchBenefitDayException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				entryId);
		}

		return benefitDay;
	}

	/**
	 * Returns the benefit day with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the benefit day
	 * @return the benefit day, or <code>null</code> if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public BenefitDay fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		return fetchByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the benefit day with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the benefit day
	 * @return the benefit day, or <code>null</code> if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByPrimaryKey(long entryId) throws SystemException {
		BenefitDay benefitDay = (BenefitDay)EntityCacheUtil.getResult(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
				BenefitDayImpl.class, entryId);

		if (benefitDay == _nullBenefitDay) {
			return null;
		}

		if (benefitDay == null) {
			Session session = null;

			boolean hasException = false;

			try {
				session = openSession();

				benefitDay = (BenefitDay)session.get(BenefitDayImpl.class,
						Long.valueOf(entryId));
			}
			catch (Exception e) {
				hasException = true;

				throw processException(e);
			}
			finally {
				if (benefitDay != null) {
					cacheResult(benefitDay);
				}
				else if (!hasException) {
					EntityCacheUtil.putResult(BenefitDayModelImpl.ENTITY_CACHE_ENABLED,
						BenefitDayImpl.class, entryId, _nullBenefitDay);
				}

				closeSession(session);
			}
		}

		return benefitDay;
	}

	/**
	 * Returns all the benefit daies where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByUserId(long userId) throws SystemException {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the benefit daies where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @return the range of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByUserId(long userId, int start, int end)
		throws SystemException {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the benefit daies where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByUserId(long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId, start, end, orderByComparator };
		}

		List<BenefitDay> list = (List<BenefitDay>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (BenefitDay benefitDay : list) {
				if ((userId != benefitDay.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_BENEFITDAY_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(BenefitDayModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				list = (List<BenefitDay>)QueryUtil.list(q, getDialect(), start,
						end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first benefit day in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByUserId_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByUserId_First(userId, orderByComparator);

		if (benefitDay != null) {
			return benefitDay;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBenefitDayException(msg.toString());
	}

	/**
	 * Returns the first benefit day in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching benefit day, or <code>null</code> if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByUserId_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<BenefitDay> list = findByUserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last benefit day in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByUserId_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByUserId_Last(userId, orderByComparator);

		if (benefitDay != null) {
			return benefitDay;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBenefitDayException(msg.toString());
	}

	/**
	 * Returns the last benefit day in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching benefit day, or <code>null</code> if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByUserId_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId(userId);

		List<BenefitDay> list = findByUserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the benefit daies before and after the current benefit day in the ordered set where userId = &#63;.
	 *
	 * @param entryId the primary key of the current benefit day
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay[] findByUserId_PrevAndNext(long entryId, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			BenefitDay[] array = new BenefitDayImpl[3];

			array[0] = getByUserId_PrevAndNext(session, benefitDay, userId,
					orderByComparator, true);

			array[1] = benefitDay;

			array[2] = getByUserId_PrevAndNext(session, benefitDay, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected BenefitDay getByUserId_PrevAndNext(Session session,
		BenefitDay benefitDay, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BENEFITDAY_WHERE);

		query.append(_FINDER_COLUMN_USERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(BenefitDayModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(benefitDay);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<BenefitDay> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the benefit daies where type = &#63;.
	 *
	 * @param type the type
	 * @return the matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByType(String type) throws SystemException {
		return findByType(type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the benefit daies where type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param type the type
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @return the range of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByType(String type, int start, int end)
		throws SystemException {
		return findByType(type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the benefit daies where type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param type the type
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByType(String type, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TYPE;
			finderArgs = new Object[] { type };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_TYPE;
			finderArgs = new Object[] { type, start, end, orderByComparator };
		}

		List<BenefitDay> list = (List<BenefitDay>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (BenefitDay benefitDay : list) {
				if (!Validator.equals(type, benefitDay.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_BENEFITDAY_WHERE);

			if (type == null) {
				query.append(_FINDER_COLUMN_TYPE_TYPE_1);
			}
			else {
				if (type.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_TYPE_TYPE_3);
				}
				else {
					query.append(_FINDER_COLUMN_TYPE_TYPE_2);
				}
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(BenefitDayModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (type != null) {
					qPos.add(type);
				}

				list = (List<BenefitDay>)QueryUtil.list(q, getDialect(), start,
						end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first benefit day in the ordered set where type = &#63;.
	 *
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByType_First(String type,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByType_First(type, orderByComparator);

		if (benefitDay != null) {
			return benefitDay;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBenefitDayException(msg.toString());
	}

	/**
	 * Returns the first benefit day in the ordered set where type = &#63;.
	 *
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching benefit day, or <code>null</code> if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByType_First(String type,
		OrderByComparator orderByComparator) throws SystemException {
		List<BenefitDay> list = findByType(type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last benefit day in the ordered set where type = &#63;.
	 *
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByType_Last(String type,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByType_Last(type, orderByComparator);

		if (benefitDay != null) {
			return benefitDay;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBenefitDayException(msg.toString());
	}

	/**
	 * Returns the last benefit day in the ordered set where type = &#63;.
	 *
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching benefit day, or <code>null</code> if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByType_Last(String type,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByType(type);

		List<BenefitDay> list = findByType(type, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the benefit daies before and after the current benefit day in the ordered set where type = &#63;.
	 *
	 * @param entryId the primary key of the current benefit day
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay[] findByType_PrevAndNext(long entryId, String type,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			BenefitDay[] array = new BenefitDayImpl[3];

			array[0] = getByType_PrevAndNext(session, benefitDay, type,
					orderByComparator, true);

			array[1] = benefitDay;

			array[2] = getByType_PrevAndNext(session, benefitDay, type,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected BenefitDay getByType_PrevAndNext(Session session,
		BenefitDay benefitDay, String type,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BENEFITDAY_WHERE);

		if (type == null) {
			query.append(_FINDER_COLUMN_TYPE_TYPE_1);
		}
		else {
			if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_TYPE_TYPE_3);
			}
			else {
				query.append(_FINDER_COLUMN_TYPE_TYPE_2);
			}
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(BenefitDayModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (type != null) {
			qPos.add(type);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(benefitDay);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<BenefitDay> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the benefit daies where userId = &#63; and type = &#63;.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @return the matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByUserId_Type(long userId, String type)
		throws SystemException {
		return findByUserId_Type(userId, type, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the benefit daies where userId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @return the range of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByUserId_Type(long userId, String type,
		int start, int end) throws SystemException {
		return findByUserId_Type(userId, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the benefit daies where userId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findByUserId_Type(long userId, String type,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_TYPE;
			finderArgs = new Object[] { userId, type };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID_TYPE;
			finderArgs = new Object[] {
					userId, type,
					
					start, end, orderByComparator
				};
		}

		List<BenefitDay> list = (List<BenefitDay>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (BenefitDay benefitDay : list) {
				if ((userId != benefitDay.getUserId()) ||
						!Validator.equals(type, benefitDay.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_BENEFITDAY_WHERE);

			query.append(_FINDER_COLUMN_USERID_TYPE_USERID_2);

			if (type == null) {
				query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_1);
			}
			else {
				if (type.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_3);
				}
				else {
					query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_2);
				}
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(BenefitDayModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (type != null) {
					qPos.add(type);
				}

				list = (List<BenefitDay>)QueryUtil.list(q, getDialect(), start,
						end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first benefit day in the ordered set where userId = &#63; and type = &#63;.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByUserId_Type_First(long userId, String type,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByUserId_Type_First(userId, type,
				orderByComparator);

		if (benefitDay != null) {
			return benefitDay;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBenefitDayException(msg.toString());
	}

	/**
	 * Returns the first benefit day in the ordered set where userId = &#63; and type = &#63;.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching benefit day, or <code>null</code> if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByUserId_Type_First(long userId, String type,
		OrderByComparator orderByComparator) throws SystemException {
		List<BenefitDay> list = findByUserId_Type(userId, type, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last benefit day in the ordered set where userId = &#63; and type = &#63;.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay findByUserId_Type_Last(long userId, String type,
		OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = fetchByUserId_Type_Last(userId, type,
				orderByComparator);

		if (benefitDay != null) {
			return benefitDay;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchBenefitDayException(msg.toString());
	}

	/**
	 * Returns the last benefit day in the ordered set where userId = &#63; and type = &#63;.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching benefit day, or <code>null</code> if a matching benefit day could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay fetchByUserId_Type_Last(long userId, String type,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId_Type(userId, type);

		List<BenefitDay> list = findByUserId_Type(userId, type, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the benefit daies before and after the current benefit day in the ordered set where userId = &#63; and type = &#63;.
	 *
	 * @param entryId the primary key of the current benefit day
	 * @param userId the user ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next benefit day
	 * @throws com.evozon.evoportal.evozonfreedaysallocator.NoSuchBenefitDayException if a benefit day with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public BenefitDay[] findByUserId_Type_PrevAndNext(long entryId,
		long userId, String type, OrderByComparator orderByComparator)
		throws NoSuchBenefitDayException, SystemException {
		BenefitDay benefitDay = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			BenefitDay[] array = new BenefitDayImpl[3];

			array[0] = getByUserId_Type_PrevAndNext(session, benefitDay,
					userId, type, orderByComparator, true);

			array[1] = benefitDay;

			array[2] = getByUserId_Type_PrevAndNext(session, benefitDay,
					userId, type, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected BenefitDay getByUserId_Type_PrevAndNext(Session session,
		BenefitDay benefitDay, long userId, String type,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_BENEFITDAY_WHERE);

		query.append(_FINDER_COLUMN_USERID_TYPE_USERID_2);

		if (type == null) {
			query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_1);
		}
		else {
			if (type.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_3);
			}
			else {
				query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_2);
			}
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(BenefitDayModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (type != null) {
			qPos.add(type);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(benefitDay);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<BenefitDay> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the benefit daies.
	 *
	 * @return the benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the benefit daies.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @return the range of benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the benefit daies.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of benefit daies
	 * @param end the upper bound of the range of benefit daies (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public List<BenefitDay> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = new Object[] { start, end, orderByComparator };

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<BenefitDay> list = (List<BenefitDay>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_BENEFITDAY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_BENEFITDAY.concat(BenefitDayModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (orderByComparator == null) {
					list = (List<BenefitDay>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<BenefitDay>)QueryUtil.list(q, getDialect(),
							start, end);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the benefit daies where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByUserId(long userId) throws SystemException {
		for (BenefitDay benefitDay : findByUserId(userId)) {
			remove(benefitDay);
		}
	}

	/**
	 * Removes all the benefit daies where type = &#63; from the database.
	 *
	 * @param type the type
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByType(String type) throws SystemException {
		for (BenefitDay benefitDay : findByType(type)) {
			remove(benefitDay);
		}
	}

	/**
	 * Removes all the benefit daies where userId = &#63; and type = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByUserId_Type(long userId, String type)
		throws SystemException {
		for (BenefitDay benefitDay : findByUserId_Type(userId, type)) {
			remove(benefitDay);
		}
	}

	/**
	 * Removes all the benefit daies from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (BenefitDay benefitDay : findAll()) {
			remove(benefitDay);
		}
	}

	/**
	 * Returns the number of benefit daies where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUserId(long userId) throws SystemException {
		Object[] finderArgs = new Object[] { userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_BENEFITDAY_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of benefit daies where type = &#63;.
	 *
	 * @param type the type
	 * @return the number of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public int countByType(String type) throws SystemException {
		Object[] finderArgs = new Object[] { type };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_TYPE,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_BENEFITDAY_WHERE);

			if (type == null) {
				query.append(_FINDER_COLUMN_TYPE_TYPE_1);
			}
			else {
				if (type.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_TYPE_TYPE_3);
				}
				else {
					query.append(_FINDER_COLUMN_TYPE_TYPE_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (type != null) {
					qPos.add(type);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_TYPE,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of benefit daies where userId = &#63; and type = &#63;.
	 *
	 * @param userId the user ID
	 * @param type the type
	 * @return the number of matching benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUserId_Type(long userId, String type)
		throws SystemException {
		Object[] finderArgs = new Object[] { userId, type };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID_TYPE,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_BENEFITDAY_WHERE);

			query.append(_FINDER_COLUMN_USERID_TYPE_USERID_2);

			if (type == null) {
				query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_1);
			}
			else {
				if (type.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_3);
				}
				else {
					query.append(_FINDER_COLUMN_USERID_TYPE_TYPE_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (type != null) {
					qPos.add(type);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_TYPE,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of benefit daies.
	 *
	 * @return the number of benefit daies
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_BENEFITDAY);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the benefit day persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.evozon.evoportal.evozonfreedaysallocator.model.BenefitDay")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<BenefitDay>> listenersList = new ArrayList<ModelListener<BenefitDay>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<BenefitDay>)InstanceFactory.newInstance(
							listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(BenefitDayImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = BenefitDayPersistence.class)
	protected BenefitDayPersistence benefitDayPersistence;
	@BeanReference(type = BenefitDayQueuePersistence.class)
	protected BenefitDayQueuePersistence benefitDayQueuePersistence;
	@BeanReference(type = FreeDaysHistoryEntryPersistence.class)
	protected FreeDaysHistoryEntryPersistence freeDaysHistoryEntryPersistence;
	@BeanReference(type = ResourcePersistence.class)
	protected ResourcePersistence resourcePersistence;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private static final String _SQL_SELECT_BENEFITDAY = "SELECT benefitDay FROM BenefitDay benefitDay";
	private static final String _SQL_SELECT_BENEFITDAY_WHERE = "SELECT benefitDay FROM BenefitDay benefitDay WHERE ";
	private static final String _SQL_COUNT_BENEFITDAY = "SELECT COUNT(benefitDay) FROM BenefitDay benefitDay";
	private static final String _SQL_COUNT_BENEFITDAY_WHERE = "SELECT COUNT(benefitDay) FROM BenefitDay benefitDay WHERE ";
	private static final String _FINDER_COLUMN_USERID_USERID_2 = "benefitDay.userId = ?";
	private static final String _FINDER_COLUMN_TYPE_TYPE_1 = "benefitDay.type IS NULL";
	private static final String _FINDER_COLUMN_TYPE_TYPE_2 = "benefitDay.type = ?";
	private static final String _FINDER_COLUMN_TYPE_TYPE_3 = "(benefitDay.type IS NULL OR benefitDay.type = ?)";
	private static final String _FINDER_COLUMN_USERID_TYPE_USERID_2 = "benefitDay.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_TYPE_TYPE_1 = "benefitDay.type IS NULL";
	private static final String _FINDER_COLUMN_USERID_TYPE_TYPE_2 = "benefitDay.type = ?";
	private static final String _FINDER_COLUMN_USERID_TYPE_TYPE_3 = "(benefitDay.type IS NULL OR benefitDay.type = ?)";
	private static final String _ORDER_BY_ENTITY_ALIAS = "benefitDay.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No BenefitDay exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No BenefitDay exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(BenefitDayPersistenceImpl.class);
	private static BenefitDay _nullBenefitDay = new BenefitDayImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<BenefitDay> toCacheModel() {
				return _nullBenefitDayCacheModel;
			}
		};

	private static CacheModel<BenefitDay> _nullBenefitDayCacheModel = new CacheModel<BenefitDay>() {
			public BenefitDay toEntityModel() {
				return _nullBenefitDay;
			}
		};
}