package saul.orc.app.orc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import saul.orc.app.orc.image.UrlImageFinder;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrcApplicationTests {
    @Autowired
    private UrlImageFinder finder;

    @Test
    public void contextLoads() {
        ReturnImg returnImg = finder.resultURL("http://wx1.sinaimg.cn/large/71adc809gy1fucxexah6dj20qo1bfq9f.jpg");
        log.info(returnImg.toString());
    }

}
