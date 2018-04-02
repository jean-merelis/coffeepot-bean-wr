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
import coffeepot.bean.wr.types.CharCase;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a default string handler with some commands for
 * filtering, replacing and changing charcase.
 *
 * <br>
 * Supported commands are:
 * <ul>
 * <li>replace</li>
 * <li>replaceFirst</li>
 * <li>replaceAll</li>
 * <li>filter</li>
 * <li>charCase</li>
 * </ul>
 *
 * <p>
 * <strong>Replace command</strong>: "replace".<br>
 * Replace command require two args, the first arg is the target and the second
 * is the replacement. A third optional argument can be provided to determine
 * whether command is read-only or write-only (--onlyOnRead or
 * --onlyOnWrite).<br>
 * This command use the 'replace' method of the String class.
 * <code>s.replace({arg0}, {arg1});</code></p>
 * <p>
 *
 * <p>
 * <strong>Replace First command</strong>: "replaceFirst".<br>
 * ReplaceFirst command require two args, the first arg is a regex target and
 * the second is the replacement. A third optional argument can be provided to
 * determine whether command is read-only or write-only (--onlyOnRead or
 * --onlyOnWrite).<br>
 * This command use the 'replaceFirst' method of the String class.
 * <code>s.replaceFirst({arg0}, {arg1});</code></p>
 * <p>
 *
 * <p>
 * <strong>Replace All command</strong>: "replaceAll".<br>
 * ReplaceAll command require two args, the first arg is a regex target and the
 * second is the replacement. A third optional argument can be provided to
 * determine whether command is read-only or write-only (--onlyOnRead or
 * --onlyOnWrite).<br>
 * This command use the 'replaceAll' method of the String class.
 * <code>s.replaceAll({arg0}, {arg1});</code></p>
 *
 * <p>
 * <strong>CharCase command</strong>: "charCase".<br>
 * CharCase command require one arg. The options are: "UPPER", "LOW",
 * "NORMAL".</p>
 *
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultStringHandler implements TypeHandler<String> {

    /**
     * Replace command: "replace".
     * <p>
     * Replace command require two args, the first arg is the target and the
     * second is the replacement. A third optional argument can be provided to
     * determine whether command is read-only or write-only (--onlyOnRead or
     * --onlyOnWrite).<br>
     * This command use the 'replace' method of the String class.
     * <code>s.replace({arg0}, {arg1});</code></p>
     * <p>
     */
    public static final String CMD_REPLACE = "replace";

    /**
     * Replace First command: "replaceFirst".
     * <p>
     * ReplaceFirst command require two args, the first arg is a regex target
     * and the second is the replacement. A third optional argument can be
     * provided to determine whether command is read-only or write-only
     * (--onlyOnRead or --onlyOnWrite).<br>
     * This command use the 'replaceFirst' method of the String class.
     * <code>s.replaceFirst({arg0}, {arg1});</code></p>
     * <p>
     */
    public static final String CMD_REPLACE_FIRST = "replaceFirst";

    /**
     * Replace All command: "replaceAll".
     * <p>
     * ReplaceAll command require two args, the first arg is a regex target and
     * the second is the replacement. A third optional argument can be provided
     * to determine whether command is read-only or write-only (--onlyOnRead or
     * --onlyOnWrite).<br>
     * This command use the 'replaceAll' method of the String class.
     * <code>s.replaceAll({arg0}, {arg1});</code></p>
     */
    public static final String CMD_REPLACE_ALL = "replaceAll";

    /**
     * Add prefix command: "addPrefix".
     * <p>
     * The addPrefix command require one arg. The first arg is used to prefix
     * the text. A second optional argument can be provided to determine whether
     * command is read-only or write-only (--onlyOnRead or --onlyOnWrite).
     */
    public static final String CMD_ADD_PREFIX = "addPrefix";

    /**
     * Add suffix command: "addSuffix".
     * <p>
     * The addSuffix command require one arg. The first arg is used to suffix
     * the text. A second optional argument can be provided to determine whether
     * command is read-only or write-only (--onlyOnRead or --onlyOnWrite).
     * <strong>This command is applyed</strong>
     *
     */
    public static final String CMD_ADD_SUFFIX = "addSuffix";

    public static final String OPT_ONLY_ON_READ = "--onlyOnRead";
    public static final String OPT_ONLY_ON_WRITE = "--onlyOnWrite";

    /**
     * Filter command: "filter".
     * <p>
     * The Filter command require one arg. The first arg is a regex target to be
     * used to filter the string. A second optional argument can be provided to
     * determine whether command is read-only or write-only (--onlyOnRead or
     * --onlyOnWrite).<br>
     * This command use the 'replaceAll' method of the String class.
     * <code>s.replaceAll({arg0}, "");</code></p>
     */
    public static final String CMD_FILTER = "filter";
    public static final String FILTER_NUMBER_ONLY = "\\D";
    public static final String FILTER_NUMBER_LETTERS_ONLY = "[^0-9\\p{L}]";

    /**
     * CharCase command: "charCase".
     * <p>
     * CharCase command require one arg. The options are: "UPPER", "LOW",
     * "NORMAL".. A second optional argument can be provided to determine
     * whether command is read-only or write-only (--onlyOnRead or
     * --onlyOnWrite).</p>
     */
    public static final String CMD_CHARCASE = "charCase";
    public static final String CHARCASE_UPPER = "UPPER";
    public static final String CHARCASE_LOW = "LOW";
    public static final String CHARCASE_NORMAL = "NORMAL";

    private List<InternalCommnand> commands;

    public DefaultStringHandler() {
        setDefault();
    }

    @Override
    public String parse(String text, Metadata metadata) throws HandlerParseException {

        if (text == null) {
            return null;
        }

        if (commands != null) {
            for (InternalCommnand r : commands) {
                text = r.execOnRead(text);
            }
        }

        return text;
    }

    @Override
    public String toString(String text, Metadata metadata) {
        if (text == null) {
            return null;
        }


        if (commands != null) {
            for (InternalCommnand r : commands) {
                text = r.execOnWrite(text);
            }
        }

        return text;
    }

    @Override
    public void config(Command[] cmds) {
        if (cmds == null || cmds.length == 0) {
            setDefault();
            return;
        }
        for (Command cmd : cmds) {
            switch (cmd.getName()) {
                case CMD_REPLACE: {
                    if (cmd.getArgs() == null || cmd.getArgs().length < 2) {
                        throw new IllegalArgumentException("The replace command requires two args, the third arg is optional. The first is the target and the second is the replacement");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }

                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.REPLACE;
                    r.arg0 = cmd.getArgs()[0];
                    r.arg1 = cmd.getArgs()[1];
                    defReadWriteOnly(r, cmd.getArgs().length > 2 ? cmd.getArgs()[2] : null);
                    this.commands.add(r);
                    break;
                }
                case CMD_REPLACE_FIRST: {
                    if (cmd.getArgs() == null || cmd.getArgs().length < 2) {
                        throw new IllegalArgumentException("The replaceFirst command requires two args, the third arg is optional. The first is a regex and the second is the replacement");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }

                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.REPLACE_FIRST;
                    r.arg0 = cmd.getArgs()[0];
                    r.arg1 = cmd.getArgs()[1];
                    defReadWriteOnly(r, cmd.getArgs().length > 2 ? cmd.getArgs()[2] : null);
                    this.commands.add(r);
                    break;
                }
                case CMD_REPLACE_ALL: {
                    if (cmd.getArgs() == null || cmd.getArgs().length < 2) {
                        throw new IllegalArgumentException("The replaceAll command requires two args, the third arg is optional. The first is a regex and the second is the replacement");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }

                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.REPLACE_ALL;
                    r.arg0 = cmd.getArgs()[0];
                    r.arg1 = cmd.getArgs()[1];
                    defReadWriteOnly(r, cmd.getArgs().length > 2 ? cmd.getArgs()[2] : null);
                    this.commands.add(r);
                    break;
                }
                case CMD_FILTER: {
                    if (cmd.getArgs() == null || cmd.getArgs().length == 0) {
                        throw new IllegalArgumentException("The filter command requires one arg, the second arg is optional. The first is a regex used to filter the string");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }

                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.REPLACE_ALL;
                    r.arg0 = cmd.getArgs()[0];
                    r.arg1 = "";
                    defReadWriteOnly(r, cmd.getArgs().length > 1 ? cmd.getArgs()[1] : null);
                    this.commands.add(r);
                    break;
                }
                case CMD_CHARCASE: {
                    if (cmd.getArgs() == null || cmd.getArgs().length == 0) {
                        throw new IllegalArgumentException("The charCase command requires one arg. The first is a regex used to filter the string");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }
                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.CHARCASE;
                    r.charCase = CharCase.valueOf(cmd.getArgs()[0]);
                    defReadWriteOnly(r, cmd.getArgs().length > 1 ? cmd.getArgs()[1] : null);
                    this.commands.add(r);
                    break;
                }
                case CMD_ADD_PREFIX: {
                    if (cmd.getArgs() == null || cmd.getArgs().length == 0) {
                        throw new IllegalArgumentException("The addPrefix command requires one arg, the second arg is optional.");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }

                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.ADD_PREFIX;
                    r.arg0 = cmd.getArgs()[0];
                    defReadWriteOnly(r, cmd.getArgs().length > 1 ? cmd.getArgs()[1] : null);
                    this.commands.add(r);
                    break;
                }
                case CMD_ADD_SUFFIX: {
                    if (cmd.getArgs() == null || cmd.getArgs().length == 0) {
                        throw new IllegalArgumentException("The addSuffix command requires one arg, the second arg is optional.");
                    }
                    if (this.commands == null) {
                        this.commands = new ArrayList<>();
                    }

                    InternalCommnand r = new InternalCommnand();
                    r.type = CommandType.ADD_SUFFIX;
                    r.arg0 = cmd.getArgs()[0];
                    defReadWriteOnly(r, cmd.getArgs().length > 1 ? cmd.getArgs()[1] : null);
                    this.commands.add(r);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown command: " + cmd.getName());
                }
            }
        }
    }

    private void defReadWriteOnly(InternalCommnand r, String arg) {
        r.onRead = true;
        r.onWrite = true;

        if (arg == null) {
            return;
        }

        if (OPT_ONLY_ON_READ.equals(arg)) {
            r.onWrite = false;
        } else if (OPT_ONLY_ON_WRITE.equals(arg)) {
            r.onRead = false;
        }
    }

    private void setDefault() {
        commands = null;
    }

    private static class InternalCommnand {

        CommandType type;
        String arg0;
        String arg1;
        CharCase charCase;
        boolean onRead;
        boolean onWrite;

        String execOnRead(String text) {
            if (!onRead) {
                return text;
            }
            return perform(text);
        }

        String execOnWrite(String text) {
            if (!onWrite) {
                return text;
            }
            return perform(text);
        }

        private String perform(String text) {
            switch (type) {
                case REPLACE:
                    text = text.replace(arg0, arg1);
                    break;
                case REPLACE_FIRST:
                    text = text.replaceFirst(arg0, arg1);
                    break;
                case REPLACE_ALL:
                    text = text.replaceAll(arg0, arg1);
                    break;
                case CHARCASE: {
                    switch (charCase) {
                        case UPPER:
                            text = text.toUpperCase();
                            break;
                        case LOW:
                            text = text.toLowerCase();
                            break;
                    }
                    break;
                }
                case ADD_PREFIX: {
                    text = arg0 + text;
                    break;
                }
                case ADD_SUFFIX: {
                    text = text + arg0;
                    break;
                }
            }
            return text;
        }
    }

    private enum CommandType {
        REPLACE,
        REPLACE_FIRST,
        REPLACE_ALL,
        CHARCASE,
        ADD_PREFIX,
        ADD_SUFFIX;
    }
}
