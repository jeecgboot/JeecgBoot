package com.shop.common.util;

public interface SynchronizedByKeyService {
    void exec(String key, Runnable runnable);
}
