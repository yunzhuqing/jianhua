package com.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.OrderEntity;
import com.platform.service.OrderService;
import com.platform.utils.HttpUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/logistics", "/logistics"})
public class LogisticsController extends AbstractController {

    private static  Map<Integer, String> logistics = new HashMap<>();

    private RestTemplate restTemplate;

    static {
        logistics.put(15, "ems");
        logistics.put(74, "yunda");
        logistics.put(60, "shentong");
        logistics.put(80, "yuantong");
        logistics.put(88, "zhongtong");
        logistics.put(88, "zhongtong");
        logistics.put(29, "baishi");
        logistics.put(56, "shunfeng");
        logistics.put(59, "sutong");

    }

    @Autowired
    private OrderService orderService;

    @IgnoreAuth
    @RequestMapping("/info/{oid}")
    public JSONObject info(@PathVariable("oid") String oid) {
        JSONObject res = new JSONObject();
        JSONArray arr = new JSONArray();
        if(NumberUtils.isNumber(oid)) {
            OrderEntity orderEntity = orderService.queryObject(Integer.parseInt(oid));
            String com = logistics.get(orderEntity.getShippingId());
            res.put("errno", 0);
            res.put("data", getLogistics(orderEntity.getShippingNo(), com));
            return res;
        }
        res.put("errno", 500);
        return res;
    }


    public JSONArray getLogistics(String nu, String com) {
        try {
            String url = "https://sp0.baidu.com/9_Q4sjW91Qh3otqbppnN2DJv/pae/channel/data/asyncqury?appid=4001&com=" + com + "&nu=" + nu;
            String uuid = UUID.randomUUID().toString();
            String resp = HttpUtil.get(url, String.class, null, "Cookie", "BAIDUID=" + uuid +":FG=1");
            JSONObject json = JSONObject.parseObject(resp, JSONObject.class);
            if("0".equals(json.getString("status"))) {
                JSONObject datas  = json.getJSONObject("data");
                if(null != datas) {
                    JSONObject infoJSON = datas.getJSONObject("info");
                    if(null != infoJSON) {
                        JSONArray contextArr = infoJSON.getJSONArray("context");
                        return contextArr;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
