package saul.orc.app.orc.image;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saul.orc.app.orc.entity.ReturnImg;
import saul.orc.app.orc.config.OrcPropertiesCfg;
import saul.orc.app.orc.util.Result;
import saul.orc.app.orc.util.ResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取网络图片,并且进行识别
 */
@Component
@Slf4j
public class UrlImageFinder {
    @Autowired
    private OrcPropertiesCfg cfg;


    /**
     * 识别网络图片
     *
     * @param url 图片地址
     * @return 返回值
     */
    public ReturnImg resultURL(String url) {
        ReturnImg returnImg = null;
        try {
            String data = "";
            data = client().basicGeneral(url, options()).toString(2);
            returnImg = JSON.parseObject(data, ReturnImg.class);
            //handle the error message
            Result errorCode = isError(returnImg);
            if (errorCode.getCode().intValue() != ResultCode.SUCCESS.code()) {
                // TODO: 19/08/2018 文件错误处理
            } else {
                StringBuffer buffer = new StringBuffer();
                for (ReturnImg.WordsResultBean next : returnImg.getWords_result()) {
                    buffer.append(StringUtils.trim(next.getWords()));
                }
                ReturnImg r = ReturnImg.builder().build();
                r.setReturnstr(buffer.toString());
                return r;
            }
        } catch (Exception e) {
            throw e;
        }
        return returnImg;
    }

    /**
     * 获取本地图片文件
     *
     * @param file
     * @return
     */
    public String resultLocal(String file) {
        return this.client().basicGeneral(file, this.options()).toString(2);
    }

    public String getTableRes3(String file) {
        JSONObject jsonres = client().tableRecognizeToExcelUrl(file, 20000);
        System.out.println(jsonres.toString(2));
        return null;
    }

    /**
     * 初始化识别客户端
     *
     * @return client
     */
    private AipOcr client() {
        return new AipOcr(cfg.getAppid(), cfg.getApikey(), cfg.getSecret());
    }

    /**
     * 初始化配置参数
     *
     * @return options
     */
    private HashMap<String, String> options() {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", cfg.getLanguage_type());//
        options.put("detect_direction", cfg.getDetect_direction());
        options.put("detect_language", cfg.getDetect_language());
        options.put("probability", cfg.getProbability());
        return options;
    }

    /**
     * 处理错误
     *
     * @param returnImg
     * @return
     */
    private Result isError(ReturnImg returnImg) {
        if (StringUtils.isNotEmpty(returnImg.getError_code())) {
            for (Map<String, String> next : cfg.getError()) {
                String code = next.keySet().iterator().next().toString();
                if (StringUtils.equalsIgnoreCase(code, returnImg.getError_code())) {
                    return Result.failure(ResultCode.SYSTEM_INNER_ERROR, next);
                }
            }
        }
        return Result.success();
    }
}
