package saul.orc.app.orc.image;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import saul.orc.app.orc.entity.ReturnImg;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UrlImageFinderTest {
    @Autowired
    private UrlImageFinder finder;

    @Test
    public void resultURL() throws Exception {

    }

    @Test
    public void resultLocal() throws Exception {
        String data = finder.resultLocal("/Users/Saul/Desktop/IMG_1092.PNG");
//        String data = finder.resultLocal("/Users/Saul/Desktop/IMG_0581.JPG");
//        String data = finder.resultLocal("/Users/Saul/Desktop/IMG_0811.PNG");
        ReturnImg returnImg = JSON.parseObject(data, ReturnImg.class);
        StringBuffer buffer = new StringBuffer();
        for (ReturnImg.WordsResultBean next : returnImg.getWords_result()) {
            buffer.append(next.getWords());
            buffer.append(StringUtils.LF);
        }
        System.out.println(buffer.toString());
    }

    @Test
    public void getTableRes() throws Exception {
    }

}