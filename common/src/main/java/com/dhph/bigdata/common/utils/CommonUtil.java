package com.dhph.bigdata.common.utils;

import lombok.extern.slf4j.Slf4j;

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
}