/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

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

    public DefaultDoubleHandler() {
        setDefaultValues();
    }

    @Override
    public Double parse(String text) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }
        Double d;
        try {
            d = (Double) decimalFormat.parse(text);
            return d;
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
        pattern = "#0.##########";
        decimalFormat = new DecimalFormat(pattern);
        decimalSeparator = decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
        groupingSeparator = decimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
    }
}
