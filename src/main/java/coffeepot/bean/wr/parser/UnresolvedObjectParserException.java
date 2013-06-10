/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.parser;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class UnresolvedObjectParserException extends Exception {

    /**
     * Creates a new instance of
     * <code>UnresolvedObjectParserException</code> without detail message.
     */
    public UnresolvedObjectParserException() {
    }

    /**
     * Constructs an instance of
     * <code>UnresolvedObjectParserException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public UnresolvedObjectParserException(String msg) {
        super(msg);
    }
}
