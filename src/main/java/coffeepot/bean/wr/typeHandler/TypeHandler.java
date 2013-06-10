/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import java.util.Map;

/**
 *
 * @author Jeandeson O. Merelis
 */
public interface TypeHandler<T> {

    T parse(String text) throws HandlerParseException;

    String toString(T obj);

    void setConfig(String[] params);
}
