//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sunmi.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadPoolManager {
  private static SingleThreadPoolManager _singleThreadPoolManager;
  private static ExecutorService _pool;

  private SingleThreadPoolManager() {
    _pool = Executors.newSingleThreadExecutor();
  }

  public static synchronized SingleThreadPoolManager getInstance() {
    if(_singleThreadPoolManager == null) {
      _singleThreadPoolManager = new SingleThreadPoolManager();
    }

    return _singleThreadPoolManager;
  }

  public synchronized void push(Runnable task) {
    if(_pool != null) {
      _pool.execute(task);
    }

  }

  public synchronized void shutdown() {
    if(_pool != null) {
      _pool.shutdown();
      _singleThreadPoolManager = null;
    }

  }
}
