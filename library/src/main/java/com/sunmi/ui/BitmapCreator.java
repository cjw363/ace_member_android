
package com.sunmi.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.sunmi.util.PicFromPrintUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import woyou.aidlservice.jiuiv5.ICallback;

public class BitmapCreator {
  private static final byte ESC = 27;
  private static final byte FS = 28;
  private static final byte GS = 29;
  private static final byte DLE = 16;
  private int canvasWidth;
  private PrinterSet currentPrinterSet;
  private RawPrintInterface rawPrinter;
  private float xLeftPadding;
  private float printWidth;
  private PrinterSet defaultPrinterSet;
  private ArrayList<CharWithPos> chars;
  private PointF currentPos;
  private Context context;
  private byte[] testData = null;
  private Bitmap bitmap;
  private Canvas canvas;
  private Typeface typeface = null;

  public BitmapCreator(int canvasWidth, RawPrintInterface rawPrinter, Context context) {
    this.context = context;
    this.canvasWidth = canvasWidth;
    this.currentPrinterSet = new PrinterSet((float)canvasWidth, context, this.typeface);
    this.defaultPrinterSet = new PrinterSet((float)canvasWidth, context, this.typeface);
    this.chars = new ArrayList();
    this.currentPos = new PointF(this.currentPrinterSet.xLeftPadding, this.currentPrinterSet.lineHight);
    this.rawPrinter = rawPrinter;
    this.xLeftPadding = this.currentPrinterSet.xLeftPadding;
    this.printWidth = this.currentPrinterSet.printWidth;
  }

  public boolean setFontName(String fontname) {
    return this.currentPrinterSet.setTypeface(fontname);
  }

  public boolean setFontName(Typeface typeface) {
    return this.currentPrinterSet.setTypeface(typeface);
  }

  public boolean setFontSize(float size) {
    return this.currentPrinterSet.setTextSize(size);
  }

  public float getFontSize() {
    return this.currentPrinterSet.printCharWidth;
  }

  public void setPrinterDefault(ICallback callback) {
    this.currentPrinterSet = new PrinterSet((float)this.canvasWidth, this.context, this.typeface);
    this.currentPos.y = this.defaultPrinterSet.lineHight;
    this.currentPos.x = this.defaultPrinterSet.xLeftPadding;
    this.chars.clear();
    this.xLeftPadding = this.defaultPrinterSet.xLeftPadding;
    this.printWidth = this.defaultPrinterSet.printWidth;
    this.rawPrinter.printBytes(new byte[]{(byte)27, (byte)64}, callback);
  }

