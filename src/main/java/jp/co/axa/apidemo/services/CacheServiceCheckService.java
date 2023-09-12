package jp.co.axa.apidemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceCheckService {

    @Autowired
    CacheManager cacheManager;

    public Cache getCacheByName(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

}
