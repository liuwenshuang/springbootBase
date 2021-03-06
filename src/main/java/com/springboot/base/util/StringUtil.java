package com.springboot.base.util;

import java.text.NumberFormat;

/**
 * string 工具类
 * Created by jay on 16-3-4.
 */
public class StringUtil {

    public static String firstCharUpperCase(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static boolean isEmpty(String cs) {
        return cs == null || cs.trim().length() == 0;
    }


    /**
     * @param figures 加几位0
     * @param value   给谁加0
     * @return 格式化好的值
     */

    public static String addZero(int figures, Object value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(figures);
        nf.setMinimumIntegerDigits(figures);
        return nf.format(value);
    }

}
