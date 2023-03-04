package com.zcp.core.engine.metas;

import lombok.Data;

import java.util.HashMap;

/**
 * @author ZCP
 * @title: MetaData
 * @projectName JavaGen
 * @description: 元数据
 * @date 2023/1/15 20:57
 */
@Data
public class MetaData<K, V> extends HashMap<K, V> {

    /**
     * 前缀
     */
    private String prefix = "";

    /**
     * 如果传入的key是 prefix.key，那需要去掉前缀
     *
     * @param key
     * @return
     */
    @Override
    public V get(Object key) {
        return containsKey(key) ? super.get(key) : super.get(((String) key).replaceFirst(prefix.concat("."), ""));
    }
}
