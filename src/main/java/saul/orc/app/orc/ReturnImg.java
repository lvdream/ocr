package saul.orc.app.orc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnImg {

    /**
     * log_id : 8597706989079099925
     * words_result : [{"probability":{"average":0.894639,"min":0.550875,"variance":0.033246},"words":"l1中国移动4G"},{"probability":{"average":0.998463,"min":0.993305,"variance":5.0E-6},"words":"下午7:26"},{"probability":{"average":0.933448,"min":0.77596,"variance":0.007867},"words":"米66%℃"},{"probability":{"average":0.911457,"min":0.609215,"variance":0.023138},"words":"N新新华网"},{"probability":{"average":0.999974,"min":0.999953,"variance":0},"words":"关注"},{"probability":{"average":0.877941,"min":0.568136,"variance":0.032622},"words":"N新一手机版财经"},{"probability":{"average":0.916069,"min":0.688341,"variance":0.013728},"words":"m. news. cn"},{"probability":{"average":0.993333,"min":0.86891,"variance":8.15E-4},"words":"2018年1-6月份全国规模以上工业企业"},{"probability":{"average":0.996343,"min":0.968625,"variance":9.6E-5},"words":"利润增长17.2%"},{"probability":{"average":0.995484,"min":0.978213,"variance":4.7E-5},"words":"2018-07-31来源:国家统计局"},{"probability":{"average":0.989305,"min":0.769846,"variance":0.002294},"words":"1-6月份,全国规模以上工业企业实现利润总额"},{"probability":{"average":0.996714,"min":0.96646,"variance":6.2E-5},"words":"33882.1亿元,同比增长17.2%(按可比口径计算,详见"},{"probability":{"average":0.998827,"min":0.990518,"variance":5.0E-6},"words":"附注二),增速比1-5月份加快0.7个百分点。"},{"probability":{"average":0.987344,"min":0.704593,"variance":0.003478},"words":"1-6月份,规模以上工业企业中,国有控股企业实现"},{"probability":{"average":0.998367,"min":0.979422,"variance":1.6E-5},"words":"利润总额10248.7亿元,同比增长31.5%;集体企业实现"},{"probability":{"average":0.997504,"min":0.970313,"variance":4.9E-5},"words":"利润总额109.7亿元,增长4.6%;股份制企业实现利润总"},{"probability":{"average":0.995523,"min":0.911341,"variance":2.84E-4},"words":"额24059.9亿元,增长21%;外商及港澳台商投资企业实"},{"probability":{"average":0.998963,"min":0.988773,"variance":6.0E-6},"words":"现利润总额8197.6亿元,增长8.7%;私营企业实现利润"},{"probability":{"average":0.99807,"min":0.978051,"variance":2.9E-5},"words":"总额8889.1亿元,增长10%"},{"probability":{"average":0.744101,"min":0.488202,"variance":0.065484},"words":"< O"},{"probability":{"average":0.997635,"min":0.993488,"variance":9.0E-6},"words":"Q搜索"}]
     * words_result_num : 21
     * language : -1
     * direction : 0
     */

    private long log_id;
    private int words_result_num;
    private int language;
    private int direction;
    private List<WordsResultBean> words_result;
    private String error_msg;
    private String error_code;
    private String returnstr;


    @Data
    public static class WordsResultBean {
        /**
         * probability : {"average":0.894639,"min":0.550875,"variance":0.033246}
         * words : l1中国移动4G
         */

        private ProbabilityBean probability;
        private String words;

        public ProbabilityBean getProbability() {
            return probability;
        }

        @Data
        public static class ProbabilityBean {
            /**
             * average : 0.894639
             * min : 0.550875
             * variance : 0.033246
             */

            private double average;
            private double min;
            private double variance;

        }
    }
}
