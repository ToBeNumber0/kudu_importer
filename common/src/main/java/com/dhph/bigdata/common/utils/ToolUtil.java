/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dhph.bigdata.common.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 高频方法集合类
 */
public class ToolUtil {

    /**
     * list分割
     *
     * @param sourceList 需要分割的集合
     * @param maxSend    每个子集合大小
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> sourceList, int maxSend) {
        // 计算limit，需要拆成多少个子集合
        int limit = sourceList.size() % maxSend == 0 ? sourceList.size() / maxSend : sourceList.size() / maxSend + 1;
        return Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> sourceList.stream().skip(a * maxSend).limit(maxSend).parallel().collect(Collectors.toList())).collect(Collectors.toList());
    }


}