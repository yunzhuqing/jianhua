package com.platform.controller;

import com.platform.constants.SuggestItemType;
import com.platform.entity.GoodsEntity;
import com.platform.entity.SceneEntity;
import com.platform.entity.SuggestEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.*;
import com.platform.utils.PageUtils;
import com.platform.utils.R;
import com.platform.vo.SceneInnerVO;
import com.platform.vo.SceneVO;
import com.platform.vo.SuggestItemVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
    SysUserService sysUserService;

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
                items.add(sceneVO);
            });
        }
        PageUtils pageUtil = new PageUtils(items, items.size(), 100, 1);
        return  R.ok().put("page", pageUtil);
    }

    @GetMapping("catalog")
    public R catalog() {
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
                items.add(sceneVO);
            });
        }
        R r = R.ok();
        r.put("scenes", scenes);
        r.put("current", scenes.get(0));
        return r;
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
        r.put("scenes", scenes);
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
}
