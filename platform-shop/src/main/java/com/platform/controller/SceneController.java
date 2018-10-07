package com.platform.controller;

import com.platform.annotation.IgnoreAuth;
import com.platform.constants.SuggestItemType;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.utils.PageUtils;
import com.platform.utils.R;
import com.platform.vo.SceneInnerVO;
import com.platform.vo.SceneVO;
import com.platform.vo.SuggestItemVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Api(tags = "场景接口")
@RestController
@RequestMapping(value = {"/api/scene", "/scene"})
public class SceneController extends AbstractController {

    @Autowired
    SceneService sceneService;

    @Autowired
    SceneGroupService sceneGroupService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    SysUserService sysUserService;

    @IgnoreAuth
    @GetMapping("suggest")
    public R suggest() {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(SceneService.FIELD_OFFSET, 0);
        attrs.put(SceneService.FIELD_LIMIT, 4);
        List<SceneEntity> scenes = sceneService.queryList(attrs);
        List<SceneVO> items = new LinkedList<>();

        if(!CollectionUtils.isEmpty(scenes)) {
            scenes.forEach(item -> {
                SceneVO sceneVO = new SceneVO();
                sceneVO.setId(item.getId());
                sceneVO.setName(item.getName());
                items.add(sceneVO);
            });
        }
        return  R.ok().put("items", items);
    }

    @IgnoreAuth
    @GetMapping("list")
    public R list() {
        Map<String, Object> attrs = new HashMap<>();
        List<SceneEntity> scenes = sceneService.queryList(attrs);
        List<SceneInnerVO> items = new LinkedList<>();

        if(!CollectionUtils.isEmpty(scenes)) {
            scenes.forEach(item -> {
                SceneInnerVO sceneVO = new SceneInnerVO();
                sceneVO.setId(item.getId());
                sceneVO.setName(item.getName());
                sceneVO.setUserName(sysUserService.queryObject(item.getUserId()).getNickname());
                sceneVO.setCt(item.getCreateTime());
                sceneVO.setParentId(item.getParentId());
                items.add(sceneVO);
            });
        }

        return  R.ok().put("list", items);
    }

    @IgnoreAuth
    @GetMapping("subcatalog/{id}")
    public R subCatalog(@PathVariable("id") Long id) {
        Map<String, Object> attrs = new HashMap<>();
        List<SceneEntity> scenes = null;
        if(null == id) {
            attrs.put("parent_id", 0);
        } else {
            attrs.put("parent_id", id);
        }
        scenes = sceneService.queryList(attrs);
        List<SceneVO> items = new LinkedList<>();

        if(!CollectionUtils.isEmpty(scenes)) {
            scenes.forEach(item -> {
                SceneVO sceneVO = new SceneVO();
                sceneVO.setId(item.getId());
                sceneVO.setName(item.getName());

                List<SuggestItemVO> sceneItems = new LinkedList<>();
                if(0 != id) {
                    List<SceneGroupEntity> sceneGroups = sceneGroupService.list(item.getId(), 0 , 20);
                    sceneGroups.forEach(sceneGroup -> {
                        SuggestItemVO suggestItem = new SuggestItemVO();
                        suggestItem.setId(sceneGroup.getId());
                        suggestItem.setType(sceneGroup.getType());
                        if(SuggestItemType.GOODS.ordinal() == sceneGroup.getType()) {
                            GoodsEntity goodsEntity = goodsService.queryObject(sceneGroup.getRid());
                            suggestItem.setItemFirst(goodsEntity.getName());
                            suggestItem.setItemSecond(String.valueOf(goodsEntity.getMarketPrice()));
                            suggestItem.setUrl(goodsEntity.getPrimaryPicUrl());
                            suggestItem.setRid((long)goodsEntity.getId().intValue());
                        } else {

                        }
                        sceneItems.add(suggestItem);
                    });
                }
                sceneVO.setSuggestItems(sceneItems);
                items.add(sceneVO);
            });
        }


        R r = R.ok();
        r.put("scenes", items);
        if(items.size() > 0 && id == 0) {
            r.put("current", scenes.get(0));
        }
        return r;
    }


    @IgnoreAuth
    @GetMapping("catalog/{id}")
    public R catalog(@PathVariable("id") Long id) {
        SceneEntity current = sceneService.queryObject(id);
        return R.ok().put("current", current);
    }


    @GetMapping("getParent")
    public R getParent() {
        List<SceneEntity> scenes = sceneService.queryParent();
        List<SceneInnerVO> items = new LinkedList<>();

        if(!CollectionUtils.isEmpty(scenes)) {
            scenes.forEach(item -> {
                SceneInnerVO sceneVO = new SceneInnerVO();
                sceneVO.setId(item.getId());
                sceneVO.setName(item.getName());
                sceneVO.setUserName(sysUserService.queryObject(item.getUserId()).getNickname());
                sceneVO.setCt(item.getCreateTime());
                items.add(sceneVO);
            });
        }

        R r = R.ok();
        SceneInnerVO sceneVO = new SceneInnerVO();
        sceneVO.setId(0L);
        sceneVO.setName("请选择");
        items.add(0, sceneVO);
        r.put("scenes", items);
        return r;
    }

    /**
     * 新建场景
     * @param scene
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestBody SceneEntity scene) {
        scene.setUserId(getUserId());
        scene.setOrder(0);
        if(sceneService.save(scene) > 0) {
            return R.ok();
        } else {
            return R.error("保存失败");
        }
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SceneEntity sceneEntity = sceneService.queryObject(id);
        if(null == sceneEntity) {
            return R.error("未找到对应的场景");
        }
        return R.ok().put("scene", sceneEntity);
    }

    /**
     * 新建场景
     * @param scene
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestBody SceneEntity scene) {
        if(sceneService.update(scene) > 0) {
            return R.ok();
        } else {
            return R.error("保存失败");
        }
    }

    @RequestMapping("delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        if(null == id) {
            return R.error("传入的ID 为空");
        }

        if(sceneService.delete(id) > 0) {
            return R.ok();
        }
        return R.error();
    }
}
