//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sunmi.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class PicFromPrintUtils {
  public PicFromPrintUtils() {
  }

  public static byte[] realDraw2PxPoint(Bitmap bit) {
    int w = bit.getWidth();
    int hh = bit.getHeight();
    int h = hh / 24;
    int[] pixels = getAllPixels(bit);
    if(bit.getHeight() % 24 != 0) {
      ++h;
    }

    int max = h * (3 * w + 6) + 1;
    byte wl = (byte)w;
    byte wh = (byte)(w >> 8);
    System.out.println("w=" + w + ",h=" + h + ",wL=" + wl + ",WH=" + wh);
    byte[] data = new byte[max];
    int k = 0;

    for(int j = 0; j < h; ++j) {
      data[k++] = 27;
      data[k++] = 42;
      data[k++] = 33;
      data[k++] = wl;
      data[k++] = wh;

      for(int i = 0; i < w; ++i) {
        for(int m = 0; m < 3; ++m) {
          for(int n = 0; n < 8; ++n) {
            byte b = getPixels(pixels, i, j * 24 + m * 8 + n, w, hh);
            data[k] = (byte)(data[k] + data[k] + b);
          }

          ++k;
        }
      }

      data[k++] = 10;
    }

    System.out.println("k==" + k + "===max==" + max);
    return data;
  }

  public static byte[] realDraw2PxPoint(Bitmap bit, int alignment) {
    bit = compressPic(bit);
    int w = bit.getWidth();
    int hh = bit.getHeight();
    int h = hh / 24;
    int[] pixels = getAllPixels(bit);
    if(bit.getHeight() % 24 != 0) {
      ++h;
    }

    alignment &= 3;
    if(alignment == 3) {
      alignment = 0;
    }

    int max = h * (3 * w + 6) + 10;
    byte wl = (byte)w;
    byte wh = (byte)(w >> 8);
    System.out.println("w=" + w + ",h=" + h + ",wL=" + wl + ",WH=" + wh);
    byte[] data = new byte[max];
    data[0] = 27;
    data[1] = 97;
    data[2] = (byte)alignment;
    data[3] = 27;
    data[4] = 51;
    data[5] = 0;
    int k = 6;

    for(int j = 0; j < h; ++j) {
      data[k++] = 27;
      data[k++] = 42;
      data[k++] = 33;
      data[k++] = wl;
      data[k++] = wh;

      for(int i = 0; i < w; ++i) {
        for(int m = 0; m < 3; ++m) {
          for(int n = 0; n < 8; ++n) {
            byte b = getPixels(pixels, i, j * 24 + m * 8 + n, w, hh);
            data[k] = (byte)(data[k] + data[k] + b);
          }

          ++k;
        }
      }

      data[k++] = 10;
    }

    data[k++] = 27;
    data[k++] = 50;
    data[k++] = 10;
    System.out.println("k==" + k + "===max==" + max);
    return data;
  }

  public static byte[] realDraw2PxPoint(byte[] bytes, int alignment) {
    Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    bit = compressHPic(bit, bit.getHeight() / 24 * 24);
    int w = bit.getWidth();
    int hh = bit.getHeight();
    int h = hh / 24;
    int[] pixels = getAllPixels(bit);
    if(bit.getHeight() % 24 != 0) {
      ++h;
    }

    alignment &= 3;
    if(alignment == 3) {
      alignment = 0;
    }

    int max = h * (3 * w + 6) + 10;
    byte wl = (byte)w;
    byte wh = (byte)(w >> 8);
    System.out.println("w=" + w + ",h=" + h + ",wL=" + wl + ",WH=" + wh);
    byte[] data = new byte[max];
    data[0] = 27;
    data[1] = 97;
    data[2] = (byte)alignment;
    data[3] = 27;
    data[4] = 51;
    data[5] = 0;
    int k = 6;

    for(int j = 0; j < h; ++j) {
      data[k++] = 27;
      data[k++] = 42;
      data[k++] = 33;
      data[k++] = wl;
      data[k++] = wh;

      for(int i = 0; i < w; ++i) {
        for(int m = 0; m < 3; ++m) {
          for(int n = 0; n < 8; ++n) {
            byte b = getPixels(pixels, i, j * 24 + m * 8 + n, w, hh);
            data[k] = (byte)(data[k] + data[k] + b);
          }

          ++k;
        }
      }

      data[k++] = 10;
    }

    data[k++] = 27;
    data[k++] = 50;
    data[k++] = 10;
    System.out.println("k==" + k + "===max==" + max);
    return data;
  }

  public static byte px2Byte(int x, int y, Bitmap bit) {
    if(y < bit.getHeight() && x < bit.getWidth()) {
      int pixel = bit.getPixel(x, y);
      int red = (pixel & 16711680) >> 16;
      int green = (pixel & '\uff00') >> 8;
      int blue = pixel & 255;
      int gray = RGB2Gray(red, green, blue);
      return (byte)(gray < 128?1:0);
    } else {
      return (byte)0;
    }
  }

  private static int RGB2Gray(int r, int g, int b) {
    int gray = (int)(0.299D * (double)r + 0.587D * (double)g + 0.114D * (double)b);
    return gray;
  }

  public static Bitmap compressPic(Bitmap bitmapOrg, int newWidth, int newHeight) {
    int width = bitmapOrg.getWidth();
    int height = bitmapOrg.getHeight();
    Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
    Canvas targetCanvas = new Canvas(targetBmp);
    targetCanvas.drawColor(-1);
    targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), (Paint)null);
    return targetBmp;
  }

  public static Bitmap compressWPic(Bitmap bitmapOrg, int newWidth) {
    int width = bitmapOrg.getWidth();
    int height = bitmapOrg.getHeight();
    int newHeight = height * newWidth / width;
    Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
    Canvas targetCanvas = new Canvas(targetBmp);
    targetCanvas.drawColor(-1);
    targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), (Paint)null);
    return targetBmp;
  }

  public static Bitmap compressHPic(Bitmap bitmapOrg, int newHeight) {
    int width = bitmapOrg.getWidth();
    int height = bitmapOrg.getHeight();
    int newWidth = width * newHeight / height;
    Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
    Canvas targetCanvas = new Canvas(targetBmp);
    targetCanvas.drawColor(-1);
    targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), (Paint)null);
    return targetBmp;
  }

  public static Bitmap compressPic(Bitmap bitmapOrg) {
    int width = bitmapOrg.getWidth();
    int height = bitmapOrg.getHeight();
    Bitmap targetBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    Canvas targetCanvas = new Canvas(targetBmp);
    targetCanvas.drawColor(-1);
    targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, width, height), (Paint)null);
    return targetBmp;
  }

  public static Bitmap compressBitmap(Bitmap bitmapOrg, int newWidth) {
    int width = bitmapOrg.getWidth();
    int height = bitmapOrg.getHeight();
    float scaleWidth = (float)newWidth / (float)width;
    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleWidth);
    Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
    return resizedBitmap;
  }

  public static byte[] rasterDataFromBitmap(Bitmap bm) {
    int ww = bm.getWidth();
    int h = bm.getHeight();
    int w = (ww - 1) / 8 + 1;
    byte[] data = new byte[h * w + 8];
    data[0] = 29;
    data[1] = 118;
    data[2] = 48;
    data[3] = 0;
    data[4] = (byte)w;
    data[5] = (byte)(w >> 8);
    data[6] = (byte)h;
    data[7] = (byte)(h >> 8);
    int[] pixels = getAllPixels(bm);
    int k = 8;

    for(int i = 0; i < h; ++i) {
      for(int j = 0; j < w; ++j) {
        for(int n = 0; n < 8; ++n) {
          byte b = getPixels(pixels, j * 8 + n, i, ww, h);
          data[k] = (byte)(data[k] + data[k] + b);
        }

        ++k;
      }
    }

    return data;
  }

  public static byte[] rasterDataFromBitmap_gh(Bitmap bm) {
    int ww = bm.getWidth();
    int h = bm.getHeight();
    int w = (ww - 1) / 8 + 1;
    byte[] data = new byte[h * w + 8];
    data[0] = 29;
    data[1] = 118;
    data[2] = 48;
    data[3] = 0;
    data[4] = (byte)w;
    data[5] = (byte)(w >> 8);
    data[6] = (byte)h;
    data[7] = (byte)(h >> 8);
    int[] pixels = getAllPixels_gh(bm);
    int k = 8;

    for(int i = 0; i < h; ++i) {
      for(int j = 0; j < w; ++j) {
        for(int n = 0; n < 8; ++n) {
          byte b = getPixels(pixels, j * 8 + n, i, ww, h);
          data[k] = (byte)(data[k] + data[k] + b);
        }

        ++k;
      }
    }

    return data;
  }

  public static byte[] rasterDataFromBitmap(Bitmap bm, int alignment) {
    bm = compressPic(bm);
    alignment &= 3;
    if(alignment == 3) {
      alignment = 0;
    }

    int ww = bm.getWidth();
    int h = bm.getHeight();
    int w = (ww - 1) / 8 + 1;
    int[] pixels = getAllPixels(bm);
    byte[] data = new byte[h * w + 11];
    data[0] = 27;
    data[1] = 97;
    data[2] = (byte)alignment;
    data[3] = 29;
    data[4] = 118;
    data[5] = 48;
    data[6] = 0;
    data[7] = (byte)w;
    data[8] = (byte)(w >> 8);
    data[9] = (byte)h;
    data[10] = (byte)(h >> 8);
    int k = 11;

    for(int i = 0; i < h; ++i) {
      for(int j = 0; j < w; ++j) {
        for(int n = 0; n < 8; ++n) {
          byte b = getPixels(pixels, j * 8 + n, i, ww, h);
          data[k] = (byte)(data[k] + data[k] + b);
        }

        ++k;
      }
    }

    return data;
  }

  public static Bitmap bytes2Bitmap(byte[] bytes) {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }

  public static byte[] rasterDataFromBitmap(byte[] bytes, int alignment) {
    alignment &= 3;
    if(alignment == 3) {
      alignment = 0;
    }

    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    bm = compressPic(bm);
    int ww = bm.getWidth();
    int h = bm.getHeight();
    int w = (ww - 1) / 8 + 1;
    int[] pixels = getAllPixels(bm);
    byte[] data = new byte[h * w + 11];
    data[0] = 27;
    data[1] = 97;
    data[2] = (byte)alignment;
    data[3] = 29;
    data[4] = 118;
    data[5] = 48;
    data[6] = 0;
    data[7] = (byte)w;
    data[8] = (byte)(w >> 8);
    data[9] = (byte)h;
    data[10] = (byte)(h >> 8);
    int k = 11;

    for(int i = 0; i < h; ++i) {
      for(int j = 0; j < w; ++j) {
        for(int n = 0; n < 8; ++n) {
          byte b = getPixels(pixels, j * 8 + n, i, ww, h);
          data[k] = (byte)(data[k] + data[k] + b);
        }

        ++k;
      }
    }

    return data;
  }

  public static Bitmap doubleBitmap(Bitmap bitmap1, Bitmap bitmap2) {
    int h1 = bitmap1.getHeight();
    int w1 = bitmap1.getWidth();
    int h2 = bitmap2.getHeight();
    int w2 = bitmap2.getWidth();
    int new_h = h1 > h2?h1:h2;
    int new_w = w1 + w2 + 10;
    Bitmap targetBmp = Bitmap.createBitmap(new_w, new_h, Config.ARGB_8888);
    Canvas targetCanvas = new Canvas(targetBmp);
    targetCanvas.drawColor(-1);
    targetCanvas.drawBitmap(bitmap1, 0.0F, (float)((new_h - h1) / 2), (Paint)null);
    targetCanvas.drawBitmap(bitmap2, (float)(w1 + 10), (float)((new_h - h2) / 2), (Paint)null);
    return targetBmp;
  }

  public static Bitmap getGrayBitmapFromData(int width, int height) {
    int[] pixels = createGrayBlock(width, height);
    return getBitmapFromData(pixels, width, height);
  }

  public static Bitmap getBlackBitmapFromData(int width, int height, int n) {
    int[] pixels = createBlackBlock(width, height, n);
    return getBitmapFromData(pixels, width, height);
  }

  public static Bitmap getTableBitmapFromData(int rows, int cols, int size) {
    int[] pixels = createTableData(rows, cols, size);
    return getBitmapFromData(pixels, cols * size, rows * size);
  }

  protected static int[] createTableData(int rows, int cols, int size) {
    int width = cols * size;
    int height = rows * size;
    int[] pixels = new int[width * height];
    int k = 0;

    for(int j = 0; j < height; ++j) {
      for(int i = 0; i < width; ++i) {
        if(i != width - 1 && j != height - 1 && i % size != 0 && j % size != 0) {
          pixels[k++] = -1;
        } else {
          pixels[k++] = -16777216;
        }
      }
    }

    return pixels;
  }

  protected static int[] createBlackBlock(int width, int height, int n) {
    int unitHeight = height / n;
    int unitWidth = width / n;
    int[] pixels = new int[width * height];
    int k = 0;

    for(int i = 0; i < height; ++i) {
      for(int j = 0; j < width; ++j) {
        if(i / unitHeight == j / unitWidth) {
          pixels[k++] = -16777216;
        } else {
          pixels[k++] = -1;
        }
      }
    }

    return pixels;
  }

  protected static int[] createGrayBlock(int width, int height) {
    int[] pixels = new int[width * height];
    int[] colors = new int[]{-16777216, -1};
    int k = 0;

    for(int i = 0; i < height; ++i) {
      int index = i & 1;

      for(int j = 0; j < width; ++j) {
        pixels[k++] = colors[index];
        index = 1 - index;
      }
    }

    return pixels;
  }

  protected static Bitmap getBitmapFromData(int[] pixels, int width, int height) {
    Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }

  protected static int[] getAllPixels(Bitmap bit) {
    int k = bit.getWidth() * bit.getHeight();
    int[] pixels = new int[k];
    bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());

    for(int i = 0; i < pixels.length; ++i) {
      int clr = pixels[i];
      int red = (clr & 16711680) >> 16;
      int green = (clr & '\uff00') >> 8;
      int blue = clr & 255;
      pixels[i] = RGB2Gray(red, green, blue) < 128?1:0;
    }

    return pixels;
  }

  protected static int[] getAllPixels_gh(Bitmap bit) {
    int k = bit.getWidth() * bit.getHeight();
    int[] pixels = new int[k];
    bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());

    for(int i = 0; i < pixels.length; ++i) {
      int clr = pixels[i];
      int red = (clr & 16711680) >> 16;
      int green = (clr & '\uff00') >> 8;
      int blue = clr & 255;
      pixels[i] = RGB2Gray(red, green, blue) < 180?1:0;
    }

    return pixels;
  }

  protected static byte getPixels(int[] pixels, int x, int y, int w, int h) {
    return y < h && x < w?(byte)pixels[x + y * w]:0;
  }

  public static Bitmap createQRImage(String url, int size) {
    try {
      if(url != null && !"".equals(url) && url.length() >= 1) {
        Hashtable e = new Hashtable();
        e.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = (new QRCodeWriter()).encode(url, BarcodeFormat.QR_CODE, size, size, e);
        int h = bitMatrix.getHeight();
        int w = bitMatrix.getWidth();
        int[] pixels = new int[w * h];

        for(int bitmap = 0; bitmap < h; ++bitmap) {
          int k = bitmap * w;

          for(int x = 0; x < w; ++x) {
            if(bitMatrix.get(x, bitmap)) {
              pixels[k + x] = -16777216;
            } else {
              pixels[k + x] = -1;
            }
          }
        }

        Bitmap var11 = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        var11.setPixels(pixels, 0, w, 0, 0, w, h);
        return var11;
      } else {
        return null;
      }
    } catch (WriterException var10) {
      var10.printStackTrace();
      return null;
    }
  }

  public static Bitmap createBarcodeImage(String data, BarcodeFormat format, int width, int height) {
    try {
      Hashtable e = new Hashtable();
      e.put(EncodeHintType.CHARACTER_SET, "utf-8");
      BitMatrix bits = null;
      bits = (new MultiFormatWriter()).encode(data, format, width, height, e);
      int h = bits.getHeight();
      int w = bits.getWidth();
      int[] pixels = new int[w * h];

      for(int bitmap = 0; bitmap < h; ++bitmap) {
        int k = bitmap * width;

        for(int x = 0; x < w; ++x) {
          if(bits.get(x, bitmap)) {
            pixels[k + x] = -16777216;
          } else {
            pixels[k + x] = -1;
          }
        }
      }

      Bitmap var13 = Bitmap.createBitmap(w, h, Config.ARGB_8888);
      var13.setPixels(pixels, 0, w, 0, 0, w, h);
      return var13;
    } catch (WriterException var12) {
      var12.printStackTrace();
      return null;
    }
  }

  protected static int[] createLineData(int size, int width) {
    int[] pixels = new int[width * (size + 6)];
    int k = 0;

    int j;
    int i;
    for(j = 0; j < 3; ++j) {
      for(i = 0; i < width; ++i) {
        pixels[k++] = -1;
      }
    }

    for(j = 0; j < size; ++j) {
      for(i = 0; i < width; ++i) {
        pixels[k++] = -16777216;
      }
    }

    for(j = 0; j < 3; ++j) {
      for(i = 0; i < width; ++i) {
        pixels[k++] = -1;
      }
    }

    return pixels;
  }

  public static Bitmap getLineBitmapFromData(int size, int width) {
    int[] pixels = createLineData(size, width);
    return getBitmapFromData(pixels, width, size + 6);
  }
}
