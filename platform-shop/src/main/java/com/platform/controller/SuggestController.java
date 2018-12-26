package com.platform.controller;

import com.platform.annotation.IgnoreAuth;
import com.platform.constants.SuggestItemType;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.utils.R;
import com.platform.vo.SuggestItemInnerVO;
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
@RequestMapping(value = {"/api/suggest", "/suggest"})
public class SuggestController extends AbstractController {

    @Autowired
    private SuggestService suggestService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TopicService topicService;

    @GetMapping("list")
    public R list(@RequestParam(name="scene", defaultValue = "0", required = false) Integer scene) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("scene", scene);
        attrs.put(AbstractService.FIELD_SIDX, "order");
        attrs.put(AbstractService.FIELD_ORDER, "asc");
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
            } else if (SuggestItemType.ACTIVITY.ordinal() == item.getType()) {
                TopicEntity topicEntity = topicService.queryObject(item.getRid());
                if(null != topicEntity) {
                    suggestItemVO.setItemFirst(topicEntity.getTitle());
                    suggestItemVO.setUrl(topicEntity.getItemPicUrl());
                    suggestItemVO.setRid(topicEntity.getId().longValue());
                }
            }
            items.add(suggestItemVO);
        });

        return  R.ok().put("items", items);
    }

    @GetMapping("innerList")
    public R innerList(@RequestParam(name="scene", defaultValue = "0", required = false) Integer scene) {
        Map<String, Object> attrs = new HashMap<>();
        if(0 != scene) {
            attrs.put("scene", scene);
        }

        List<SuggestEntity> suggests = suggestService.queryList(attrs);
        List<SuggestItemInnerVO> items = new LinkedList<>();
        suggests.forEach(item -> {
            SuggestItemInnerVO suggestItemInnerVO = new SuggestItemInnerVO();
            suggestItemInnerVO.setId(item.getId());
            suggestItemInnerVO.setType(item.getType());
            suggestItemInnerVO.setOrder(item.getOrder());
            suggestItemInnerVO.setScene(item.getScene());
            suggestItemInnerVO.setCt(item.getCreateTime().getTime());
            if(SuggestItemType.GOODS.ordinal() == item.getType()) {
                GoodsEntity goodsEntity = goodsService.queryObject(item.getRid());
                SysUserEntity user = sysUserService.queryObject(item.getUserId());
                if(null != goodsEntity) {
                    suggestItemInnerVO.setItemFirst(goodsEntity.getName());
                    suggestItemInnerVO.setUrl(goodsEntity.getPrimaryPicUrl());
                    suggestItemInnerVO.setItemSecond(String.valueOf(goodsEntity.getMarketPrice()));
                    suggestItemInnerVO.setUserName(user.getNickname());
                    suggestItemInnerVO.setRid(goodsEntity.getId().longValue());
                }
            } else if (SuggestItemType.ACTIVITY.ordinal() == item.getType()){
                TopicEntity topicEntity = topicService.queryObject(item.getRid());
                if(null != topicEntity) {
                    suggestItemInnerVO.setItemFirst(topicEntity.getTitle());
                    suggestItemInnerVO.setUrl(topicEntity.getItemPicUrl());
                    suggestItemInnerVO.setRid(topicEntity.getId().longValue());
                }
            }
            items.add(suggestItemInnerVO);
        });

        return  R.ok().put("list", items);
    }

    @PostMapping("/save")
    public R save(@RequestBody SuggestEntity suggestEntity) {
        suggestEntity.setUserId(getUserId());
        if(suggestService.save(suggestEntity) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/update")
    public R update(@RequestBody SuggestEntity suggestEntity) {
        if(suggestService.update(suggestEntity) > 0) {
            return R.ok();
        }
        return R.error();
    }


    @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        suggestService.delete(id);
        return R.ok();
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        return R.ok().put("info", suggestService.queryObject(id));
    }
}
