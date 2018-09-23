package com.platform.service.impl;


import com.platform.constants.SysUserTypeEnum;
import com.platform.dao.BaseDao;
import com.platform.entity.ShopEntity;
import com.platform.entity.ShopUserEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.AbstractDefaultService;
import com.platform.service.ShopService;
import com.platform.service.ShopUserService;
import com.platform.service.SysUserService;
import com.platform.vo.ShopUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("shopUserService")
public class ShopUserServiceImpl extends AbstractDefaultService<ShopUserEntity> implements ShopUserService {

	@Autowired
	private ShopService shopService;

	@Autowired
	private SysUserService sysUserService;

	@Resource(name = "shopUserDao")
	@Override
	public void setBaseDao(BaseDao<ShopUserEntity> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	public List<ShopUserVO> getUsers(Long userId, int pageNo, int pageSize) {
		if(pageNo > 0) {
			pageNo = pageNo - 1;
		} else {
			pageNo = 0;
		}
		List<ShopEntity> shops = shopService.getShops(userId, 0 , MAX_SHOPS);
		List<Integer> shopIds = new LinkedList<>();
		shops.forEach(shopEntity -> shopIds.add(shopEntity.getId()));

		Map<String, Object> attrs = new HashMap<>();
		attrs.put(FIELD_SHOP_ID_LIST, shopIds);
		attrs.put(FIELD_OFFSET, pageNo * pageSize);
		attrs.put(FIELD_LIMIT, pageSize);
		List<ShopUserEntity> shopusersEntities = baseDao.queryList(attrs);

		List<ShopUserVO> shopUsers = new LinkedList<>();
		shopusersEntities.forEach(shopUserEntity -> {
			SysUserEntity sysUserEntity = sysUserService.queryObject(shopUserEntity.getUserId());
			shopUsers.add(convertToShopUser(shopUserEntity.getId(), sysUserEntity));
		});
		return shopUsers;
	}


	@Override
	public long size(Long userId) {
		Map<String, Object> attrs = new HashMap<>();
		attrs.put(FIELD_USER_ID, userId);
		return baseDao.queryTotal(attrs);
	}

	@Transactional
	@Override
	public int saveUser(ShopUserVO shopUser) {
		SysUserEntity sysUser = convertToSysUser(shopUser);
		sysUserService.save(sysUser);

		ShopUserEntity shopUserEntity = new ShopUserEntity();

		if(null != shopUser.getShopId()) {
			shopUserEntity.setShopId(shopUser.getShopId());
		} else {
			List<ShopEntity> shops = shopService.getShops(sysUser.getCreateUserId(), 0, 1);
			if(shops.size() > 0) {
				shopUserEntity.setShopId((long)shops.get(0).getId());
			}
		}
		shopUserEntity.setUserId(sysUser.getUserId());
		save(shopUserEntity);
		return 0;
	}

	@Override
	public int updateUser(ShopUserVO shopUser) {
		return 0;
	}

	@Override
	public int removeUser(Long userId) {
		return 0;
	}


	private ShopUserVO convertToShopUser(long id, SysUserEntity sysUserEntity) {
		ShopUserVO shopUser = new ShopUserVO();
		shopUser.setId(id);
		shopUser.setAvatar(sysUserEntity.getAvatar());
		shopUser.setBirthday(sysUserEntity.getBirthday());
		shopUser.setGender(sysUserEntity.getGender());
		shopUser.setLastLoginTime(sysUserEntity.getLastLoginTime());
		shopUser.setMobile(sysUserEntity.getMobile());
		shopUser.setNickname(sysUserEntity.getNickname());
		shopUser.setRegisterTime(sysUserEntity.getCreateTime());
		shopUser.setUserLevelId(sysUserEntity.getUserLevelId());
		shopUser.setUsername(sysUserEntity.getUsername());
		shopUser.setRegisterIp(sysUserEntity.getRegisterIp());
		shopUser.setOpenId(sysUserEntity.getOpenId());
		shopUser.setOpenType(sysUserEntity.getOpenType());
		return shopUser;
	}

	private SysUserEntity convertToSysUser(ShopUserVO shopUserVO) {
		SysUserEntity sysUser = new SysUserEntity();
		sysUser.setUserType(SysUserTypeEnum.SYS.ordinal());
		sysUser.setUsername(shopUserVO.getUsername());
		sysUser.setNickname(shopUserVO.getNickname());
		sysUser.setGender(shopUserVO.getGender());
		sysUser.setBirthday(shopUserVO.getBirthday());
		sysUser.setUserLevelId(shopUserVO.getUserLevelId());
		sysUser.setMobile(shopUserVO.getMobile());
		sysUser.setCreateUserId(shopUserVO.getCreateUserId());
		return sysUser;
	}
}
