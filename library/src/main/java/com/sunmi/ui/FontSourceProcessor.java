
package com.sunmi.ui;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

final class FontSourceProcessor {
  private static final String TAG = "FontSourceProcessor";

  FontSourceProcessor() {
  }

  public static Typeface process(int resource, Context context) {
    InputStream sInputStream = null;
    String sOutPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

    try {
      sInputStream = context.getResources().openRawResource(resource);
    } catch (NotFoundException var8) {
      Log.e("FontSourceProcessor", "Could not find font in Resources!");
      return null;
    }

    Typeface sResTypeface;
    try {
      byte[] e = new byte[sInputStream.available()];
      BufferedOutputStream sBOutStream = new BufferedOutputStream(new FileOutputStream(sOutPath));

      while(true) {
        int l;
        if((l = sInputStream.read(e)) <= 0) {
          sBOutStream.close();
          sResTypeface = Typeface.createFromFile(sOutPath);
          (new File(sOutPath)).delete();
          break;
        }

        sBOutStream.write(e, 0, l);
      }
    } catch (IOException var9) {
      Log.e("FontSourceProcessor", "Error reading in fonts!");
      return null;
    }

    Log.d("FontSourceProcessor", "Successfully loaded font.");
    return sResTypeface;
  }

  public static Typeface process(String fontname, Context context) {
    int rid = context.getResources().getIdentifier(fontname, "raw", "woyou.aidlservice.jiuiv5");
    return rid == 0?null:process(rid, context);
  }
}
