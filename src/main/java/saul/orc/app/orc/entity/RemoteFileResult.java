package saul.orc.app.orc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoteFileResult {
    /**
     * result : {"result_data":"http://bj.bcebos.com/v1/aip-web/form_ocr/154BCE3149D74EB0AC3BB45CBC647025.xls?authorization=bce-auth-v1%2Ff86a2044998643b5abc89b59158bad6d%2F2018-08-28T12%3A39%3A56Z%2F86400%2F%2Feea60633fe778aceb5dad75421cd425f863e3662a5e1351f87fdfdb87eeb0732","ret_msg":"已完成","request_id":"11663730_439351","percent":100,"ret_code":3}
     * log_id : 153545999667914
     */

    private ResultBean result;
    private long log_id;
    private String error_msg;
    private String error_code;

    @Data
    public static class ResultBean {
        /**
         * result_data : http://bj.bcebos.com/v1/aip-web/form_ocr/154BCE3149D74EB0AC3BB45CBC647025.xls?authorization=bce-auth-v1%2Ff86a2044998643b5abc89b59158bad6d%2F2018-08-28T12%3A39%3A56Z%2F86400%2F%2Feea60633fe778aceb5dad75421cd425f863e3662a5e1351f87fdfdb87eeb0732
         * ret_msg : 已完成
         * request_id : 11663730_439351
         * percent : 100
         * ret_code : 3
         */

        private String result_data;
        private String ret_msg;
        private String request_id;
        private int percent;
        private int ret_code;
    }
}
