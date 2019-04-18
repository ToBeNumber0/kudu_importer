package com.dhph.bigdata.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wulizheng
 */
@Slf4j
public class JsonUtil {

    public static String toJson(Object object) {
        if (null == object) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JSON转换异常", e);
            return "";
        }
    }
}