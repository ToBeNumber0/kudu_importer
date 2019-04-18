package com.dhph.bigdata.common.utils;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

import static org.dozer.loader.api.TypeMappingOptions.mapEmptyString;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;

/**
 * @author wulizheng
 */
public class DozerUtil {

    /**
     * 复制属性，忽略null
     * @param sources 复制源
     * @param destination 接收对象
     */
    public static void copyProperties(final Object sources, final Object destination) {

        WeakReference weakReference = new WeakReference<>(new DozerBeanMapper());
        DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();
        if (null == mapper) {
            mapper = new DozerBeanMapper();
        }
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(sources.getClass(), destination.getClass(), mapNull(false), mapEmptyString(false));
            }
        });
        mapper.map(sources, destination);
        mapper.destroy();
        weakReference.clear();
    }


    /**
     * copy Collection
     * @param sourceList
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(Collection sourceList, Class<T> destinationClass) {
        WeakReference weakReference = new WeakReference<>(new DozerBeanMapper());
        DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();
        if (null == mapper) {
            mapper = new DozerBeanMapper();
        }

        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = mapper.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

}