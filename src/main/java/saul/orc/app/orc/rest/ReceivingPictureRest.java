package saul.orc.app.orc.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import saul.orc.app.orc.config.OrcFileCfg;
import saul.orc.app.orc.entity.ReturnImg;
import saul.orc.app.orc.image.UrlImageFinder;
import saul.orc.app.orc.util.Result;
import saul.orc.app.orc.util.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class ReceivingPictureRest {
    @Autowired
    private OrcFileCfg cfg;
    @Autowired
    private UrlImageFinder finder;

    // 文件上传
    @PostMapping(value = "/upload")
    @ResponseBody
    public Result upload(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return Result.failure(ResultCode.PARAM_FILE_IS_BLANK);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = cfg.getPath();
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
            log.info("文件上传成功:" + dest.getPath());
            //文件识别类型,0:Excel,1:Text
            if (StringUtils.equalsIgnoreCase(request.getParameter("type"), "0")) {
                ReturnImg img = finder.getTableRes(cfg.getPath() + fileName);
                return Result.success(img);
            } else if (StringUtils.equalsIgnoreCase(request.getParameter("type"), "1")) {
                ReturnImg img = finder.resultURL(cfg.getPath() + fileName);
                return Result.success(img);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }

    // 文件下载
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request,
                               HttpServletResponse response) {
        String fileName = request.getParameter("file");
        String realPath = cfg.getPath();
        File file = new File(realPath, fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    // 多文件上传
    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        String filePath = cfg.getPath();
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + System.currentTimeMillis() + extName)));
                    stream.write(bytes);
                    stream.close();


                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => " + e.getMessage();
                }
            } else {
                return "You failed to upload " + i + " because the file was empty.";
            }
        }
        return "upload successful";
    }
}
