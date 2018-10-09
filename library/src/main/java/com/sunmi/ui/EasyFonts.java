
package com.sunmi.ui;

import android.content.Context;
import android.graphics.Typeface;

public final class EasyFonts {
  private EasyFonts() {
  }

  public static Typeface getTypefaceByName(String name, Context context) {
    return FontSourceProcessor.process(name, context);
  }
}
