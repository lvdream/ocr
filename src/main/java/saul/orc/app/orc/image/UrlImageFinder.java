package saul.orc.app.orc.image;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saul.orc.app.orc.config.OrcFileCfg;
import saul.orc.app.orc.config.OrcPropertiesCfg;
import saul.orc.app.orc.entity.RemoteFileResult;
import saul.orc.app.orc.entity.ReturnImg;
import saul.orc.app.orc.util.DownloadFileUtil;
import saul.orc.app.orc.util.ExcelUtil;
import saul.orc.app.orc.util.Result;
import saul.orc.app.orc.util.ResultCode;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 获取网络图片,并且进行识别
 */
@Component
@Slf4j
public class UrlImageFinder {
    @Autowired
    private OrcPropertiesCfg cfg;
    @Autowired
    private OrcFileCfg orcFileCfg;
    @Autowired
    private DownloadFileUtil downloadFileUtil;
    @Autowired
    private ExcelUtil excelUtil;


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
//                    buffer.append(StringUtils.trim(next.getWords()));
                    buffer.append(next.getWords());
                    buffer.append(StringUtils.LF);
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

    /**
     * 处理表格文件识别
     *
     * @param file 本地表格文件
     * @return
     */
    public ReturnImg getTableRes(String file) {
        ReturnImg returnImg = null;
        try {
            //调用接口进行文件中的文本识别
            JSONObject jsonres = client().tableRecognizeToExcelUrl(file, 20000);
            RemoteFileResult remoteFileResult = JSON.parseObject(jsonres.toString(2), RemoteFileResult.class);
            if (remoteFileResult != null && Strings.isNotEmpty(remoteFileResult.getError_code())) {
                returnImg = ReturnImg.builder().build();
                returnImg.setError_msg(remoteFileResult.getError_msg());
                returnImg.setError_code(remoteFileResult.getError_code());
                return returnImg;
            }
            //判断是否识别完成
            if (remoteFileResult != null && null == remoteFileResult.getError_code() &&
                    Strings.isNotEmpty(remoteFileResult.getResult().getResult_data())) {
                //下载文件到本地
                String fileName = UUID.randomUUID().toString().replaceAll("-", "");
                String fileNameDW = downloadFileUtil.downRemoteFile(remoteFileResult.getResult().getResult_data(), fileName + ExcelTypeEnum.XLS.getValue(), orcFileCfg.getPath());
                //解析Excel,提取Body-Sheet里面数据,并生成新的Excel
                List<List<String>> lists = excelUtil.readExcel(fileNameDW, orcFileCfg.getKeepsheet());
                //生成新的Excel
                String newFile = orcFileCfg.getPath() + fileName + "_new";
                ReturnImg r = ReturnImg.builder().build();
                excelUtil.writeExcel(lists, newFile + ExcelTypeEnum.XLSX.getValue());
                r.setReturnstr(fileName + "_new" + ExcelTypeEnum.XLSX.getValue());
                return r;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return returnImg;
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
