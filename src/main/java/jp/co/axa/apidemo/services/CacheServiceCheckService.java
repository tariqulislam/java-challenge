package jp.co.axa.apidemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/* Using for getting cache information from CacheManager*/
@Service
public class CacheServiceCheckService {

    @Autowired
    CacheManager cacheManager;

    /* Method: Get the cache by cache name */
    public Cache getCacheByName(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

}
