package saul.orc.app.orc.image;

import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saul.orc.app.orc.config.HWImageCfg;
import saul.orc.app.orc.config.OrcFileCfg;

import java.io.File;

@Component
@Slf4j
public class HWImageHandle {
    @Autowired
    private OrcFileCfg orcFileCfg;
    @Autowired
    private HWImageCfg hwImageCfg;

    /**
     * 初始化连接
     *
     * @return AisAccess
     */
    private AisAccess init() {
        return new AisAccess(new AuthInfo(hwImageCfg.getEndpoint(),
                hwImageCfg.getRegion(),  /* OCR服务的区域信息, 可以在上面的地址中查询 */
                hwImageCfg.getAk(),    /* 请输入你的AK信息 */
                hwImageCfg.getSk()));
    }

    /**
     * 解析主方法
     *
     * @param file
     */
    public void doOpeation(String file) {
        AisAccess aisAccess = init();
        try {
            byte[] fileData = FileUtils.readFileToByteArray(new File(file));
            String fileBase64Str = Base64.encodeBase64String(fileData);
            JSONObject json = new JSONObject();
            json.put("image", fileBase64Str);
            StringEntity stringEntity = new StringEntity(json.toString(), "utf-8");
            HttpResponse response = aisAccess.post(hwImageCfg.getUri(), stringEntity);
            String result = HttpClientUtils.convertStreamToString(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
