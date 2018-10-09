
package com.sunmi.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.zxing.BarcodeFormat;
import com.sunmi.controller.IV1Printer;
import com.sunmi.ui.BitmapCreator;
import com.sunmi.ui.RawPrintInterface;
import com.sunmi.util.BytesUtil;
import com.sunmi.util.PicFromPrintUtils;
import com.sunmi.util.SingleThreadPoolManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;
import woyou.aidlservice.jiuiv5.IWoyouService.Stub;

public class V1Printer implements IV1Printer {
  private static String TAG = "V1Printer";
  private Context context;
  private IWoyouService woyouService = null;
  private ICallback woyouCallback = null;
  private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private com.sunmi.controller.ICallback callback = null;
  private static final int PRINTER_PAPER_WIDTH = 384;
  private RawPrintInterface printerface = null;
  private BitmapCreator bitmapCreator = null;
  private SingleThreadPoolManager pool = null;
  private ServiceConnection connService = new ServiceConnection() {
    public void onServiceDisconnected(ComponentName name) {
      V1Printer.this.woyouService = null;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
      V1Printer.this.woyouService = Stub.asInterface(service);
    }
  };

  private void initService() {
    Intent intent = new Intent();
    intent.setPackage("woyou.aidlservice.jiuiv5");
    intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
    this.context.startService(intent);
    this.context.bindService(intent, this.connService, Context.BIND_AUTO_CREATE);
    this.printerface = new RawPrintInterface() {
      public void printBytes(byte[] bytes, ICallback callback) {
        try {
          V1Printer.this.buffer.write(bytes);
        } catch (IOException var4) {
          var4.printStackTrace();
        }

      }
    };
    this.bitmapCreator = new BitmapCreator(384, this.printerface, this.context);
    this.pool = SingleThreadPoolManager.getInstance();
  }

  public V1Printer(Context context) {
    this.context = context;
    this.initService();
  }

  public void setCallback(final com.sunmi.controller.ICallback callback) {
    this.callback = callback;
    if(callback != null) {
      this.woyouCallback = new woyou.aidlservice.jiuiv5.ICallback.Stub() {
        public void onRunResult(boolean isSuccess) throws RemoteException {
          callback.onRunResult(isSuccess);
        }

        public void onReturnString(String result) throws RemoteException {
          callback.onReturnString(result);
        }

        public void onRaiseException(int code, String msg) throws RemoteException {
          callback.onRaiseException(code, msg);
        }
      };
    }

  }

