/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import coffeepot.bean.wr.types.CharCase;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultStringHandler implements TypeHandler<String> {

    public static final String PARAM_FILTER_NUMBER_ONLY = "filter=\\D";
    public static final String PARAM_FILTER_NUMBER_LETTERS_ONLY = "filter=[^0-9\\p{L}]";
    private CharCase charCase;
    private String filter;

    public DefaultStringHandler() {
        setDefaultValues();
    }

    @Override
    public String parse(String text) throws HandlerParseException {
        return text;
    }

    @Override
    public String toString(String obj) {
        if (obj == null) {
            return null;
        }

        if (filter != null && !filter.isEmpty()) {
            obj = obj.replaceAll(filter, "");
        }

        switch (charCase) {
            case UPPER:
                obj = obj.toUpperCase();
                break;
            case LOW:
                obj = obj.toLowerCase();
                break;
        }
        return obj;
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
                    value = "true";
                }
                switch (key) {
                    case "filter":
                        filter = value;
                        break;
                    case "charCase":
                        charCase = CharCase.valueOf(value);
                        break;
                    case "CharCase.UPPER":
                        charCase = CharCase.UPPER;
                        break;
                    case "CharCase.LOW":
                        charCase = CharCase.LOW;
                        break;
                    case "CharCase.NORMAL":
                        charCase = CharCase.NORMAL;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown parameter: \"" + key + "\"");
                }
            }
        }
    }

    private void setDefaultValues() {
        charCase = CharCase.NORMAL;
        filter = null;
    }
}
