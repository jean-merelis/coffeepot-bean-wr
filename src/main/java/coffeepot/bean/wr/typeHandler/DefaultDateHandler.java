/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultDateHandler implements TypeHandler<Date> {

    protected SimpleDateFormat dateFormat;
    protected String patternForDate;
    protected String patternForTime;

    public DefaultDateHandler() {
        patternForDate = "yyyy-MM-dd";
        patternForTime = "HH:mm:ss";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    @Override
    public Date parse(String text) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }
        Date d;
        try {
            d = dateFormat.parse(text);
            return d;
        } catch (Exception ex) {
            throw new HandlerParseException(ex.getMessage());
        }

    }

    @Override
    public String toString(Date obj) {
        if (obj == null) {
            return null;
        }
        return dateFormat.format(obj);
    }

    @Override
    public void setConfig(String[] params) {
        if (params == null || params.length == 0) {
            setDefaultValues();
            return;
        }
        for (String s : params) {
            s = s.replace("pattern=", "");
            switch (s) {
                case "date":
                    dateFormat = new SimpleDateFormat(patternForDate);
                    break;
                case "time":
                    dateFormat = new SimpleDateFormat(patternForTime);
                    break;
                default:
                    dateFormat = new SimpleDateFormat(s);
            }
        }
    }

    protected void setDefaultValues() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
    }
}
