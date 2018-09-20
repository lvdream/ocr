package saul.orc.app.orc.image;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import saul.orc.app.orc.config.TranslateCfg;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MpImageCodeHandle {
    @Autowired
    private TranslateCfg cfg;

    /**
     * 获取token信息
     *
     * @return
     */
    public String getToken() {
        Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("grant_type", "client_credential");
        uriVariables.put("appid", cfg.getAppid());
        uriVariables.put("secret", cfg.getAppsec());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> resp = restTemplate.getForEntity("https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}", Map.class, uriVariables);
        if (resp != null) {
            return resp.getBody().get("access_token").toString();
        }
        return null;
    }

    public void getQRimg(String ACCESS_TOKEN) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> dmap = new HashMap<>();
        dmap.put("page", "pages/index/index");
        dmap.put("scene", "14");
        HttpEntity<String> requentity = new HttpEntity<String>(JSON.toJSONString(dmap), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> entity = restTemplate.exchange("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + ACCESS_TOKEN, HttpMethod.POST, requentity, byte[].class);
        byte[] result = entity.getBody();
        inputStream = new ByteArrayInputStream(result);
        File targetFile = new File("/Users/Saul/Desktop");
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream("/Users/Saul/Desktop/1.png");

        byte[] buffer = new byte[8192];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        out.flush();
        out.close();
    }
}
