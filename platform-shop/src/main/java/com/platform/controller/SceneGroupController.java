package com.platform.controller;

import com.platform.constants.SceneTypeEnum;
import com.platform.entity.GoodsEntity;
import com.platform.entity.SceneGroupEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsService;
import com.platform.service.SceneGroupService;
import com.platform.service.SysUserService;
import com.platform.utils.PageUtils;
import com.platform.utils.R;
import com.platform.vo.SceneGroupInnerVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "场景接口")
@RestController
@RequestMapping(value = {"/api/scenegroup", "/scenegroup"})
public class SceneGroupController extends AbstractController {

    @Autowired
    private SceneGroupService sceneGroupService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public R list(@RequestParam(value = "sceneId", defaultValue = "0") Long sceneId,
                  @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<SceneGroupInnerVO> innerSceneGroups = new ArrayList<>();
        List<SceneGroupEntity> sceneGroups = sceneGroupService.list(sceneId, pageNo, pageSize);
        sceneGroups.forEach(sceneGroup -> {
            innerSceneGroups.add(convertToInner(sceneGroup));
        });
        int size = sceneGroupService.size(sceneId);
        PageUtils pageUtil = new PageUtils(innerSceneGroups, size, pageSize, pageNo);
        return  R.ok().put("page", pageUtil);
    }

    @PostMapping("/save")
    public R save(@RequestBody SceneGroupEntity sceneGroupEntity) {
        sceneGroupEntity.setFrom(0);
        sceneGroupEntity.setUserId(getUserId());
        if(sceneGroupService.save(sceneGroupEntity) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable(value = "id") Long id) {
        if(null == id) {
            return R.error("请求参数为空");
        }
        SceneGroupEntity sceneGroupEntity = sceneGroupService.queryObject(id);
        R r = R.ok().put("sceneGroup", sceneGroupEntity);
        return r;
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        if(null == id) {
            return R.error();
        }
        sceneGroupService.delete(id);
        return R.ok();
    }

    private SceneGroupInnerVO convertToInner(SceneGroupEntity sceneGroup) {
        SceneGroupInnerVO innerSceneGroup = new SceneGroupInnerVO();
        innerSceneGroup.setId(sceneGroup.getId());
        innerSceneGroup.setSid(sceneGroup.getSid());
        innerSceneGroup.setType(sceneGroup.getType());
        innerSceneGroup.setRid(sceneGroup.getRid());
        if(SceneTypeEnum.GOODS.ordinal() == sceneGroup.getType()) {
            GoodsEntity goodsEntity = goodsService.queryObject(sceneGroup.getRid());
            if(null != goodsEntity) {
                innerSceneGroup.setName(goodsEntity.getName());
                innerSceneGroup.setPictureUrl(goodsEntity.getPrimaryPicUrl());
            }
        } else {

        }
        SysUserEntity sysUserEntity = sysUserService.queryObject(sceneGroup.getUserId());
        if(null != sysUserEntity) {
            innerSceneGroup.setUserName(sysUserEntity.getNickname());
        }
        innerSceneGroup.setUserId(sceneGroup.getUserId());
        innerSceneGroup.setFrom(sceneGroup.getFrom());
        innerSceneGroup.setLocation(sceneGroup.getLocation());
        innerSceneGroup.setCreateTime(sceneGroup.getCreateTime());
        innerSceneGroup.setUpdateTime(sceneGroup.getUpdateTime());
        return innerSceneGroup;
    }
}
