package com.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.OrderEntity;
import com.platform.service.OrderService;
import com.platform.utils.HttpUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("logistics")
public class LogisticsController extends AbstractController {

    private static  Map<Integer, String> logistics = new HashMap<>();

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
    public JSONArray info(@PathVariable("oid") String oid) {
        JSONArray arr = new JSONArray();
        if(NumberUtils.isNumber(oid)) {
            OrderEntity orderEntity = orderService.queryObject(Integer.parseInt(oid));
            String com = logistics.get(orderEntity.getShippingId());
            return getLogistics(orderEntity.getShippingNo(), com);
        }
        return arr;
    }


    public JSONArray getLogistics(String nu, String com) {
        try {
            String url = "https://sp0.baidu.com/9_Q4sjW91Qh3otqbppnN2DJv/pae/channel/data/asyncqury?appid=4001&com=" + com + "&nu=" + nu;
            Map<String, String> header = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            header.put("Cookie", "BAIDUID=" + uuid +":FG=1");
            String resp = HttpUtil.URLGet(url, null, null, header);
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