  public void sendRAWData(byte[] command, ICallback callback) {
    int i = 0;
    boolean skip = false;

    while(true) {
      while(i < command.length) {
        if(this.oneBytecommand(command[i], callback)) {
          ++i;
        } else if(i < command.length - 1 && this.twoBytecommand(command[i], command[i + 1], callback)) {
          i += 2;
        } else if(i < command.length - 2 && this.threeBytecommand(command[i], command[i + 1], command[i + 2], callback)) {
          i += 3;
        } else if(i < command.length - 3 && this.fourBytecommand(command[i], command[i + 1], command[i + 2], command[i + 3])) {
          i += 4;
        } else {
          int var9;
          if((var9 = this.rasterpiccommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.realpiccommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.tabPositions(i, command)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.filtercutpapercommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.selfcheckcommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.updatevaluescommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.hexPrintModeCommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.setPrintStrengthCommand(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else if((var9 = this.printBarCode(i, command, callback)) != 0) {
            i += var9;
            skip = false;
          } else {
            String e;
            if((command[i] & 128) != 128) {
              try {
                e = new String(command, i, 1, this.currentPrinterSet.codeSystem);
                ++i;
                this.addChar(e, this.currentPrinterSet, callback);
                continue;
              } catch (UnsupportedEncodingException var8) {
                var8.printStackTrace();
              }
            } else if(i < command.length - 1 && (command[i + 1] & 255) > 57) {
              try {
                e = new String(command, i, 2, this.currentPrinterSet.codeSystem);
                i += 2;
                this.addChar(e, this.currentPrinterSet, callback);
                continue;
              } catch (UnsupportedEncodingException var7) {
                var7.printStackTrace();
              }
            } else if(i < command.length - 3 && (command[i + 1] & 255) < 64 && (command[i + 2] & 255) > 128 && (command[i + 3] & 255) < 64) {
              try {
                e = new String(command, i, 4, this.currentPrinterSet.codeSystem);
                i += 4;
                this.addChar(e, this.currentPrinterSet, callback);
                continue;
              } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
              }
            }

            ++i;
          }
        }
      }

      return;
    }
  }

  public void printText(String s, ICallback callback) {
    this.addChar(s, this.currentPrinterSet, callback);
  }

  public void printOriginalText(String s, ICallback callback) {
    this.addOriginalChar(s, this.currentPrinterSet, callback);
  }

  public void printText(String s, String fontname, float size, ICallback callback) {
    new PrinterSet((float)this.canvasWidth, this.context, this.typeface);
    Typeface temp1 = this.currentPrinterSet.textFont;
    float temp2 = this.currentPrinterSet.getTextSize();
    this.currentPrinterSet.setTypeface(fontname);
    this.currentPrinterSet.setTextSize(size);
    this.addChar(s, this.currentPrinterSet, callback);
    this.currentPrinterSet.setTypeface(temp1);
    this.currentPrinterSet.setTextSize(temp2);
  }

  private void addChar(String s, PrinterSet printerSet, ICallback callback) {
    char[] temp = s.toCharArray();

    for(int i = 0; i < temp.length; ++i) {
      if(temp[i] == 10) {
        this.print(callback);
      } else if(temp[i] != 0) {
        float printCharWidth = printerSet.printCharWidth;
        float printCharHeight = printerSet.printCharHeight;
        if(temp[i] < 128) {
          printCharWidth /= 2.0F;
        } else if(!printerSet.isTextTimes) {
          printCharWidth = printerSet.getTextSize();
          printCharHeight = printerSet.getTextSize();
        }

        if(this.currentPos.y < printCharHeight) {
          this.currentPos.y = printCharHeight;
        }

        if(this.currentPos.x + printCharWidth + printerSet.xWorldLeftPadding > printerSet.xLeftPadding + printerSet.printWidth) {
          this.print(callback);
          if(this.currentPos.y < printCharHeight) {
            this.currentPos.y = printCharHeight;
          }
        }

        CharWithPos charWithPos = new CharWithPos();
        charWithPos.char_bitmap = printerSet.char_bitmap;
        charWithPos.char_canvas = printerSet.char_canvas;
        charWithPos.x = this.currentPos.x;
        this.currentPos.x += printCharWidth + printerSet.xWorldLeftPadding + printerSet.xWorldRigthPadding;
        charWithPos.offsetY = Math.abs(printerSet.textPaint.getFontMetrics().descent);
        charWithPos.paint = printerSet.textPaint;
        charWithPos.char_ = temp[i];
        charWithPos.reversePrintingMode = printerSet.reversePrintingMode;
        charWithPos.width = printerSet.textPaint.measureText(temp, i, 1);
        charWithPos.height = printerSet.textHeight;
        charWithPos.printCharHeight = printCharHeight;
        charWithPos.printCharWidth = printCharWidth;
        charWithPos.currentTimes = printerSet.currentTimes;
        this.chars.add(charWithPos);
      }
    }

  }

  private void addOriginalChar(String s, PrinterSet printerSet, ICallback callback) {
    char[] temp = s.toCharArray();

    for(int i = 0; i < temp.length; ++i) {
      if(temp[i] == 10) {
        this.print(callback);
      } else if(temp[i] != 0) {
        float printCharWidth = printerSet.textPaint.measureText(temp, i, 1) * (float)printerSet.TextTimesWidth / (float)printerSet.currentTimes;
        float printCharHeight = printerSet.textHeight * (float)printerSet.TextTimesHight / (float)printerSet.currentTimes;
        if(this.currentPos.y < printCharHeight) {
          this.currentPos.y = printCharHeight;
        }

        if(this.currentPos.x + printCharWidth + printerSet.xWorldLeftPadding > printerSet.xLeftPadding + printerSet.printWidth) {
          this.print(callback);
          if(this.currentPos.y < printCharHeight) {
            this.currentPos.y = printCharHeight;
          }
        }

        CharWithPos charWithPos = new CharWithPos();
        charWithPos.x = this.currentPos.x;
        this.currentPos.x += printCharWidth + printerSet.xWorldLeftPadding + printerSet.xWorldRigthPadding;
        charWithPos.offsetY = Math.abs(printerSet.textPaint.getFontMetrics().descent);
        charWithPos.paint = printerSet.textPaint;
        charWithPos.char_ = temp[i];
        charWithPos.reversePrintingMode = printerSet.reversePrintingMode;
        charWithPos.width = printerSet.textPaint.measureText(temp, i, 1);
        charWithPos.height = printerSet.textHeight;
        charWithPos.printCharHeight = printCharHeight;
        charWithPos.printCharWidth = printCharWidth;
        charWithPos.currentTimes = printerSet.currentTimes;
        charWithPos.char_bitmap = Bitmap.createBitmap((int)charWithPos.width, (int)charWithPos.height, Config.ARGB_8888);
        charWithPos.char_canvas = new Canvas(charWithPos.char_bitmap);
        this.chars.add(charWithPos);
      }
    }

  }

  private void Tab(ICallback callback) {
    float tabWidth = 96.0F;
    float gh = 0.0F;

    while(gh < this.currentPos.x) {
      gh += tabWidth;
      if(gh >= this.currentPrinterSet.xLeftPadding + this.currentPrinterSet.printWidth) {
        gh = this.currentPrinterSet.xLeftPadding + this.currentPrinterSet.printWidth;
        break;
      }
    }

    this.currentPos.x = gh;
    if(this.currentPos.x == this.currentPrinterSet.xLeftPadding + this.currentPrinterSet.printWidth) {
      this.print(callback);
    }

  }

  private void print(float gh, ICallback callback) {
    if(this.bitmap == null || this.bitmap.getHeight() != (int)(this.currentPos.y + gh)) {
      this.bitmap = Bitmap.createBitmap(this.canvasWidth, (int)(this.currentPos.y + gh), Config.ARGB_8888);
      this.canvas = new Canvas(this.bitmap);
      this.canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
    }

    this.canvas.drawColor(-1);
    float offSet = 0.0F;
    if(this.chars.size() != 0) {
      if(this.currentPrinterSet.justificationMode == 0) {
        offSet = this.currentPrinterSet.xLeftPadding;
      } else if(this.currentPrinterSet.justificationMode == 1) {
        offSet = (this.currentPrinterSet.printWidth - (((CharWithPos)this.chars.get(this.chars.size() - 1)).x + ((CharWithPos)this.chars.get(this.chars.size() - 1)).printCharWidth - ((CharWithPos)this.chars.get(0)).x)) / 2.0F + this.currentPrinterSet.xLeftPadding;
      } else if(this.currentPrinterSet.justificationMode == 2) {
        offSet = this.currentPrinterSet.printWidth - (((CharWithPos)this.chars.get(this.chars.size() - 1)).x + ((CharWithPos)this.chars.get(this.chars.size() - 1)).printCharWidth - ((CharWithPos)this.chars.get(0)).x) + this.currentPrinterSet.xLeftPadding;
      }
    }

    Iterator var5 = this.chars.iterator();

    while(var5.hasNext()) {
      CharWithPos charWithPos = (CharWithPos)var5.next();
      if(!charWithPos.isBitmap) {
        if(!charWithPos.reversePrintingMode) {
          charWithPos.char_canvas.drawColor(-1);
          charWithPos.char_canvas.drawText(new char[]{charWithPos.char_}, 0, 1, ((float)charWithPos.char_bitmap.getWidth() - charWithPos.width) / 2.0F, charWithPos.height - charWithPos.offsetY, charWithPos.paint);
        } else {
          charWithPos.char_canvas.drawColor(-16777216);
          charWithPos.paint.setColor(-1);
          charWithPos.char_canvas.drawText(new char[]{charWithPos.char_}, 0, 1, ((float)charWithPos.char_bitmap.getWidth() - charWithPos.width) / 2.0F, charWithPos.height - charWithPos.offsetY, charWithPos.paint);
          charWithPos.paint.setColor(-16777216);
        }
      }

      if(charWithPos.width > charWithPos.printCharWidth) {
        this.canvas.drawBitmap(charWithPos.char_bitmap, new Rect((int)(((float)charWithPos.char_bitmap.getWidth() - charWithPos.width) / 2.0F), 0, (int)(((float)charWithPos.char_bitmap.getWidth() + charWithPos.width) / 2.0F), charWithPos.char_bitmap.getHeight()), new RectF(offSet + charWithPos.x, this.currentPos.y - charWithPos.printCharHeight, offSet + charWithPos.x + charWithPos.printCharWidth, this.currentPos.y), charWithPos.paint);
      } else {
        this.canvas.drawBitmap(charWithPos.char_bitmap, new Rect((int)(((float)charWithPos.char_bitmap.getWidth() - charWithPos.printCharWidth) / 2.0F), 0, (int)(((float)charWithPos.char_bitmap.getWidth() + charWithPos.printCharWidth) / 2.0F), charWithPos.char_bitmap.getHeight()), new RectF(offSet + charWithPos.x, this.currentPos.y - charWithPos.printCharHeight, offSet + charWithPos.x + charWithPos.printCharWidth, this.currentPos.y), charWithPos.paint);
      }
    }

    this.chars.clear();
    this.currentPrinterSet.xLeftPadding = this.xLeftPadding;
    this.currentPrinterSet.printWidth = this.printWidth;
    this.currentPos.y = this.currentPrinterSet.lineHight;
    this.currentPos.x = this.currentPrinterSet.xLeftPadding;
    this.testData = PicFromPrintUtils.rasterDataFromBitmap_gh(this.bitmap);
    this.rawPrinter.printBytes(this.testData, callback);
  }

  private void print(ICallback callback) {
    this.print(0.0F, callback);
  }

  private void setDefaultLineHeight() {
    this.currentPrinterSet.lineHight = 30.0F;
  }

  private boolean oneBytecommand(byte command, ICallback callback) {
    switch(command) {
    case 9:
      this.Tab(callback);
      return true;
    case 10:
      this.print(callback);
      return true;
    default:
      return false;
    }
  }

  private boolean twoBytecommand(byte command1, byte command2, ICallback callback) {
    switch(command1) {
    case 27:
      switch(command2) {
      case 38:
        return true;
      case 50:
        this.setDefaultLineHeight();
        return true;
      case 64:
        this.setPrinterDefault(callback);
        return true;
      default:
        return false;
      }
    case 28:
      switch(command2) {
      case 46:
        return true;
      default:
        return false;
      }
    case 29:
      return false;
    default:
      return false;
    }
  }

  private void setxWorldRigthPadding(byte command) {
    this.currentPrinterSet.xWorldRigthPadding = (float)command;
  }

  private void setPrintMode(byte command) {
    if((command & 8) == 8) {
      this.currentPrinterSet.isFakeBoldText = true;
    } else {
      this.currentPrinterSet.isFakeBoldText = false;
    }

    if((command & 16) == 16) {
      this.currentPrinterSet.TextTimesHight = 2;
    } else {
      this.currentPrinterSet.TextTimesHight = 1;
    }

    if((command & 32) == 32) {
      this.currentPrinterSet.TextTimesWidth = 2;
    } else {
      this.currentPrinterSet.TextTimesWidth = 1;
    }

    if((command & 128) == 128) {
      this.currentPrinterSet.isUnderlineText = true;
    } else {
      this.currentPrinterSet.isUnderlineText = false;
    }

    this.currentPrinterSet.isTextTimes = true;
    this.currentPrinterSet.refresh();
  }

  private void setIsUnderline(byte command) {
    if(command != 0 && command != 48) {
      this.currentPrinterSet.isUnderlineText = true;
    } else {
      this.currentPrinterSet.isUnderlineText = false;
    }

    this.currentPrinterSet.refresh();
  }

  private void setLineHeight(byte command) {
    this.currentPrinterSet.lineHight = (float)command;
  }

  private void setisFakeBoldText(byte command) {
    if((command & 1) == 1) {
      this.currentPrinterSet.isFakeBoldText = true;
    } else {
      this.currentPrinterSet.isFakeBoldText = false;
    }

    this.currentPrinterSet.refresh();
  }

  private void setJustificationMode(byte command) {
    if(command != 0 && command != 48) {
      if(command != 1 && command != 49) {
        if(command == 2 || command == 50) {
          this.currentPrinterSet.justificationMode = 2;
        }
      } else {
        this.currentPrinterSet.justificationMode = 1;
      }
    } else {
      this.currentPrinterSet.justificationMode = 0;
    }

  }

  private boolean threeBytecommand(byte command1, byte command2, byte command3, ICallback callback) {
    switch(command1) {
    case 16:
      if(command2 == 4) {
        this.rawPrinter.printBytes(new byte[]{(byte)16, (byte)4, command3}, callback);
        return true;
      }

      return false;
    case 27:
      switch(command2) {
      case 32:
        this.setxWorldRigthPadding(command3);
        return true;
      case 33:
        this.setPrintMode(command3);
        return true;
      case 45:
        this.setIsUnderline(command3);
        return true;
      case 51:
        this.setLineHeight(command3);
        return true;
      case 69:
        this.setisFakeBoldText(command3);
        return true;
      case 71:
        this.setisFakeBoldText(command3);
        return true;
      case 74:
        this.print((float)command3, callback);
        return true;
      case 77:
        return true;
      case 82:
        return true;
      case 97:
        this.setJustificationMode(command3);
        this.rawPrinter.printBytes(new byte[]{(byte)27, (byte)97, command3}, callback);
        return true;
      case 100:
        this.print((float)command3 * this.currentPrinterSet.lineHight, callback);
        return true;
      default:
        return false;
      }
    case 28:
      switch(command2) {
      case 33:
        this.setPrintMode(command3);
        return true;
      case 67:
        this.setCodeSystem(command3);
        return true;
      case 87:
        this.setTextMode(command3);
        return true;
      default:
        return false;
      }
    case 29:
      switch(command2) {
      case 33:
        this.setTextSize(command3);
        return true;
      case 66:
        this.setReversePrintingMode(command3);
        return true;
      case 72:
        this.sendToPrinter(new byte[]{command1, command2, command3}, 0, 3, callback);
        return true;
      case 73:
        this.rawPrinter.printBytes(new byte[]{(byte)29, (byte)73, command3}, callback);
        return true;
      case 102:
        this.sendToPrinter(new byte[]{command1, command2, command3}, 0, 3, callback);
        return true;
      case 104:
        this.sendToPrinter(new byte[]{command1, command2, command3}, 0, 3, callback);
        return true;
      case 119:
        this.sendToPrinter(new byte[]{command1, command2, command3}, 0, 3, callback);
        return true;
      default:
        return false;
      }
    default:
      return false;
    }
  }

  public void setTextMode(byte command) {
    if((command & 1) == 1) {
      this.currentPrinterSet.isTextTimes = true;
    } else {
      this.currentPrinterSet.isTextTimes = false;
    }

  }

  private void setReversePrintingMode(byte command) {
    if((command & 1) == 1) {
      this.currentPrinterSet.reversePrintingMode = true;
    } else {
      this.currentPrinterSet.reversePrintingMode = false;
    }

  }

  private void setTextSize(byte command) {
    int sizeH = (command & 240) / 16 + 1;
    int sizeL = (command & 15) + 1;
    this.currentPrinterSet.TextTimesHight = sizeL;
    this.currentPrinterSet.TextTimesWidth = sizeH;
    this.currentPrinterSet.isTextTimes = true;
    this.currentPrinterSet.refresh();
  }

  private void setxLeftPadding(byte command1, byte command2) {
    int offset = (command2 & 255) << 8 | command1 & 255;
    if(offset < this.canvasWidth) {
      this.xLeftPadding = (float)offset;
    } else {
      this.xLeftPadding = (float)this.canvasWidth;
    }

    if((float)this.canvasWidth - this.xLeftPadding < this.printWidth) {
      this.printWidth = (float)this.canvasWidth - this.xLeftPadding;
    }

  }

  private void setxPrintWidth(byte command1, byte command2) {
    int offset = (command2 & 255) << 8 | command1 & 255;
    if(offset < this.canvasWidth) {
      this.printWidth = (float)offset;
    } else {
      this.printWidth = (float)this.canvasWidth;
    }

    if((float)this.canvasWidth - this.xLeftPadding < this.printWidth) {
      this.xLeftPadding = (float)this.canvasWidth - this.printWidth;
    }

  }

  private void setCurrentX1(byte command1, byte command2) {
    int detaX = (command2 & 255) << 8 | command1 & 255;
    if(detaX < this.canvasWidth) {
      this.currentPos.x = (float)detaX;
    } else {
      this.currentPos.x = (float)this.canvasWidth;
    }

    this.currentPrinterSet.justificationMode = 0;
  }

  public void setCodeSystem(byte command) {
    if(command == 0 || command == 72) {
      this.currentPrinterSet.codeSystem = "GB18030";
    }

    if(command == 1 || command == 73) {
      this.currentPrinterSet.codeSystem = "BIG5";
    }

    if(command == 2 || command == 80) {
      this.currentPrinterSet.codeSystem = "KSC5601";
    }

  }

  private void setCurrentX2(byte command1, byte command2) {
    int detaX = (command2 & 255) << 8 | command1 & 255;
    if(this.currentPos.x + (float)detaX < (float)this.canvasWidth) {
      this.currentPos.x += (float)detaX;
    } else {
      this.currentPos.x = (float)this.canvasWidth;
    }

  }

  private void setxWorldLeftRightPadding(byte command1, byte command2) {
    this.currentPrinterSet.xWorldLeftPadding = (float)command1;
    this.currentPrinterSet.xWorldRigthPadding = (float)command2;
  }

  private boolean fourBytecommand(byte command1, byte command2, byte command3, byte command4) {
    switch(command1) {
    case 27:
      switch(command2) {
      case 36:
        this.setCurrentX1(command3, command4);
        return true;
      case 92:
        this.setCurrentX2(command3, command4);
        return true;
      default:
        return false;
      }
    case 28:
      switch(command2) {
      case 83:
        this.setxWorldLeftRightPadding(command3, command4);
        return true;
      default:
        return false;
      }
    case 29:
      switch(command2) {
      case 76:
        this.setxLeftPadding(command3, command4);
        return true;
      case 87:
        this.setxPrintWidth(command3, command4);
        return true;
      default:
        return false;
      }
    default:
      return false;
    }
  }

  private int selfcheckcommand(int i, byte[] command, ICallback callback) {
    try {
      if(command[i] == 31 && command[i + 1] == 27 && command[i + 2] == 31 && command[i + 3] == 83) {
        this.rawPrinter.printBytes(new byte[]{(byte)31, (byte)27, (byte)31, (byte)83}, callback);
        return 4;
      } else {
        return 0;
      }
    } catch (Exception var5) {
      return 0;
    }
  }

  private int filtercutpapercommand(int i, byte[] command, ICallback callback) {
    try {
      return command[i] == 29 && command[i + 1] == 86?(command[i + 2] == 66?4:3):0;
    } catch (Exception var5) {
      return 0;
    }
  }

  private int updatevaluescommand(int i, byte[] command, ICallback callback) {
    try {
      if(command[i] == 31 && command[i + 1] == 27 && command[i + 2] == 31 && command[i + 3] == 114) {
        byte[] data = new byte[11];
        System.arraycopy(command, i, data, 0, 11);
        this.rawPrinter.printBytes(data, callback);
        return 11;
      } else {
        return 0;
      }
    } catch (Exception var5) {
      return 0;
    }
  }

  private int printBarCode(int i, byte[] command, ICallback callback) {
    try {
      int j;
      byte[] data;
      if(command[i] == 29 && command[i + 1] == 107 && command[i + 2] < 6) {
        for(j = 0; command[j + 3] != 0; ++j) {
          ;
        }

        ++j;
        data = new byte[j + 3];

        for(int z = 0; z < j + 3; ++z) {
          data[z] = command[z + i];
        }

        this.rawPrinter.printBytes(data, callback);
        return j + 3;
      }

      if(command[i] == 29 && command[i + 1] == 107 && command[i + 2] > 6) {
        j = 0;

        for(data = new byte[(command[i + 3] & 255) + 4]; j < data.length; ++j) {
          data[j] = command[j + i];
        }

        this.rawPrinter.printBytes(data, callback);
        return j + 4;
      }
    } catch (Exception var7) {
      ;
    }

    return 0;
  }

  private int realpiccommand(int i, byte[] command, ICallback callback) {
    try {
      if(command[i] == 27 && command[i + 1] == 42) {
        int skip = 0;
        if(command[i + 2] != 0 && command[i + 2] != 1) {
          if(command[i + 2] == 32 || command[i + 2] == 33) {
            skip = ((command[i + 4] & 255) << 8 | command[i + 3] & 255) * 3;
          }
        } else {
          skip = (command[i + 4] & 255) << 8 | command[i + 3] & 255;
        }

        skip += 5;
        byte[] data = new byte[skip];
        System.arraycopy(command, i, data, 0, skip);
        this.rawPrinter.printBytes(data, callback);
        return skip;
      } else {
        return 0;
      }
    } catch (Exception var6) {
      return 0;
    }
  }

  private int hexPrintModeCommand(int i, byte[] command, ICallback callback) {
    try {
      if(command[i] == 29 && command[i + 1] == 40 && command[i + 2] == 65) {
        byte[] data = new byte[7];
        System.arraycopy(command, i, data, 0, 7);
        this.rawPrinter.printBytes(data, callback);
        return 7;
      } else {
        return 0;
      }
    } catch (Exception var5) {
      return 0;
    }
  }

  private int setPrintStrengthCommand(int i, byte[] command, ICallback callback) {
    try {
      if(command[i] == 29 && command[i + 1] == 40 && command[i + 2] == 69) {
        byte[] data = new byte[9];
        System.arraycopy(command, i, data, 0, 9);
        this.rawPrinter.printBytes(data, callback);
        return 9;
      } else {
        return 0;
      }
    } catch (Exception var5) {
      return 0;
    }
  }

  private void sendToPrinter(byte[] command, int offset, int count, ICallback callback) {
    byte[] data = new byte[count];
    System.arraycopy(command, offset, data, 0, count);
    this.rawPrinter.printBytes(data, callback);
  }

  private int rasterpiccommand(int i, byte[] command, ICallback callback) {
    try {
      if(command[i] == 29 && command[i + 1] == 118) {
        int w = (command[i + 5] & 255) << 8 | command[i + 4] & 255;
        int h = (command[i + 7] & 255) << 8 | command[i + 6] & 255;
        int skip = w * h + 8;
        byte[] data = new byte[skip];
        System.arraycopy(command, i, data, 0, skip);
        this.rawPrinter.printBytes(data, callback);
        return skip;
      } else {
        return 0;
      }
    } catch (Exception var8) {
      return 0;
    }
  }

  private int tabPositions(int i, byte[] command) {
    try {
      if(command[i] == 27 && command[i + 1] == 68) {
        int skip;
        for(skip = 0; command[i + 2 + skip] != 0; ++skip) {
          ;
        }

        return 3 + skip;
      } else {
        return 0;
      }
    } catch (Exception var4) {
      return 0;
    }
  }

  private void addBitmap(Bitmap bitmap, PrinterSet printerSet, ICallback callback) {
    if(bitmap != null) {
      float printCharWidth = (float)bitmap.getWidth();
      float printCharHeight = (float)bitmap.getHeight();
      if(this.currentPos.y < printCharHeight) {
        this.currentPos.y = printCharHeight;
      }

      if(this.currentPos.x + printCharWidth + printerSet.xWorldLeftPadding > printerSet.xLeftPadding + printerSet.printWidth) {
        this.print(callback);
        if(this.currentPos.y < printCharHeight) {
          this.currentPos.y = printCharHeight;
        }
      }

      CharWithPos charWithPos = new CharWithPos();
      charWithPos.char_bitmap = bitmap;
      Bitmap targetBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
      charWithPos.char_canvas = new Canvas(targetBmp);
      charWithPos.x = this.currentPos.x;
      this.currentPos.x += printCharWidth + printerSet.xWorldLeftPadding + printerSet.xWorldRigthPadding;
      charWithPos.offsetY = 0.0F;
      charWithPos.paint = printerSet.textPaint;
      charWithPos.char_ = 0;
      charWithPos.reversePrintingMode = printerSet.reversePrintingMode;
      charWithPos.width = printCharWidth;
      charWithPos.height = printCharHeight;
      charWithPos.printCharHeight = printCharHeight;
      charWithPos.printCharWidth = printCharWidth;
      charWithPos.currentTimes = 1;
      this.chars.add(charWithPos);
    }
  }

  public void printBitmap(Bitmap bitmap, ICallback callback) {
    this.addBitmap(bitmap, this.currentPrinterSet, callback);
  }

  class CharWithPos {
    public char char_;
    public float x;
    public Paint paint;
    public float offsetY;
    public boolean reversePrintingMode;
    public float width;
    public float height;
    public float printCharWidth;
    public float printCharHeight;
    public Bitmap char_bitmap;
    public Canvas char_canvas;
    public int currentTimes;
    public boolean isBitmap = false;

    CharWithPos() {
    }
  }
}
