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


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultEnumHandler implements TypeHandler<Enum> {

    protected boolean ordinalMode = false;
    protected Class<? extends Enum> type;

    @Override
    public Enum parse(String text) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }

        try {
            Enum[] enumConstants = type.getEnumConstants();
            if (ordinalMode) {
                int i = Integer.parseInt(text);
                if (i < 0 || (i + 1) > enumConstants.length) {
                    throw new Exception("Index out of bounds");
                }
                return enumConstants[i];
            } else {
                return Enum.valueOf(type, text);
            }
        } catch (Exception ex) {
            throw new HandlerParseException(ex.getMessage());
        }
    }

    @Override
    public String toString(Enum obj) {
        if (obj == null) {
            return null;
        }

        if (ordinalMode) {
            return String.valueOf(obj.ordinal());
        }

        return obj.toString();
    }

    @Override
    public void setConfig(String[] params) {
        if (params == null || params.length == 0) {
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
                    case "ordinalMode":
                        ordinalMode = "true".equals(value);
                        break;
                    case "enum":
                        ;
                    case "enumClass":
                        ;
                    case "class":
                        try {
                            type = (Class<? extends Enum>) Class.forName(value);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(DefaultEnumHandler.class.getName()).log(Level.SEVERE, null, ex);
                            throw new IllegalArgumentException("Class not found: \"" + value + "\"");
                        } catch (Exception ex) {
                            Logger.getLogger(DefaultEnumHandler.class.getName()).log(Level.SEVERE, null, ex);
                            throw new IllegalArgumentException("The Class \"" + value + "\" may not be a Enum class");
                        }
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown parameter: \"" + key + "\"");
                }
            }
        }
    }
}
