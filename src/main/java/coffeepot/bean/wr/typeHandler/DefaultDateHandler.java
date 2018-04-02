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
import coffeepot.bean.wr.mapper.Command;
import coffeepot.bean.wr.mapper.Metadata;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handler default for Date type. Parameters supported by this handler: date,
 * time, datetime, pattern or direct pattern. Multiple params are not supported.
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultDateHandler implements TypeHandler<Date> {

    protected SimpleDateFormat dateFormat;

    protected String pattern;

    protected static String patternDefault;
    protected static String patternDefaultForDate;
    protected static String patternDefaultForTime;
    protected static String patternDefaultForDateTime;

    public static final String CMD_SET_PATTERN = "setPattern";

    static {
        patternDefault = "yyyy-MM-dd'T'HH:mm:ss";
        patternDefaultForDate = "yyyy-MM-dd";
        patternDefaultForTime = "HH:mm:ss";
        patternDefaultForDateTime = patternDefault;
    }

    public DefaultDateHandler() {
        setDefaultValues();
    }

    @Override
    public Date parse(String text, Metadata metadata) throws HandlerParseException {
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
    public String toString(Date obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }
        return dateFormat.format(obj);
    }

    @Override
    public void config(Command[] commands) {
        if (commands == null || commands.length == 0) {
            setDefaultValues();
            return;
        }

        for (Command cmd : commands) {
            switch (cmd.getName()) {
                case CMD_SET_PATTERN: {
                    String arg = cmd.getArgs()[0];
                    switch (arg) {
                        case "date":
                            pattern = patternDefaultForDate;
                            break;
                        case "time":
                            pattern = patternDefaultForTime;
                            break;
                        case "datetime":
                            pattern = patternDefaultForDateTime;
                            break;
                        default:
                            pattern = arg;
                    }
                    break;
                }

                default: {
                    throw new IllegalArgumentException("Unknown command: " + cmd.getName());
                }
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

    public static String getPatternDefaultForDate() {
        return patternDefaultForDate;
    }

    public static void setPatternDefaultForDate(String patternDefaultForDate) {
        DefaultDateHandler.patternDefaultForDate = patternDefaultForDate;
    }

    public static String getPatternDefaultForTime() {
        return patternDefaultForTime;
    }

    public static void setPatternDefaultForTime(String patternDefaultForTime) {
        DefaultDateHandler.patternDefaultForTime = patternDefaultForTime;
    }

    public static String getPatternDefaultForDateTime() {
        return patternDefaultForDateTime;
    }

    public static void setPatternDefaultForDateTime(String patternDefaultForDateTime) {
        DefaultDateHandler.patternDefaultForDateTime = patternDefaultForDateTime;
    }

}
