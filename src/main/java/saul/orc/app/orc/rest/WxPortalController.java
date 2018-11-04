package saul.orc.app.orc.rest;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import saul.orc.app.orc.config.WxMaConfiguration;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/wx/portal/{appid}")
public class WxPortalController {
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
                signature, timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @PostMapping
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("openid") String openid,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        log.info("\n接收微信请求：[openid=[{}], ], signature=[{}]," +
                        " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, timestamp, nonce, requestBody);

        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
        }

        final boolean isJson = Objects.equals(wxService.getWxMaConfig().getMsgDataFormat(),
                WxMaConstants.MsgDataFormat.JSON);
        // 明文传输的消息
        WxMaMessage inMessage;
        if (isJson) {
            inMessage = WxMaMessage.fromJson(requestBody);
        } else {//xml
            inMessage = WxMaMessage.fromXml(requestBody);
        }

        this.route(inMessage, appid);
        return "success";
    }


    private void route(WxMaMessage message, String appid) {
        try {
            WxMaConfiguration.getRouters().get(appid).route(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
