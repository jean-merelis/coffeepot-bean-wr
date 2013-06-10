/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class HandlerParseException extends Exception {

    /**
     * Creates a new instance of
     * <code>HandlerParseException</code> without detail message.
     */
    public HandlerParseException() {
    }

    /**
     * Constructs an instance of
     * <code>HandlerParseException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public HandlerParseException(String msg) {
        super(msg);
    }
}
