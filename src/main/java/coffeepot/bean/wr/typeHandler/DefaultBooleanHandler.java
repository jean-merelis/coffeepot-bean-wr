/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import coffeepot.bean.wr.mapper.Command;
import coffeepot.bean.wr.mapper.Metadata;

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
/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultBooleanHandler implements TypeHandler<Boolean> {

    private String trueText;
    private String falseText;

    protected static String trueTextDefault = "true";
    protected static String falseTextDefault = "false";

    public static final String CMD_SET_TRUE_TEXT = "setTrueText";
    public static final String CMD_SET_FALSE_TEXT = "setFalseText";

    public DefaultBooleanHandler() {
        setDefaultValues();
    }

    @Override
    public Boolean parse(String text, Metadata metadata) throws HandlerParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        if (text.equals(trueText)) {
            return Boolean.TRUE;
        }

        if (text.equals(falseText)) {
            return Boolean.FALSE;
        }

        throw new HandlerParseException("Can not convert the text \"" + text + "\" to Boolean");
    }

    @Override
    public String toString(Boolean obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }

        return obj == true ? trueText : falseText;
    }

    @Override
    public void config(Command[] commands) {
        if (commands == null || commands.length == 0) {
            setDefaultValues();
            return;
        }


        for (Command cmd : commands) {
            switch (cmd.getName()) {
                case CMD_SET_TRUE_TEXT: {
                    trueText = cmd.getArgs()[0];
                    break;
                }
                case CMD_SET_FALSE_TEXT: {
                    trueText = cmd.getArgs()[0];
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown command: " + cmd.getName());
                }
            }
        }
    }

    private void setDefaultValues() {
        trueText = trueTextDefault;
        falseText = falseTextDefault;
    }

    public static String getTrueTextDefault() {
        return trueTextDefault;
    }

    public static void setTrueTextDefault(String trueTextDefault) {
        DefaultBooleanHandler.trueTextDefault = trueTextDefault;
    }

    public static String getFalseTextDefault() {
        return falseTextDefault;
    }

    public static void setFalseTextDefault(String falseTextDefault) {
        DefaultBooleanHandler.falseTextDefault = falseTextDefault;
    }

}
