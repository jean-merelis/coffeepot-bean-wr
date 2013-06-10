/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer.customHandler;

import coffeepot.bean.wr.typeHandler.HandlerParseException;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import org.joda.time.DateTime;

/**
 * Only tests.
 * @author Jeandeson O. Merelis
 */
public class DateTimeHandler implements TypeHandler<DateTime> {

    @Override
    public DateTime parse(String text) throws HandlerParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try{
            return DateTime.parse(text);
        }catch(Exception ex){
            throw new HandlerParseException(ex.getMessage());
        }
    }

    @Override
    public String toString(DateTime obj) {
        if (obj == null) return null;
        return obj.toString();
    }

    @Override
    public void setConfig(String[] params) {
        //set your config here
    }
}
