package com.sunmi.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;

public class PrinterSet {
  private final float FONT_HEIGHT_OFFSET = 6.0F;
  private float textSize;
  public static final float default_textSize = 24.0F;
  public static final float default_lineHight = 30.0F;
  public float xLeftPadding;
  public float printWidth;
  public float xWorldLeftPadding;
  public float xWorldRigthPadding;
  public float lineHight;
  public Paint textPaint;
  public Paint backgroundPaint;
  public float textHeight;
  public Typeface textFont;
  public int justificationMode;
  public boolean isFakeBoldText;
  public boolean isUnderlineText;
  public int TextTimesHight;
  public int TextTimesWidth;
  public boolean reversePrintingMode;
  public Bitmap char_bitmap;
  public Canvas char_canvas;
  public int currentTimes;
  public float printCharWidth;
  public float printCharHeight;
  public String codeSystem;
  public boolean isTextTimes;
  private Context context;

  public PrinterSet(float printWidth, Context context, Typeface typeface) {
    this.context = context;
    this.xLeftPadding = 0.0F;
    this.printWidth = printWidth;
    this.xWorldLeftPadding = 0.0F;
    this.xWorldRigthPadding = 0.0F;
    this.textPaint = new Paint();
    this.textPaint.setAntiAlias(true);
    this.backgroundPaint = new Paint();
    this.textSize = 24.0F;
    this.textPaint.setTextSize(this.textSize);
    this.justificationMode = 0;
    this.lineHight = Math.abs(this.textPaint.getFontMetrics().ascent) + Math.abs(this.textPaint.getFontMetrics().descent);
    this.textHeight = this.lineHight;
    if(typeface != null) {
      this.textFont = typeface;
      this.textPaint.setTypeface(this.textFont);
    }

    this.backgroundPaint.setColor(-1);
    this.isFakeBoldText = false;
    this.isUnderlineText = false;
    this.TextTimesHight = 1;
    this.TextTimesWidth = 1;
    this.currentTimes = 1;
    this.reversePrintingMode = false;
    this.char_bitmap = Bitmap.createBitmap((int)this.textSize, (int)this.textHeight, Config.ARGB_8888);
    this.char_canvas = new Canvas(this.char_bitmap);
    this.char_canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
    this.printCharWidth = (float)this.TextTimesWidth * this.textSize;
    this.printCharHeight = (float)this.TextTimesHight * (this.textSize + 6.0F);
    this.codeSystem = "GB18030";
    this.isTextTimes = true;
  }

  public void refresh() {
    this.textPaint = new Paint();
    this.textPaint.setAntiAlias(true);
    this.textPaint.setTextSize(this.textSize * (float)this.currentTimes);
    if(this.textFont != null) {
      this.textPaint.setTypeface(this.textFont);
    }

    this.textPaint.setUnderlineText(this.isUnderlineText);
    this.textPaint.setFakeBoldText(this.isFakeBoldText);
    int tempTimes = Math.max(this.TextTimesWidth, this.TextTimesHight);
    if(tempTimes != this.currentTimes) {
      this.textPaint.setTextSize(this.textSize * (float)tempTimes);
      this.textHeight = Math.abs(this.textPaint.getFontMetrics().ascent) + Math.abs(this.textPaint.getFontMetrics().descent);
      this.char_bitmap = Bitmap.createBitmap((int)this.textSize * tempTimes, (int)this.textHeight, Config.ARGB_8888);
      this.char_canvas = new Canvas(this.char_bitmap);
      this.char_canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
      this.currentTimes = tempTimes;
    }

    this.printCharWidth = (float)this.TextTimesWidth * this.textSize;
    this.printCharHeight = (float)this.TextTimesHight * (this.textSize + 6.0F);
  }

  public boolean setTypeface(String fontname) {
    Typeface font = EasyFonts.getTypefaceByName(fontname, this.context);
    if(font != null) {
      this.textFont = font;
      this.refresh();
      return true;
    } else {
      return false;
    }
  }

  public boolean setTypeface(Typeface typeface) {
    this.textFont = typeface;
    this.refresh();
    return true;
  }

  public boolean setTextSize(float size) {
    this.textSize = size;
    this.textPaint = new Paint();
    this.textPaint.setAntiAlias(true);
    this.textPaint.setTextSize(this.textSize * (float)this.currentTimes);
    if(this.textFont != null) {
      this.textPaint.setTypeface(this.textFont);
    }

    this.textPaint.setUnderlineText(this.isUnderlineText);
    this.textPaint.setFakeBoldText(this.isFakeBoldText);
    this.textHeight = Math.abs(this.textPaint.getFontMetrics().ascent) + Math.abs(this.textPaint.getFontMetrics().descent);
    this.char_bitmap = Bitmap.createBitmap((int)this.textSize * this.currentTimes, (int)this.textHeight, Config.ARGB_8888);
    this.char_canvas = new Canvas(this.char_bitmap);
    this.char_canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
    this.printCharWidth = (float)this.TextTimesWidth * this.textSize;
    this.printCharHeight = (float)this.TextTimesHight * (this.textSize + 6.0F);
    return true;
  }

  public float getTextSize() {
    return this.textSize;
  }
}
