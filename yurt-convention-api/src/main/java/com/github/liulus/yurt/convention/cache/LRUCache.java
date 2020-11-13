package com.github.liulus.yurt.convention.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/13
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final int DEFAULT_CACHE_SIZE = 64;
    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    public LRUCache() {
        super(DEFAULT_CACHE_SIZE, 0.75f, true);
        maxElements = DEFAULT_CACHE_SIZE;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxElements;
    }

}
