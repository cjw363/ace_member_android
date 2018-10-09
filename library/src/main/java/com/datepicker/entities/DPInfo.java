package com.datepicker.entities;

/**
 * 日历数据实体
 * 封装日历绘制时需要的数据
 */
public class DPInfo {
    public String strG;
    public boolean isHoliday;
    public boolean isExceedToday;
    public boolean isToday, isWeekend;
    public boolean isDeferred;
    public boolean isDecorBG;
    public boolean isDecorTL, isDecorT, isDecorTR, isDecorL, isDecorR;
}