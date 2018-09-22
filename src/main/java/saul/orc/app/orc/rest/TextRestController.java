package saul.orc.app.orc.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import saul.orc.app.orc.util.Result;
import saul.orc.app.orc.util.ResultCode;
import saul.orc.app.orc.util.TransApi;

import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
public class TextRestController {
    @Autowired
    private TransApi api;

    /**
     * 文字翻译
     *
     * @param qStr 翻译字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    @PostMapping(value = "/textTranslate")
    public Result translate(String qStr) throws UnsupportedEncodingException {
        if (StringUtils.isNotEmpty(qStr)) {
            try {
                if (StringUtils.containsAny(qStr, StringUtils.LF)) {
                    String[] aStr = StringUtils.split(qStr,StringUtils.LF);
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < aStr.length; i++) {
                        String s = aStr[i];
                        stringBuffer.append(api.getTransResult(aStr[i])) ;
                        stringBuffer.append(StringUtils.LF) ;
                    }
                    return  Result.success(stringBuffer.toString());
                } else {
                    String rStr = api.getTransResult(qStr);
                    if (StringUtils.isNotEmpty(rStr)) {
                        return Result.success(rStr);
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return Result.failure(ResultCode.DATA_IS_WRONG);
    }
}
