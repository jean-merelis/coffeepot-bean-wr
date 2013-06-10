/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import java.util.Map;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultLongHandler implements TypeHandler<Long> {

    @Override
    public Long parse(String text) throws HandlerParseException {
        if (text == null || "".equals(text)) {
            return null;
        }
        try {
            return Long.parseLong(text);
        } catch (Exception ex) {
            throw new HandlerParseException(ex.getMessage());
        }
    }

    @Override
    public String toString(Long obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    @Override
    public void setConfig(String[] params) {
    }
}
