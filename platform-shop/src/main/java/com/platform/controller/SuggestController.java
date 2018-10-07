package com.platform.controller;

import com.platform.constants.SuggestItemType;
import com.platform.entity.GoodsEntity;
import com.platform.entity.ShopEntity;
import com.platform.entity.SuggestEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsService;
import com.platform.service.ShopService;
import com.platform.service.SuggestService;
import com.platform.service.SysUserService;
import com.platform.utils.R;
import com.platform.vo.SuggestItemVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Api(tags = "首页推荐接口")
@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @Autowired
    private SuggestService suggestService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("list")
    public R list(@RequestParam(name="scene", defaultValue = "0", required = false) Integer scene) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("scene", scene);
        List<SuggestEntity> suggests = suggestService.queryList(attrs);
        List<SuggestItemVO> items = new LinkedList<>();
        suggests.forEach(item -> {
            SuggestItemVO suggestItemVO = new SuggestItemVO();
            suggestItemVO.setId(item.getId());
            suggestItemVO.setType(item.getType());
            if(SuggestItemType.GOODS.ordinal() == item.getType()) {
                GoodsEntity goodsEntity = goodsService.queryObject(item.getRid());
                SysUserEntity user = sysUserService.queryObject(item.getUserId());
                if(null != goodsEntity) {
                    suggestItemVO.setItemFirst(goodsEntity.getName());
                    suggestItemVO.setUrl(goodsEntity.getPrimaryPicUrl());
                    suggestItemVO.setItemSecond(String.valueOf(goodsEntity.getMarketPrice()));
                    suggestItemVO.setUserName(user.getNickname());
                    suggestItemVO.setRid((long)goodsEntity.getId().intValue());
                }
                items.add(suggestItemVO);
            }
        });

        return  R.ok().put("items", items);
    }
}
