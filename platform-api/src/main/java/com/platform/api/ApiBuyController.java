package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.LoginUser;
import com.platform.cache.Cache;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.SysUserEntity;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品购买")
@RestController
@RequestMapping("/api/buy")
public class ApiBuyController extends ApiBaseAction {
    @Autowired
    private Cache cache;

    @ApiOperation(value = "商品添加")
    @PostMapping("/add")
    public Object addBuy(@LoginUser SysUserEntity loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer goodsId = jsonParam.getInteger("goodsId");
        Integer productId = jsonParam.getInteger("productId");
        Integer number = jsonParam.getInteger("number");
        BuyGoodsVo goodsVo = new BuyGoodsVo();
        goodsVo.setGoodsId(goodsId);
        goodsVo.setProductId(productId);
        goodsVo.setNumber(number);
        cache.set("goods" + loginUser.getUserId() + "", goodsVo);
        return toResponsMsgSuccess("添加成功");
    }
}
