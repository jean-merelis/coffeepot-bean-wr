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
import java.text.DecimalFormat;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultDoubleHandler implements TypeHandler<Double> {

    protected DecimalFormat decimalFormat;
    protected String pattern;
    protected char decimalSeparator;
    protected char groupingSeparator;

    protected static String patternDefault = "#0.##########";
    protected static char decimalSeparatorDefault = ((DecimalFormat) DecimalFormat.getInstance()).getDecimalFormatSymbols().getDecimalSeparator();
    protected static char groupingSeparatorDefault = ((DecimalFormat) DecimalFormat.getInstance()).getDecimalFormatSymbols().getGroupingSeparator();

    public DefaultDoubleHandler() {
        setDefaultValues();
    }

    @Override
    public Double parse(String text) throws HandlerParseException {
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
    public String toString(Double obj) {
        if (obj == null) {
            return null;
        }
        return decimalFormat.format(obj);
    }

    @Override
    public void setConfig(String[] params) {
        if (params == null || params.length == 0) {
            setDefaultValues();
            return;
        }
        for (String s : params) {
            String[] keyValue = s.split("=");
            if (keyValue.length > 0) {
                String key = keyValue[0].trim();
                String value;
                if (keyValue.length > 1) {
                    value = keyValue[1].trim();
                } else {
                    value = "";
                }
                switch (key) {
                    case "pattern":
                        pattern = value;
                        break;
                    case "decimalSeparator":
                        if (value.length() > 0) {
                            decimalSeparator = value.charAt(0);
                        }
                        break;
                    case "groupingSeparator":
                        if (value.length() > 0) {
                            groupingSeparator = value.charAt(0);
                        }
                        break;
                    default:
                        pattern = key;
                }
            }
        }
        decimalFormat = new DecimalFormat(pattern);
        decimalFormat.getDecimalFormatSymbols().setDecimalSeparator(decimalSeparator);
        decimalFormat.getDecimalFormatSymbols().setGroupingSeparator(groupingSeparator);
    }

    private void setDefaultValues() {
        pattern = patternDefault;
        decimalFormat = new DecimalFormat(pattern);
        decimalSeparator = decimalSeparatorDefault;
        groupingSeparator = groupingSeparatorDefault;
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
