/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer.customHandler;

import coffeepot.bean.wr.typeHandler.DefaultStringHandler;

/**
 * Only tests.
 * @author Jeandeson O. Merelis
 */
public class LowStringHandler extends DefaultStringHandler {

    @Override
    public String toString(String obj) {
        if (obj == null) {
            return null;
        }
        String s = super.toString(obj);
        return s.toLowerCase();
    }
}
