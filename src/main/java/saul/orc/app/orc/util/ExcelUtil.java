package saul.orc.app.orc.util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saul.orc.app.orc.config.OrcFileCfg;

import java.io.*;
import java.util.List;

/**
 * Excel 操作类
 */
@Component
public class ExcelUtil {
    @Autowired
    private OrcFileCfg cfg;

    /**
     * @param excelPath 文件读取路径
     * @param Keepsheet 需要保留的sheet页
     * @return
     * @throws FileNotFoundException
     */
    public List<List<String>> readExcel(String excelPath, String Keepsheet) throws FileNotFoundException {
        InputStream inputStream = null;
        List<List<String>> list = Lists.newArrayList();
        try {
            inputStream = new FileInputStream(excelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ExcelReader reader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null,
                    new AnalysisEventListener<List<String>>() {
                        @Override
                        public void invoke(List<String> object, AnalysisContext context) {
                            if (context.getCurrentSheet().getSheetName().equals(Keepsheet)) {
                                list.add(object);
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
        }
        return list;
    }

    /**
     * 依据数据,开始写Excel
     *
     * @param data    ExcelData
     * @param outFile 最终输出的文件
     */
    public void writeExcel(List<List<String>> data, String outFile) throws FileNotFoundException {
        OutputStream out = new FileOutputStream(outFile);
        try {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName(cfg.getOutsheet());
            writer.write0(data, sheet1);
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