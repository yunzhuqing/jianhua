package com.platform.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.platform.dao.BaseDao;
import com.platform.entity.ShopEntity;
import com.platform.service.AbstractDefaultService;
import com.platform.service.ShopService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("shopService")
public class ShopServiceImpl extends AbstractDefaultService<ShopEntity> implements ShopService {

	@Resource(name = "shopDao")
	@Override
	public void setBaseDao(BaseDao<ShopEntity> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	public List<ShopEntity> getShops(long userId, int pageNo, int pageSize) {
		Map<String, Object> attrs = new HashMap<String, Object>();
		int start = (pageNo <= 0) ? 0 : (pageNo - 1) * pageSize;
		attrs.put(FIELD_USER_ID, userId);
        attrs.put(FIELD_OFFSET, start);
        attrs.put(FIELD_LIMIT, pageSize);
		return baseDao.queryList(attrs);
	}

	@Override
	public int size(long userId) {
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put(FIELD_USER_ID, userId);
		return baseDao.queryTotal(attrs);
	}
}
