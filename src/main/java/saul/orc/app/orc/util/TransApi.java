package saul.orc.app.orc.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import saul.orc.app.orc.config.TranslateCfg;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Component
public class TransApi {
    @Autowired
    private TranslateCfg translateCfg;

    /**
     * @param query 查询字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getTransResult(String query) throws UnsupportedEncodingException {
        MultiValueMap<String, Object> params = buildParams(query, "auto", "auto");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> resp = restTemplate.postForEntity(translateCfg.getUrl(), params, Map.class);
        if (resp.getStatusCode() == HttpStatus.OK && MapUtils.isNotEmpty(resp.getBody())) {
            Map reMap = resp.getBody();
            if (MapUtils.getObject(reMap, "trans_result") != null) {
                List dList = (List) MapUtils.getObject(reMap, "trans_result");
                if (CollectionUtils.isNotEmpty(dList)) {
                    Map sMap = (Map) dList.get(0);
                    if (MapUtils.isNotEmpty(sMap)) {
                        if (MapUtils.getString(sMap, "dst") != null) {
                            return MapUtils.getString(sMap, "dst");
                        }
                    }
                }
            }
        }
        return null;
    }

    private MultiValueMap<String, Object> buildParams(String query, String from, String to) throws UnsupportedEncodingException {
        MultiValueMap<String, Object> pushObject = new LinkedMultiValueMap();
        pushObject.add("q", query);
        pushObject.add("from", from);
        pushObject.add("to", to);

        pushObject.add("appid", translateCfg.getAppid());

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        pushObject.add("salt", salt);

        // 签名
        String src = translateCfg.getAppid() + query + salt + translateCfg.getAppsec(); // 加密前的原文
        pushObject.add("sign", MD5.md5(src));

        return pushObject;
    }

}
