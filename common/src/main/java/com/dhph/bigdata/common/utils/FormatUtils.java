package com.dhph.bigdata.common.utils;

import java.text.DecimalFormat;

public class FormatUtils {


    public static String  formatFrist(Double data) {
           Double d = new Double(0.0);
           String da = "0.0";
           if (data.equals(d)){
               return da;
           } else {
               DecimalFormat df = new DecimalFormat("0.0");
               return df.format(data);
           }
    }
    public static String formatTwo(Double data) {
        Double d = new Double(0.0);
        String da= "0.00";
        if (data.equals(d)){
            return da;
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(data/100);
        }
    }
/*    public static void main(String[] args){
        Double d = new Double(100);
        Double da = d/100;
        System.out.println(da);
    }*/
public static String formatTwo1(Double data) {
    Double d = new Double(0.0);
    String da= "0.00";
    if (data.equals(d)){
        return da;
    } else {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(data);
    }
}

    public static String formatFour(Double data) {
            Double d = new Double(0.0);
            String da= "0.0000";
            if (data.equals(d)){
                return da;
            } else {
                DecimalFormat df = new DecimalFormat("0.0000");
                Double daa = data/100;
                return df.format(daa);
            }
    }
}
