/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import java.util.Map;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultIntegerHandler implements TypeHandler<Integer> {

    @Override
    public Integer parse(String text) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (Exception ex) {
            throw new HandlerParseException(ex.getMessage());
        }
    }

    @Override
    public String toString(Integer obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    @Override
    public void setConfig(String[] params) {
    }
}
