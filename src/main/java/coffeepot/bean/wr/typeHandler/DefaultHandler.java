/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

import java.util.Map;

/**
 *
 * @author Jeandeson O. Merelis
 */
public abstract class DefaultHandler implements TypeHandler<Object> {

    @Override
    public abstract Object parse(String text);

    @Override
    public abstract String toString(Object obj);

    @Override
    public abstract void setConfig(String[] params);
}
