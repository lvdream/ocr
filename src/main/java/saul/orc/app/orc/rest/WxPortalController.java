package saul.orc.app.orc.rest;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import saul.orc.app.orc.config.WxMaConfiguration;
import saul.orc.app.orc.entity.MstbUserEntity;
import saul.orc.app.orc.entity.TstbUserContentEntity;
import saul.orc.app.orc.repos.UserContentRepos;
import saul.orc.app.orc.repos.UserRepos;

import java.util.Date;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/wx/portal/{appid}")
public class WxPortalController {
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private UserContentRepos contentRepos;
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
        String str = "您的留言,已经收到,谢谢您的支持!";
        MstbUserEntity userEntity = new MstbUserEntity();

        userEntity.setMuCode(WxMaMessage.fromJson(requestBody).getFromUser());
        userEntity.setMuCreatetime(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        userEntity.setMuUpdatetime(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        userRepos.save(userEntity);
        TstbUserContentEntity tstbUserContentEntity = new TstbUserContentEntity();
        tstbUserContentEntity.setMuId(userEntity.getMuId());
        tstbUserContentEntity.setTucContent(WxMaMessage.fromJson(requestBody).getContent());
        tstbUserContentEntity.setTucReply(str);
        tstbUserContentEntity.setTucCreatetime(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        contentRepos.save(tstbUserContentEntity);
        // 明文传输的消息
        WxMaMessage inMessage;
        if (isJson) {
//            inMessage = WxMaMessage.fromJson(requestBody);
            inMessage = WxMaMessage.fromJson(str);
        } else {//xml
//            inMessage = WxMaMessage.fromXml(requestBody);
            inMessage = WxMaMessage.fromXml(str);
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
