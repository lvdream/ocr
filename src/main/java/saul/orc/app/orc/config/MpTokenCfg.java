package saul.orc.app.orc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "common.ocr.mp")
@Data
public class MpTokenCfg {
    String appid;
    String appsec;
}
