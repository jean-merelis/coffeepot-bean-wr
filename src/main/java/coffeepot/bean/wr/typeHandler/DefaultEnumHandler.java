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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultEnumHandler implements TypeHandler<Enum> {

    protected boolean ordinalMode = false;
    protected Class<? extends Enum> type = Enum.class;

    public static final String CMD_SET_ORDINAL_MODE = "setOrdinalMode";
    public static final String CMD_SET_ENUM_CLASS = "setEnumClass";

    @Override
    public Enum parse(String text, Metadata metadata) throws HandlerParseException {
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
    public String toString(Enum obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }

        if (ordinalMode) {
            return String.valueOf(obj.ordinal());
        }

        return obj.name();
    }

    @Override
    public void config(Command[] commands) {
        if (commands == null || commands.length == 0) {
            return;
        }
        for (Command cmd : commands) {
            switch (cmd.getName()) {
                case CMD_SET_ORDINAL_MODE: {
                    ordinalMode = "true".equals(cmd.getArgs()[0]);
                    break;
                }
                case CMD_SET_ENUM_CLASS: {
                    try {
                        type = (Class<? extends Enum>) Class.forName(cmd.getArgs()[0]);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(DefaultEnumHandler.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IllegalArgumentException("Class not found: \"" + cmd.getArgs()[0] + "\"");
                    } catch (Exception ex) {
                        Logger.getLogger(DefaultEnumHandler.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IllegalArgumentException("The Class \"" + cmd.getArgs()[0] + "\" may not be a Enum class");
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown command: " + cmd.getName());
                }
            }
        }
    }
}
