package com.dhph.bigdata.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wulizheng
 * @Description: 枚举
 **/
public interface CommonEnum {

    /**
     * 共同CODE 布尔类型
     */
    @AllArgsConstructor
    enum Common {

        /**
         * 公共字段
         * 状态(0-禁用，1-启用)
         */
        ENABLED(Boolean.TRUE, "启用"),
        UN_ENABLED(Boolean.FALSE, "禁用"),
        /**
         * 公共字段
         * 是否删除(0-未删除，1-已删除)
         */
        DELETED(Boolean.TRUE, "已删除"),
        UN_DELETED(Boolean.FALSE, "未删除"),
        /**
         * 状态(0-否，1-是)
         */
        TRUE(Boolean.TRUE, "是"),
        FALSE(Boolean.FALSE, "否"),

        /**
         * 是、否
         */
        YES(Boolean.TRUE, "是"),
        NO(Boolean.FALSE, "否");

        @Setter
        @Getter
        private Boolean code;
        @Setter
        @Getter
        private String value;
    }
}