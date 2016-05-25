package com.j2y.familypop.activity.manager.gallery;

/**
 * Created by J2YSoft_Programer on 2016-05-02.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author J2Y
 *
 * @param <K>
 * @param <V>
 */

public class ImageCache<K,V>
{
    private static final float _hashTable_Factor = 0.75f;
    private LinkedHashMap<K,V> _map;
    private int _cacheSize;

    public ImageCache(int cacheSize)
    {
        this._cacheSize = cacheSize;
        int hashTableCapacity = (int) Math.ceil(cacheSize/_hashTable_Factor) + 1;
        _map = new LinkedHashMap<K,V>(hashTableCapacity,_hashTable_Factor,true)
        {
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Entry<K, V> eldest) {
                return size() > ImageCache.this._cacheSize;
            }
        };
    }

    public synchronized boolean containKey(String key)
    {
        return _map.containsKey(key);
    }

    public synchronized V get(K key)
    {
        return _map.get(key);
    }

    public synchronized void put(K key, V value)
    {
        _map.put(key,value);
    }

    public synchronized Collection<Map.Entry<K,V>> getAll()
    {
        return new ArrayList<Map.Entry<K, V>>(_map.entrySet());
    }
}
