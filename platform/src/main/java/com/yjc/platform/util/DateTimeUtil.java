package com.yjc.platform.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil extends DateUtils {
    public static final String PART_DATA_TIME = "yyyyMMdd";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String getFormatDate(Date date,String format){
        date = date==null?new Date():date;
        format = StringUtils.isBlank(format)?FULL_DATE_FORMAT:format;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format((date));
    }
}
