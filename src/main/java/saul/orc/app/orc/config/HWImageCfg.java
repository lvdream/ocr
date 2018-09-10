package saul.orc.app.orc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "common.ocr.huawei")
@Data
public class HWImageCfg {
    String endpoint;
    String region;
    String ak;
    String sk;
    String uri;
}
