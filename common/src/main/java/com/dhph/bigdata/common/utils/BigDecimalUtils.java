package com.dhph.bigdata.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BigDecimalUtils {


    /**
     * 使用BigDecimal，保留小数点后1位
     * @param value
     * @return
     */
    public static String format(double value) {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(1, RoundingMode.HALF_UP);
            return bd.toString() ;
    }
    /**
     * 使用BigDecimal，保留小数点后两位
     * @param value
     * @return
     */
    public static String format1(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString() ;
    }
    /**
     * 使用BigDecimal，保留小数点后四位
     * @param value
     * @return
     */
    public static String format2(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.toString() ;
    }
    /**
     * 使用DecimalFormat,保留小数点后两位
     * @param value
     * @return
     */
    public static String format3(double value) {

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
}
