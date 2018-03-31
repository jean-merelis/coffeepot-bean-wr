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
import coffeepot.bean.wr.mapper.Metadata;
import coffeepot.bean.wr.types.CharCase;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a default string handler with some features for
 * filtering, replacing and changing charcase.
 *
 * <p>
 * Filters use the 'replaceAll' method of the String object. This way the
 * parameter passing occurs with the following scheme 'filter={regex}' and will
 * be used as <code>s.replaceAll({regex}, "");</code></p>
 *
 * <p>
 * The "replace" feature require three params: first is the command, second is
 * the target and third is the replacement. Example: <br>
 * <code>@Field(name="description", params={"replace", "old text", "new text"})</code>
 * <br>We can provide the "replace" command several times (
 * <code>@Field(name="description", params={"replace", "old text", "new text", "replace", "another", "c3po", "replaceAll", "test", ""})</code>
 * ).
 * </p>
 *
 * <p>
 * The "replace" feature has three commands: "replace", "replaceFirst" and
 * "replaceAll".<br>
 * The second param (target) of the "replaceFirst" and of the "replaceAll"
 * commands are a regex.
 * </p>
 *
 * <p>
 * We can enable the "replace" feature only for writing or just for reading,
 * just add "onlyOnRead" or "onlyOnWrite" in the command parameter. Example:<br>
 * <code>@Field(name="description", params={"replace onlyOnWrite", "old text", "new text"})</code>
 * </p>
 *
 * <p>
 * To change the charcase, use the following parameters: "CharCase.UPPER",
 * "CharCase.LOW" or "CharCase.NORMAL" Example:<br>
 * <code>@Field(name="description", params={"CharCase.UPPER"})</code> or <br>
 * <code>@Field(name="description", params={"charCase=UPPER"})</code>
 * </p>
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultStringHandler implements TypeHandler<String> {

    public static final String FILTER_NUMBER_ONLY = "filter=\\D";
    public static final String FILTER_NUMBER_LETTERS_ONLY = "filter=[^0-9\\p{L}]";

    /**
     * @deprecated Use DefaultStringHandler.FILTER_NUMBER_ONLY instead.
     */
    @Deprecated
    public static final String PARAM_FILTER_NUMBER_ONLY = FILTER_NUMBER_ONLY;

    /**
     * @deprecated Use DefaultStringHandler.FILTER_NUMBER_LETTERS_ONLY instead.
     */
    @Deprecated
    public static final String PARAM_FILTER_NUMBER_LETTERS_ONLY = FILTER_NUMBER_LETTERS_ONLY;

    public static final String CMD_REPLACE = "replace";
    public static final String CMD_REPLACE_FIRST = "replaceFirst";
    public static final String CMD_REPLACE_ALL = "replaceAll";

    public static final String CMD_REPLACE_ONLY_ON_READ = "replace onlyOnRead";
    public static final String CMD_REPLACE_FIRST_ONLY_ON_READ = "replaceFirst onlyOnRead";
    public static final String CMD_REPLACE_ALL_ONLY_ON_READ = "replaceAll onlyOnRead";

    public static final String CMD_REPLACE_ONLY_ON_WRITE = "replace onlyOnWrite";
    public static final String CMD_REPLACE_FIRST_ONLY_ON_WRITE = "replaceFirst onlyOnWrite";
    public static final String CMD_REPLACE_ALL_ONLY_ON_WRITE = "replaceAll onlyOnWrite";

    public static final String CHARCASE_UPPER = "CharCase.UPPER";
    public static final String CHARCASE_LOW = "CharCase.LOW";
    public static final String CHARCASE_NORMAL = "CharCase.NORMAL";

    private CharCase charCase;
    private String filter;
    private List<Replace> replaces;

    public DefaultStringHandler() {
        setDefaultValues();
    }

    @Override
    public String parse(String text, Metadata metadata) throws HandlerParseException {

        if (text == null) {
            return null;
        }

        if (filter != null && !filter.isEmpty()) {
            text = text.replaceAll(filter, "");
        }

        switch (charCase) {
            case UPPER:
                text = text.toUpperCase();
                break;
            case LOW:
                text = text.toLowerCase();
                break;
        }


        if (replaces != null) {
            for (Replace r : replaces) {
                if (!r.onRead) {
                    continue;
                }
                switch (r.type) {
                    case REPLACE:
                        text = text.replace(r.target, r.replacement);
                        break;
                    case REPLACE_FIRST:
                        text = text.replaceFirst(r.target, r.replacement);
                        break;
                    case REPLACE_ALL:
                        text = text.replaceAll(r.target, r.replacement);
                        break;
                }
            }
        }

        return text;
    }

    @Override
    public String toString(String text, Metadata metadata) {
        if (text == null) {
            return null;
        }

        if (filter != null && !filter.isEmpty()) {
            text = text.replaceAll(filter, "");
        }

        switch (charCase) {
            case UPPER:
                text = text.toUpperCase();
                break;
            case LOW:
                text = text.toLowerCase();
                break;
        }

        if (replaces != null) {
            for (Replace r : replaces) {
                if (!r.onWrite) {
                    continue;
                }
                switch (r.type) {
                    case REPLACE:
                        text = text.replace(r.target, r.replacement);
                        break;
                    case REPLACE_FIRST:
                        text = text.replaceFirst(r.target, r.replacement);
                        break;
                    case REPLACE_ALL:
                        text = text.replaceAll(r.target, r.replacement);
                        break;
                }
            }
        }

        return text;
    }

    @Override
    public void setConfig(String[] params) {
        if (params == null || params.length == 0) {
            setDefaultValues();
            return;
        }
        for (int i = 0; i < params.length; i++) {
            String s = params[i];

            if (s.startsWith("replace")) {

                // make sure it have the required two parameters
                if (i + 2 >= params.length) {
                    throw new IllegalArgumentException("The 'Replace' feature requires two extra parameters. The first is the target and the second is the replacement");
                }

                if (replaces == null) {
                    replaces = new ArrayList<>();
                }

                Replace r = new Replace();
                if (s.startsWith("replaceFirst")) {
                    r.type = ReplaceType.REPLACE_FIRST;
                } else if (s.startsWith("replaceAll")) {
                    r.type = ReplaceType.REPLACE_ALL;
                } else {
                    r.type = ReplaceType.REPLACE;
                }
                i++;
                r.target = params[i];
                i++;
                r.replacement = params[i];

                if (s.contains("onlyOnRead")) {
                    r.onRead = true;
                    r.onWrite = false;
                } else if (s.contains("onlyOnWrite")) {
                    r.onRead = false;
                    r.onWrite = true;
                } else {
                    r.onRead = true;
                    r.onWrite = true;
                }

                replaces.add(r);

            } else {

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
                            filter = s.replaceFirst("^[^=]*=", "");
                            break;
                        case "charCase":
                            charCase = CharCase.valueOf(value);
                            break;
                        case CHARCASE_UPPER:
                            charCase = CharCase.UPPER;
                            break;
                        case CHARCASE_LOW:
                            charCase = CharCase.LOW;
                            break;
                        case CHARCASE_NORMAL:
                            charCase = CharCase.NORMAL;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown parameter: \"" + key + "\"");
                    }
                }
            }
        }
    }

    private void setDefaultValues() {
        charCase = CharCase.NORMAL;
        filter = null;
        replaces = null;
    }

    private static class Replace {

        ReplaceType type;
        String target;
        String replacement;
        boolean onRead;
        boolean onWrite;
    }

    private enum ReplaceType {
        REPLACE,
        REPLACE_FIRST,
        REPLACE_ALL;
    }
}
