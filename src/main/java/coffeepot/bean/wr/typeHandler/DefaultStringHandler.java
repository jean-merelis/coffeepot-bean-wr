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
