/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/**
 *
 * @author Jeandeson O. Merelis
 */
public interface TypeHandlerFactory {

    void clearTypeHandlerMap();

    void clearRegisterTypeHandlerClass();

    /**
     * Registers a instance of handler (previously configured) as default to a type.
     * @param forClass
     * @param handlerInstance 
     */
    void registerTypeHandlerInstanceAsDefaultFor(Class<?> forClass, TypeHandler handlerInstance);
    
    void registerTypeHandlerClassFor(Class<?> forClass, Class<? extends TypeHandler> handlerClass);

    TypeHandler create(Class<?> forClass, String[] params) throws InstantiationException, IllegalAccessException;

    TypeHandler create(Class<?> forClass, Class<? extends TypeHandler> typeHandler, String[] params) throws InstantiationException, IllegalAccessException;
}
