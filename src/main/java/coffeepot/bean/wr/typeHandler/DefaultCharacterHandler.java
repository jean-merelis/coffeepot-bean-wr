/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultCharacterHandler implements TypeHandler<Character> {

    @Override
    public Character parse(String text) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }
        if (text.length() > 1) {
            throw new HandlerParseException("Can not convert the text \"" + text + "\" to Character");
        }

        return text.charAt(0);
    }

    @Override
    public String toString(Character obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    @Override
    public void setConfig(String[] params) {
    }
}
