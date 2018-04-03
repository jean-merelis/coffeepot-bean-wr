/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 - 2018 Jeandeson O. Merelis
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultZonedDateTimeHandler implements TypeHandler<ZonedDateTime> {

    protected static String patternDefault;
    public static final String CMD_SET_PATTERN = "setPattern";

    static {
        patternDefault = "yyyy-MM-dd'T'HH:mm:ssZ";
    }

    protected String pattern;
    private DateTimeFormatter formatter;

    public DefaultZonedDateTimeHandler() {
        setDefaultValues();
    }

    @Override
    public ZonedDateTime parse(String text, Metadata metadata) throws HandlerParseException {
        if (text == null || (text = text.trim()).isEmpty()) {
            return null;
        }

        try {
            return ZonedDateTime.parse(text, formatter);
        } catch (Exception ex) {
            throw new HandlerParseException(ex);
        }
    }

    @Override
    public String toString(ZonedDateTime obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }

        return formatter.format(obj);
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
                    formatter = DateTimeFormatter.ofPattern(pattern);
                    break;
                }
            }
        }
    }

    private void setDefaultValues() {
        pattern = patternDefault;
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public static String getPatternDefault() {
        return patternDefault;
    }

    public static void setPatternDefault(String patternDefault) {
        DefaultZonedDateTimeHandler.patternDefault = patternDefault;
    }
}