  public void beginTransaction() {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.buffer = new ByteArrayOutputStream();
      }
    });
  }

  public void cancelTransaction() {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.buffer = new ByteArrayOutputStream();
      }
    });
  }

  public void commitTransaction() {
    this.pool.push(new Runnable() {
      public void run() {
        if(V1Printer.this.buffer.size() <= 0) {
          if(V1Printer.this.callback != null) {
            V1Printer.this.callback.onRunResult(false);
            V1Printer.this.callback.onRaiseException(-1, "no data.");
          }

        } else {
          if(V1Printer.this.woyouService != null) {
            try {
              V1Printer.this.woyouService.sendRAWData(V1Printer.this.buffer.toByteArray(), V1Printer.this.woyouCallback);
            } catch (RemoteException var2) {
              var2.printStackTrace();
              if(V1Printer.this.callback != null) {
                V1Printer.this.callback.onRunResult(false);
                V1Printer.this.callback.onRaiseException(-1, var2.toString());
              }
            }
          } else if(V1Printer.this.callback != null) {
            V1Printer.this.callback.onRunResult(false);
            V1Printer.this.callback.onRaiseException(-1, "printer offline.");
          }

        }
      }
    });
  }

  public void printerInit() {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.sendRAWData(new byte[]{(byte)27, (byte)64}, (ICallback)null);
      }
    });
  }

  public void printerSelfChecking() {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.sendRAWData(new byte[]{(byte)31, (byte)27, (byte)31, (byte)83}, (ICallback)null);
      }
    });
  }

  public String getPrinterSerialNo() {
    if(this.woyouService != null) {
      try {
        return this.woyouService.getPrinterSerialNo();
      } catch (RemoteException var2) {
        var2.printStackTrace();
      }
    }

    return null;
  }

  public String getPrinterVersion() {
    if(this.woyouService != null) {
      try {
        return this.woyouService.getPrinterVersion();
      } catch (RemoteException var2) {
        var2.printStackTrace();
      }
    }

    return null;
  }

  public String getPrinterModal() {
    if(this.woyouService != null) {
      try {
        return this.woyouService.getPrinterModal();
      } catch (RemoteException var2) {
        var2.printStackTrace();
      }
    }

    return null;
  }

  public void lineWrap(final int lines) {
    this.pool.push(new Runnable() {
      public void run() {
        byte[] data = new byte[lines];

        for(int i = 0; i < lines; ++i) {
          data[i] = 10;
        }

        V1Printer.this.bitmapCreator.sendRAWData(data, (ICallback)null);
      }
    });
  }

  public void sendRAWData(final byte[] data) {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.sendRAWData(data, (ICallback)null);
      }
    });
  }

  public void setAlignment(final int alignment) {
    this.pool.push(new Runnable() {
      public void run() {
        byte[] data = new byte[]{(byte)27, (byte)97, (byte)alignment};
        V1Printer.this.bitmapCreator.sendRAWData(data, (ICallback)null);
      }
    });
  }

  public void setFontName(final Typeface typeface) {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.setFontName(typeface);
      }
    });
  }

  public void setFontName(final String typeface) {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.setFontName(typeface);
      }
    });
  }

  public void setFontSize(final float fontsize) {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.setFontSize(fontsize);
      }
    });
  }

  public void printText(final String text) {
    if(text == null) {
      if(this.callback != null) {
        this.callback.onRunResult(false);
        this.callback.onRaiseException(-1, "nodata");
      }

    } else {
      this.pool.push(new Runnable() {
        public void run() {
          V1Printer.this.bitmapCreator.printText(text, (ICallback)null);
        }
      });
    }
  }

  public void printTextWithFont(final String text, final String typeface, final float fontsize) {
    if(text == null) {
      if(this.callback != null) {
        this.callback.onRunResult(false);
        this.callback.onRaiseException(-1, "nodata");
      }

    } else {
      this.pool.push(new Runnable() {
        public void run() {
          V1Printer.this.bitmapCreator.printText(text, typeface, fontsize, (ICallback)null);
        }
      });
    }
  }

  public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign) {
    if(colsTextArr != null && colsWidthArr != null && colsAlign != null) {
      if(colsTextArr.length == colsWidthArr.length && colsTextArr.length == colsAlign.length) {
        int k = colsTextArr.length;
        final String[] colsText = new String[k];
        final int[] colsW = new int[k];
        final int[] colsA = new int[k];
        System.arraycopy(colsTextArr, 0, colsText, 0, k);
        System.arraycopy(colsWidthArr, 0, colsW, 0, k);
        System.arraycopy(colsAlign, 0, colsA, 0, k);
        this.pool.push(new Runnable() {
          public void run() {
            float fontSize = V1Printer.this.bitmapCreator.getFontSize();
            byte[] bytes = BytesUtil.getColumnsText(colsText, colsW, colsA, (int)(fontSize / 2.0F), (int)(fontSize / 4.0F));
            V1Printer.this.bitmapCreator.sendRAWData(bytes, (ICallback)null);
          }
        });
      } else {
        if(this.callback != null) {
          this.callback.onRunResult(false);
          this.callback.onRaiseException(-1, "params invalid");
        }

      }
    } else {
      if(this.callback != null) {
        this.callback.onRunResult(false);
        this.callback.onRaiseException(-1, "params invalid");
      }

    }
  }

  public void printBitmap(final Bitmap bitmap) {
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.printBitmap(bitmap, (ICallback)null);
      }
    });
  }

  public void printBarCode(String data, BarcodeFormat format, int width, int height) {
    Bitmap bitmap = PicFromPrintUtils.createBarcodeImage(data, format, width, height);
    this.printBitmap(bitmap);
  }

  public void printQRCode(String data, int size) {
    Bitmap bm = PicFromPrintUtils.createQRImage(data, size);
    this.printBitmap(bm);
  }

  public void printDoubleQRCode(String leftUrl, String rightUrl, int size) {
    Bitmap bm1 = PicFromPrintUtils.createQRImage(leftUrl, size);
    Bitmap bm2 = PicFromPrintUtils.createQRImage(rightUrl, size);
    Bitmap bm = PicFromPrintUtils.doubleBitmap(bm1, bm2);
    this.printBitmap(bm);
  }

  public void printHorizontalLine(int size, int type) {
    short width = 384;
    final byte[] bytes = BytesUtil.getHorizontalLine(width, size, type);
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.sendRAWData(bytes, (ICallback)null);
      }
    });
  }

  public void printTypeHorizontalLine(int type) {
    short width = 384;
    final byte[] bytes = BytesUtil.getTypeHorizontalLine(width, type);
    this.pool.push(new Runnable() {
      public void run() {
        V1Printer.this.bitmapCreator.sendRAWData(bytes, (ICallback)null);
      }
    });
  }

  public void printOriginalText(final String text) {
    if(text == null) {
      if(this.callback != null) {
        this.callback.onRunResult(false);
        this.callback.onRaiseException(-1, "nodata");
      }

    } else {
      this.pool.push(new Runnable() {
        public void run() {
          V1Printer.this.bitmapCreator.printOriginalText(text, (ICallback)null);
        }
      });
    }
  }
}
