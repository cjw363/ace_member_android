//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sunmi.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class BytesUtil {
  public BytesUtil() {
  }

  public static byte[] getBytesFromFile(File f) {
    if(f == null) {
      return null;
    } else {
      try {
        FileInputStream e = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];

        int n;
        while((n = e.read(b)) != -1) {
          out.write(b, 0, n);
        }

        e.close();
        out.close();
        return out.toByteArray();
      } catch (IOException var5) {
        var5.printStackTrace();
        return null;
      }
    }
  }

  public static byte[] initBlackBlock(int w) {
    int ww = (w + 7) / 8;
    int n = (ww + 11) / 12;
    int hh = n * 24;
    byte[] data = new byte[hh * ww + 10];
    data[0] = 10;
    data[1] = 29;
    data[2] = 118;
    data[3] = 48;
    data[4] = 0;
    data[5] = (byte)ww;
    data[6] = (byte)(ww >> 8);
    data[7] = (byte)hh;
    data[8] = (byte)(hh >> 8);
    int k = 9;

    for(int i = 0; i < n; ++i) {
      for(int j = 0; j < 24; ++j) {
        for(int m = 0; m < ww; ++m) {
          if(m / 12 == i) {
            data[k++] = -1;
          } else {
            data[k++] = 0;
          }
        }
      }
    }

    data[k++] = 10;
    return data;
  }

  public static byte[] initBlackBlock(int h, int w) {
    int hh = h;
    int ww = (w - 1) / 8 + 1;
    byte[] data = new byte[h * ww + 10];
    data[0] = 10;
    data[1] = 29;
    data[2] = 118;
    data[3] = 48;
    data[4] = 0;
    data[5] = (byte)ww;
    data[6] = (byte)(ww >> 8);
    data[7] = (byte)h;
    data[8] = (byte)(h >> 8);
    int k = 9;

    for(int i = 0; i < hh; ++i) {
      for(int j = 0; j < ww; ++j) {
        data[k++] = -1;
      }
    }

    data[k++] = 10;
    return data;
  }

  public static byte[] initGrayBlock(int h, int w) {
    int hh = h;
    int ww = (w - 1) / 8 + 1;
    byte[] data = new byte[h * ww + 10];
    data[0] = 10;
    data[1] = 29;
    data[2] = 118;
    data[3] = 48;
    data[4] = 0;
    data[5] = (byte)ww;
    data[6] = (byte)(ww >> 8);
    data[7] = (byte)h;
    data[8] = (byte)(h >> 8);
    int k = 9;
    byte m = -86;

    for(int i = 0; i < hh; ++i) {
      m = (byte)(~m);

      for(int j = 0; j < ww; ++j) {
        data[k++] = m;
      }
    }

    data[k++] = 10;
    return data;
  }

  public static byte[] initTable(int h, int w) {
    int hh = h * 32;
    int ww = w * 4;
    byte[] data = new byte[hh * ww + 10];
    data[0] = 10;
    data[1] = 29;
    data[2] = 118;
    data[3] = 48;
    data[4] = 0;
    data[5] = (byte)ww;
    data[6] = (byte)(ww >> 8);
    data[7] = (byte)hh;
    data[8] = (byte)(hh >> 8);
    int k = 9;
    byte m = 31;

    int j;
    for(j = 0; j < h; ++j) {
      int t;
      for(t = 0; t < w; ++t) {
        data[k++] = -1;
        data[k++] = -1;
        data[k++] = -1;
        data[k++] = -1;
      }

      if(j == h - 1) {
        m = 30;
      }

      for(t = 0; t < m; ++t) {
        for(int j1 = 0; j1 < w - 1; ++j1) {
          data[k++] = -128;
          data[k++] = 0;
          data[k++] = 0;
          data[k++] = 0;
        }

        data[k++] = -128;
        data[k++] = 0;
        data[k++] = 0;
        data[k++] = 1;
      }
    }

    for(j = 0; j < w; ++j) {
      data[k++] = -1;
      data[k++] = -1;
      data[k++] = -1;
      data[k++] = -1;
    }

    data[k++] = 10;
    return data;
  }

  public static byte[] getGbk(String paramString) {
    byte[] arrayOfByte = null;

    try {
      arrayOfByte = paramString.getBytes("GB18030");
    } catch (Exception var3) {
      var3.printStackTrace();
    }

    return arrayOfByte;
  }

  public static byte[] setWH(int mode) {
    byte[] returnText = new byte[]{(byte)29, (byte)33, (byte)0};
    switch(mode) {
    case 2:
      returnText[2] = 16;
      break;
    case 3:
      returnText[2] = 1;
      break;
    case 4:
      returnText[2] = 17;
      break;
    default:
      returnText[2] = 0;
    }

    return returnText;
  }

  public static byte[] setZoom(int level) {
    byte[] rv = new byte[]{(byte)29, (byte)33, (byte)((level & 7) << 4 | level & 7)};
    return rv;
  }

  public static byte[] setAlignCenter(int align) {
    byte[] returnText = new byte[]{(byte)32, (byte)10, (byte)27, (byte)97, (byte)0};
    switch(align) {
    case 1:
      returnText[4] = 1;
      break;
    case 2:
      returnText[4] = 2;
      break;
    default:
      returnText[4] = 0;
    }

    return returnText;
  }

  public static byte[] setBold(boolean dist) {
    byte[] returnText = new byte[]{(byte)27, (byte)69, (byte)0};
    if(dist) {
      returnText[2] = 1;
    } else {
      returnText[2] = 0;
    }

    return returnText;
  }

  public static byte[] setCusorPosition(int position) {
    byte[] returnText = new byte[]{(byte)27, (byte)36, (byte)position, (byte)(position >> 8)};
    return returnText;
  }

  public static byte[] PrintBarcode(String stBarcode) {
    int iLength = stBarcode.length() + 4;
    byte[] returnText = new byte[iLength];
    returnText[0] = 29;
    returnText[1] = 107;
    returnText[2] = 69;
    returnText[3] = (byte)stBarcode.length();
    System.arraycopy(stBarcode.getBytes(), 0, returnText, 4, stBarcode.getBytes().length);
    return returnText;
  }

  public static byte[] CutPaper() {
    byte[] returnText = new byte[]{(byte)32, (byte)10, (byte)29, (byte)86, (byte)66, (byte)0};
    return returnText;
  }

  public static byte[] selfCheck() {
    byte[] returnText = new byte[]{(byte)31, (byte)27, (byte)31, (byte)83};
    return returnText;
  }

  public static byte[] getPrinterStatus() {
    byte[] data = new byte[]{(byte)10, (byte)16, (byte)4, (byte)1};
    return data;
  }

  public static String getHexStringFromBytes(byte[] data) {
    if(data != null && data.length > 0) {
      String hexString = "0123456789ABCDEF";
      int size = data.length * 2;
      StringBuilder sb = new StringBuilder(size);

      for(int i = 0; i < data.length; ++i) {
        sb.append(hexString.charAt((data[i] & 240) >> 4));
        sb.append(hexString.charAt((data[i] & 15) >> 0));
      }

      return sb.toString();
    } else {
      return null;
    }
  }

  public static byte[] getPrintQRCode(String code, int modulesize, int errorlevel) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    try {
      buffer.write(setQRCodeSize(modulesize));
      buffer.write(setQRCodeErrorLevel(errorlevel));
      buffer.write(getQCodeBytes(code));
      buffer.write(getBytesForPrintQRCode(true));
    } catch (Exception var5) {
      var5.printStackTrace();
    }

    return buffer.toByteArray();
  }

  public static byte[] getPrintDoubleQRCode(String code1, String code2, int modulesize, int errorlevel) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    try {
      buffer.write(setQRCodeSize(modulesize));
      buffer.write(setQRCodeErrorLevel(errorlevel));
      buffer.write(getQCodeBytes(code1));
      buffer.write(getBytesForPrintQRCode(false));
      buffer.write(getQCodeBytes(code2));
      buffer.write(new byte[]{(byte)27, (byte)92, (byte)48, (byte)0});
      buffer.write(getBytesForPrintQRCode(true));
    } catch (Exception var6) {
      var6.printStackTrace();
    }

    return buffer.toByteArray();
  }

  public static byte[] getColumnsText(String[] colsText, int[] colsWidth, int[] colsAlign, int charWidth, int columnPadding) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int size = colsText.length;
    LinkQueue[] link = new LinkQueue[size];
    int[] s = new int[size];
    int min = 1;

    int k;
    int e;
    for(k = 0; k < size; ++k) {
      link[k] = new LinkQueue();
      byte[] t = getGbk(colsText[k]);

      for(e = 0; e < t.length; ++e) {
        link[k].r_push_queue(Byte.valueOf(t[e]));
      }

      s[k] = (t.length + colsWidth[k] - 1) / colsWidth[k];
      if(min < s[k]) {
        min = s[k];
      }
    }

    for(k = 1; k <= min; ++k) {
      try {
        e = 0;

        for(int i = 0; i < size; ++i) {
          byte[] tmp2;
          if(link[i].size() > 0) {
            boolean tmp1 = true;
            int var18;
            if(link[i].size() <= colsWidth[i]) {
              var18 = link[i].size();
            } else {
              var18 = colsWidth[i];
            }

            tmp2 = new byte[var18];

            for(int j = 0; j < var18; ++j) {
              tmp2[j] = ((Byte)link[i].l_pop_queue()).byteValue();
              if(tmp2[j] < 0) {
                ++j;
                if(j < var18) {
                  tmp2[j] = ((Byte)link[i].l_pop_queue()).byteValue();
                } else {
                  link[i].l_push_queue(Byte.valueOf(tmp2[var18 - 1]));
                  tmp2[var18 - 1] = 32;
                }
              }
            }
          } else {
            tmp2 = new byte[]{(byte)32};
          }

          if(k == min) {
            buffer.write(new byte[]{(byte)27, (byte)51, (byte)40});
          }

          switch(colsAlign[i]) {
          case 1:
            buffer.write(setCusorPosition((e + (colsWidth[i] - tmp2.length) / 2) * charWidth + columnPadding * i));
            break;
          case 2:
            buffer.write(setCusorPosition((e + colsWidth[i] - tmp2.length) * charWidth + columnPadding * i));
            break;
          default:
            buffer.write(setCusorPosition(e * charWidth + columnPadding * i));
          }

          buffer.write(tmp2);
          e += colsWidth[i];
        }

        buffer.write(10);
        buffer.write(new byte[]{(byte)27, (byte)50});
      } catch (Exception var17) {
        var17.printStackTrace();
      }
    }

    return buffer.toByteArray();
  }

  public static byte[] getPrintBarCode(String data, int symbology, int height, int width, int textposition) {
    if(symbology >= 0 && symbology <= 8) {
      if(width < 2 || width > 6) {
        width = 2;
      }

      if(textposition < 0 || textposition > 3) {
        textposition = 0;
      }

      if(height < 1 || height > 255) {
        height = 162;
      }

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      try {
        buffer.write(new byte[]{(byte)29, (byte)102, (byte)1, (byte)29, (byte)72, (byte)textposition, (byte)29, (byte)119, (byte)width, (byte)29, (byte)104, (byte)height, (byte)10});
        byte[] e = data.getBytes();
        if(symbology == 8) {
          buffer.write(new byte[]{(byte)29, (byte)107, (byte)73, (byte)(e.length + 2), (byte)123, (byte)66});
        } else {
          buffer.write(new byte[]{(byte)29, (byte)107, (byte)(symbology + 65), (byte)e.length});
        }

        buffer.write(e);
      } catch (Exception var7) {
        var7.printStackTrace();
      }

      return buffer.toByteArray();
    } else {
      return new byte[]{(byte)10};
    }
  }

  private static byte[] setQRCodeSize(int modulesize) {
    byte[] dtmp = new byte[]{(byte)29, (byte)40, (byte)107, (byte)3, (byte)0, (byte)49, (byte)67, (byte)modulesize};
    return dtmp;
  }

  private static byte[] setQRCodeErrorLevel(int errorlevel) {
    byte[] dtmp = new byte[]{(byte)29, (byte)40, (byte)107, (byte)3, (byte)0, (byte)49, (byte)69, (byte)(48 + errorlevel)};
    return dtmp;
  }

  private static byte[] getBytesForPrintQRCode(boolean single) {
    byte[] dtmp;
    if(single) {
      dtmp = new byte[9];
      dtmp[8] = 10;
    } else {
      dtmp = new byte[8];
    }

    dtmp[0] = 29;
    dtmp[1] = 40;
    dtmp[2] = 107;
    dtmp[3] = 3;
    dtmp[4] = 0;
    dtmp[5] = 49;
    dtmp[6] = 81;
    dtmp[7] = 48;
    return dtmp;
  }

  private static byte[] getQCodeBytes(String code) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    try {
      byte[] e = getGbk(code);
      int len = e.length + 3;
      if(len > 7092) {
        len = 7092;
      }

      buffer.write(29);
      buffer.write(40);
      buffer.write(107);
      buffer.write((byte)len);
      buffer.write((byte)(len >> 8));
      buffer.write(49);
      buffer.write(80);
      buffer.write(48);

      for(int i = 0; i < e.length && i < len; ++i) {
        buffer.write(e[i]);
      }
    } catch (Exception var5) {
      var5.printStackTrace();
    }

    return buffer.toByteArray();
  }

  private static byte getBitMatrixColor(BitMatrix bits, int x, int y) {
    int width = bits.getWidth();
    int height = bits.getHeight();
    return (byte)(x < width && y < height && x >= 0 && y >= 0?(bits.get(x, y)?1:0):0);
  }

  public static byte[] getBytesFromBitMatrix(BitMatrix bits) {
    if(bits == null) {
      return null;
    } else {
      int h = bits.getHeight();
      int w = (bits.getWidth() + 7) / 8;
      byte[] rv = new byte[h * w + 8];
      rv[0] = 29;
      rv[1] = 118;
      rv[2] = 48;
      rv[3] = 0;
      rv[4] = (byte)w;
      rv[5] = (byte)(w >> 8);
      rv[6] = (byte)h;
      rv[7] = (byte)(h >> 8);
      int k = 8;

      for(int i = 0; i < h; ++i) {
        for(int j = 0; j < w; ++j) {
          for(int n = 0; n < 8; ++n) {
            byte b = getBitMatrixColor(bits, j * 8 + n, i);
            rv[k] = (byte)(rv[k] + rv[k] + b);
          }

          ++k;
        }
      }

      return rv;
    }
  }

  public static byte[] getZXingQRCode(String data, int size) {
    try {
      Hashtable e = new Hashtable();
      e.put(EncodeHintType.CHARACTER_SET, "utf-8");
      BitMatrix bitMatrix = (new QRCodeWriter()).encode(data, BarcodeFormat.QR_CODE, size, size, e);
      return getBytesFromBitMatrix(bitMatrix);
    } catch (WriterException var4) {
      var4.printStackTrace();
      return null;
    }
  }

  public static byte[] getZXingBarCode(String data, BarcodeFormat format, int width, int height) {
    try {
      Hashtable e = new Hashtable();
      e.put(EncodeHintType.CHARACTER_SET, "utf-8");
      BitMatrix bits = null;
      bits = (new MultiFormatWriter()).encode(data, format, width, height, e);
      return getBytesFromBitMatrix(bits);
    } catch (Exception var6) {
      var6.printStackTrace();
      return null;
    }
  }

  public static byte[] getTypeHorizontalLine(int w, int type) {
    if(type > 11 || type < 0) {
      type = 1;
    }

    byte[][] kk = new byte[][]{{(byte)0, (byte)0, (byte)124, (byte)124, (byte)124, (byte)0, (byte)0}, {(byte)0, (byte)0, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)0}, {(byte)0, (byte)68, (byte)68, (byte)-1, (byte)68, (byte)68, (byte)0}, {(byte)0, (byte)34, (byte)85, (byte)-120, (byte)85, (byte)34, (byte)0}, {(byte)8, (byte)8, (byte)28, (byte)127, (byte)28, (byte)8, (byte)8}, {(byte)8, (byte)20, (byte)34, (byte)65, (byte)34, (byte)20, (byte)8}, {(byte)8, (byte)20, (byte)42, (byte)85, (byte)42, (byte)20, (byte)8}, {(byte)8, (byte)28, (byte)62, (byte)127, (byte)62, (byte)28, (byte)8}, {(byte)73, (byte)34, (byte)20, (byte)73, (byte)20, (byte)34, (byte)73}, {(byte)99, (byte)119, (byte)62, (byte)28, (byte)62, (byte)119, (byte)99}, {(byte)112, (byte)32, (byte)-81, (byte)-86, (byte)-6, (byte)2, (byte)7}, {(byte)-17, (byte)40, (byte)-18, (byte)-86, (byte)-18, (byte)-126, (byte)-2}};
    int ww = (w + 7) / 8;
    byte[] data = new byte[13 * ww + 8];
    data[0] = 29;
    data[1] = 118;
    data[2] = 48;
    data[3] = 0;
    data[4] = (byte)ww;
    data[5] = (byte)(ww >> 8);
    data[6] = 13;
    data[7] = 0;
    int k = 8;

    int i;
    for(i = 0; i < 3 * ww; ++i) {
      data[k++] = 0;
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][0];
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][1];
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][2];
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][3];
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][4];
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][5];
    }

    for(i = 0; i < ww; ++i) {
      data[k++] = kk[type][6];
    }

    for(i = 0; i < 3 * ww; ++i) {
      data[k++] = 0;
    }

    return data;
  }

  public static byte[] getHorizontalLine(int w, int size, int type) {
    int ww = (w + 7) / 8;
    int hh = size + 6;
    byte kk = -1;
    if(type == 0) {
      kk = 63;
    }

    byte[] data = new byte[hh * ww + 8];
    data[0] = 29;
    data[1] = 118;
    data[2] = 48;
    data[3] = 0;
    data[4] = (byte)ww;
    data[5] = (byte)(ww >> 8);
    data[6] = (byte)hh;
    data[7] = (byte)(hh >> 8);
    int k = 8;

    int i;
    for(i = 0; i < 3 * ww; ++i) {
      data[k++] = 0;
    }

    for(i = 0; i < size; ++i) {
      for(int i1 = 0; i1 < ww; ++i1) {
        data[k++] = kk;
      }
    }

    for(i = 0; i < 3 * ww; ++i) {
      data[k++] = 0;
    }

    return data;
  }
}
