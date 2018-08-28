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
import saul.orc.app.orc.image.UrlImageFinder;

import java.io.*;
import java.util.List;

import static org.apache.tomcat.util.file.ConfigFileLoader.getInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrcApplicationTests {
    @Autowired
    private UrlImageFinder finder;

        @Test
    public void contextLoads() {
//        ReturnImg returnImg = finder.resultURL("http://wx1.sinaimg.cn/large/71adc809gy1fucxexah6dj20qo1bfq9f.jpg");
//        finder.getTableImg("/Users/Saul/Downloads/1-1P42609351V56.png");
//        finder.getTableRes("11663730_437292");
        finder.getTableRes3("/Users/Saul/Desktop/11.png");
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
            inputStream = getInputStream("/Users/Saul/Downloads/BA7BCDF4A1674B36B7C2D151729B798F.xls");
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
}
