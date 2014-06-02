/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 Jeandeson O. Merelis
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
    @Deprecated
    void registerTypeHandlerInstanceAsDefaultFor(Class<?> forClass, TypeHandler handlerInstance);
    
    void registerTypeHandlerClassFor(Class<?> forClass, Class<? extends TypeHandler> handlerClass);

    TypeHandler create(Class<?> forClass, String[] params) throws InstantiationException, IllegalAccessException;

    TypeHandler create(Class<?> forClass, Class<? extends TypeHandler> typeHandler, String[] params) throws InstantiationException, IllegalAccessException;
}
