//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sunmi.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
  private ExecutorService service;
  private static final ThreadPoolManager manager = new ThreadPoolManager();

  private ThreadPoolManager() {
    int num = Runtime.getRuntime().availableProcessors() * 20;
    this.service = Executors.newFixedThreadPool(num);
  }

  public static ThreadPoolManager getInstance() {
    return manager;
  }

  public void executeTask(Runnable runnable) {
    this.service.execute(runnable);
  }
}
