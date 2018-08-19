package saul.orc.app.orc.config;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "common.ocr.baidu")
@Data
public class OrcPropertiesCfg {
    private String appid;

    private String apikey;

    private String secret;
    private String language_type;
    private String detect_direction;
    private String detect_language;
    private String probability;
    private List<Map<String,String>> error;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
