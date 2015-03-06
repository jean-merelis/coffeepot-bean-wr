/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 Jeandeson O. Merelis
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handler default for Date type.
 * Parameters supported by this handler: date, time, datetime, pattern or direct pattern.
 * Multiple params are not supported.
 * @author Jeandeson O. Merelis
 */
public class DefaultDateHandler implements TypeHandler<Date> {

    protected SimpleDateFormat dateFormat;

    protected String pattern;

    protected static String patternDefault;
    protected static String patternForDateDefault;
    protected static String patternForTimeDefault;
    protected static String patternForDateTimeDefault;

    static {
        patternDefault = "yyyy-MM-dd'T'HH:mm:ss";
        patternForDateDefault = "yyyy-MM-dd";
        patternForTimeDefault = "HH:mm:ss";
        patternForDateTimeDefault = patternDefault;
    }

    public DefaultDateHandler() {
        setDefaultValues();
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
            String[] param = s.split("=");
            String key = param[0].trim();
            String value;
            if (param.length > 1) {
                value = param[1];
            } else {
                value = key;
            }
            switch (key) {
                case "date":
                    pattern = patternForDateDefault;
                    break;
                case "time":
                    pattern = patternForTimeDefault;
                    break;
                case "datetime":
                    pattern = patternForDateTimeDefault;
                    break;
                case "pattern":
                    pattern = value;
                    break;
                default:
                    pattern = value;
            }
        }
        dateFormat = new SimpleDateFormat(pattern);
    }

    private void setDefaultValues() {
        pattern = patternDefault;
        dateFormat = new SimpleDateFormat(pattern);
    }

    public static String getPatternDefault() {
        return patternDefault;
    }

    public static void setPatternDefault(String patternDefault) {
        DefaultDateHandler.patternDefault = patternDefault;
    }

    public static String getPatternForDateDefault() {
        return patternForDateDefault;
    }

    public static void setPatternForDateDefault(String patternForDateDefault) {
        DefaultDateHandler.patternForDateDefault = patternForDateDefault;
    }

    public static String getPatternForTimeDefault() {
        return patternForTimeDefault;
    }

    public static void setPatternForTimeDefault(String patternForTimeDefault) {
        DefaultDateHandler.patternForTimeDefault = patternForTimeDefault;
    }

    public static String getPatternForDateTimeDefault() {
        return patternForDateTimeDefault;
    }

    public static void setPatternForDateTimeDefault(String patternForDateTimeDefault) {
        DefaultDateHandler.patternForDateTimeDefault = patternForDateTimeDefault;
    }

}
