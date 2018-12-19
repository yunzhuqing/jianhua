    package com.platform.utils;

    import org.apache.log4j.Logger;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.client.RestTemplate;

    import java.util.Map;


    /**
     * 作者: @author Harmon <br>
     * 时间: 2016-09-01 09:18<br>
     * 描述: Http请求通用工具 <br>
     */
    public class HttpUtil {

        // 日志
        private static final Logger logger = Logger.getLogger(HttpUtil.class);

        public static  <T> T get(String url, Class<T> responseClass, Map<String, ?> params, String...headers) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            int len = headers.length / 2;
            for(int i=0; i < len; i++) {
                int start = i * 2 + 1;
                requestHeaders.add(headers[start], headers[start + 1]);
            }
            HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseClass, params);
            return response.getBody();
        }
    }
