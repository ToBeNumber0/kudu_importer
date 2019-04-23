package com.dhph.bigdata.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author carlosxiao
 */
@Slf4j
public class CommonUtil {

    public static Integer getLimitSize(Integer totalSize) {
        int processorNum = Runtime.getRuntime().availableProcessors() * 8;
        if (totalSize % processorNum == 0) {
            return totalSize / processorNum;
        } else {
            return totalSize / processorNum + 1;
        }
    }

    /**
     * 判断Object对象为空或空字符串
     * @param obj
     * @return
     */
    public static Boolean isObjectNotEmpty(Object obj) {
        String str = ObjectUtils.toString(obj, "");
        return StringUtils.isNotBlank(str);
    }

}