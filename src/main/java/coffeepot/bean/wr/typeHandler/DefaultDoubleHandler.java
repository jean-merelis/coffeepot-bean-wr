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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultDoubleHandler implements TypeHandler<Double> {

    protected DecimalFormat decimalFormat;
    protected String pattern;
    protected char decimalSeparator;
    protected char groupingSeparator;

    protected static Locale locale = Locale.getDefault();
    protected static String patternDefault = "#0.##########";
    protected static char decimalSeparatorDefault = DecimalFormatSymbols.getInstance().getDecimalSeparator();
    protected static char groupingSeparatorDefault = DecimalFormatSymbols.getInstance().getGroupingSeparator();

    public final static String CMD_SET_PATTERN = "setPattern";
    public final static String CMD_SET_DECIMAL_SEPARATOR = "setDecimalSeparator";
    public final static String CMD_SET_GROUPING_SEPARATOR = "setGroupingSeparator";

    public DefaultDoubleHandler() {
        setDefaultValues();
    }

    @Override
    public Double parse(String text, Metadata metadata) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }
        Number d;
        try {
            d = decimalFormat.parse(text);
            return d.doubleValue();
        } catch (Exception ex) {
            throw new HandlerParseException(ex.getMessage());
        }
    }

    @Override
    public String toString(Double obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }
        return decimalFormat.format(obj);
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
                    pattern = cmd.getArgs()[0];
                    break;
                }
                case CMD_SET_DECIMAL_SEPARATOR: {
                    decimalSeparator = cmd.getArgs()[0].charAt(0);
                    break;
                }
                case CMD_SET_GROUPING_SEPARATOR: {
                    groupingSeparator = cmd.getArgs()[0].charAt(0);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown command: " + cmd.getName());
                }
            }
        }

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);

        dfs.setDecimalSeparator(decimalSeparator);

        dfs.setGroupingSeparator(groupingSeparator);
        decimalFormat = new DecimalFormat(pattern, dfs);
    }

    private void setDefaultValues() {
        pattern = patternDefault;
        decimalSeparator = decimalSeparatorDefault;
        groupingSeparator = groupingSeparatorDefault;

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
        dfs.setDecimalSeparator(decimalSeparator);
        dfs.setGroupingSeparator(groupingSeparator);
        decimalFormat = new DecimalFormat(pattern, dfs);
    }

    public static Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DefaultDoubleHandler.locale = locale;
    }

    public static String getPatternDefault() {
        return patternDefault;
    }

    public static void setPatternDefault(String patternDefault) {
        DefaultDoubleHandler.patternDefault = patternDefault;
    }

    public static char getDecimalSeparatorDefault() {
        return decimalSeparatorDefault;
    }

    public static void setDecimalSeparatorDefault(char decimalSeparatorDefault) {
        DefaultDoubleHandler.decimalSeparatorDefault = decimalSeparatorDefault;
    }

    public static char getGroupingSeparatorDefault() {
        return groupingSeparatorDefault;
    }

    public static void setGroupingSeparatorDefault(char groupingSeparatorDefault) {
        DefaultDoubleHandler.groupingSeparatorDefault = groupingSeparatorDefault;
    }
}
