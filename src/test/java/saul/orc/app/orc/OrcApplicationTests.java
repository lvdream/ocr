package saul.orc.app.orc;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import saul.orc.app.orc.excel.ExcelReaderFactory;
import saul.orc.app.orc.image.MpImageCodeHandle;
import saul.orc.app.orc.image.UrlImageFinder;
import saul.orc.app.orc.util.TransApi;

import java.io.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrcApplicationTests {
    @Autowired
    private UrlImageFinder finder;
    @Autowired
    private MpImageCodeHandle handle;
    @Autowired
    private TransApi api;

        @Test
    public void contextLoads() {

//        ReturnImg returnImg = finder.resultURL("http://wx1.sinaimg.cn/large/71adc809gy1fucxexah6dj20qo1bfq9f.jpg");
//        finder.getTableImg("/Users/Saul/Downloads/1-1P42609351V56.png");
//        finder.getTableRes("11663730_437292");
        System.out.println(finder.resultURL("/Users/Saul/Desktop/IMG_1092.PNG"));
    }

    //    @Test
    public void read() throws Exception {
        try (InputStream in = new FileInputStream("/Users/Saul/Downloads/BA7BCDF4A1674B36B7C2D151729B798F.xls")) {
            AnalysisEventListener<List<String>> listener = new AnalysisEventListener<List<String>>() {

                @Override
                public void invoke(List<String> object, AnalysisContext context) {
                    System.err.println("Row:" + context.getCurrentRowNum() + " Data:" + object);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    System.err.println("doAfterAllAnalysed...");
                }
            };
            ExcelReader excelReader = ExcelReaderFactory.getExcelReader(in, null, listener);
            excelReader.read();
        }
    }

    //    @Test
    public void testExcel2003WithReflectModel() throws FileNotFoundException {
        InputStream inputStream = null;
        List<List<String>> list = Lists.newArrayList();
        try {
            inputStream = new FileInputStream("/Users/Saul/Desktop/e42334c41c954988b75e07808344a136.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ExcelReader reader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null,
                    new AnalysisEventListener<List<String>>() {
                        @Override
                        public void invoke(List<String> object, AnalysisContext context) {
                            if (context.getCurrentSheet().getSheetName().equals("body")) {
                                list.add(object);
                                System.out.println(
                                        "当前sheet:[" + context.getCurrentSheet().getSheetNo() + " ],[ " + context.getCurrentSheet().getSheetName() + "] 当前行：" + context.getCurrentRowNum()
                                                + " data:" + object);
                            }
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {

                        }
                    });

            reader.read();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(list.toString());
            OutputStream out = new FileOutputStream("/Users/Saul/Downloads/test.xlsx");
            try {
                ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
                Sheet sheet1 = new Sheet(1, 0);
                sheet1.setSheetName("第一个sheet");
                writer.write0(list, sheet1);
                writer.finish();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 华为测试
     */
//    @Test

    //    @Test
    public void testMP() throws IOException {
        handle.getQRimg(handle.getToken());
    }

//    @Test
    public void testTranslate() throws UnsupportedEncodingException {
        System.out.println(api.getTransResult("我写了上千篇反对极端宗教和所谓自贱维稳的帖子。但我绝不是对维吾尔族人深仇大恨。不然也不会赞助这个。作为新疆人，还是希望新疆能平平安安。给陈书记点赞"));
    }
}
