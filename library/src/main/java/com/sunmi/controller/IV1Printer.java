
package com.sunmi.controller;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.google.zxing.BarcodeFormat;

public interface IV1Printer {
  void setCallback(ICallback var1);

  void beginTransaction();

  void cancelTransaction();

  void commitTransaction();

  void printerInit();

  void printerSelfChecking();

  String getPrinterSerialNo();

  String getPrinterVersion();

  String getPrinterModal();

  void lineWrap(int var1);

  void sendRAWData(byte[] var1);

  void setAlignment(int var1);

  void setFontName(String var1);

  void setFontName(Typeface var1);

  void setFontSize(float var1);

  void printText(String var1);

  void printTextWithFont(String var1, String var2, float var3);

  void printColumnsText(String[] var1, int[] var2, int[] var3);

  void printBitmap(Bitmap var1);

  void printBarCode(String var1, BarcodeFormat var2, int var3, int var4);

  void printQRCode(String var1, int var2);

  void printDoubleQRCode(String var1, String var2, int var3);

  void printHorizontalLine(int var1, int var2);

  void printTypeHorizontalLine(int var1);

  void printOriginalText(String var1);
}
