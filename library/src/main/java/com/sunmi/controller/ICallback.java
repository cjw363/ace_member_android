
package com.sunmi.controller;

public interface ICallback {
  void onRunResult(boolean var1);

  void onReturnString(String var1);

  void onRaiseException(int var1, String var2);
}
