package com.bomaos.reception.util;

public interface SynchronizedByKeyService {
    void exec(String key, Runnable runnable);
}
