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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class TypeHandlerFactoryImpl implements TypeHandlerFactory {

    private Map<String, TypeHandler> handlers = new HashMap<>();
    private Map<Class<?>, Class<? extends TypeHandler>> defaultHandlers = new HashMap<>();

    public TypeHandlerFactoryImpl() {
        registerDefaultHandlers();
    }

    private String getParamsId(String[] params) {
        if (params == null || params.length == 0) {
            return "{default}";
        }
        //TODO: vale a pena ordernar os parametros para se criar uma chave mais confiável?
        // exemplo: {charCase=UPPER; filter=\\D+} é equivalente a {filter=\\D+; charCase=UPPER}

        Arrays.sort(params, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (String s : params) {
            sb.append(s);
        }
        sb.append('}');
        return sb.toString().toLowerCase();
    }

    @Override
    public TypeHandler create(Class<?> forClass, String[] params) throws InstantiationException, IllegalAccessException {
        String key;
        TypeHandler handler = null;

        if (TypeHandler.class.isAssignableFrom(forClass)) {
            key = forClass.getName() + getParamsId(params);
            handler = handlers.get(key);
            if (handler != null) {
                return handler;
            }
            try {
                handler = (TypeHandler) forClass.newInstance();
                handler.setConfig(params);
                handlers.put(key, handler);
                return handler;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(TypeHandlerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }

        Class<? extends TypeHandler> defHandler = defaultHandlers.get(forClass);
        if (defHandler != null) {
            key = defHandler.getName() + getParamsId(params);
            handler = handlers.get(key);
            if (handler != null) {
                return handler;
            }

            try {
                handler = defHandler.newInstance();
                handler.setConfig(params);
                handlers.put(key, handler);
                return handler;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(TypeHandlerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }

        return handler;
    }

    @Override
    public TypeHandler create(Class<?> forClass, Class<? extends TypeHandler> typeHandler, String[] params) throws InstantiationException, IllegalAccessException {
        if (DefaultHandler.class.equals(typeHandler)) {
            return create(forClass, params);
        } else {
            return create(typeHandler, params);
        }
    }

    @Override
    public void registerTypeHandlerClassFor(Class<?> forClass, Class<? extends TypeHandler> handlerClass) {
        defaultHandlers.put(forClass, handlerClass);
    }

    @Override
    public void registerTypeHandlerInstanceAsDefaultFor(Class<?> forClass, TypeHandler handlerInstance) {
        if (forClass == null) {
            throw new IllegalArgumentException("Parameter 'forClass' can not be null");
        }
        if (handlerInstance == null) {
            throw new IllegalArgumentException("Parameter 'handlerInstance' can not be null");
        }
        registerTypeHandlerClassFor(forClass, handlerInstance.getClass());
        String key = handlerInstance.getClass().getName() + getParamsId(null);
        handlers.put(key, handlerInstance);
    }

    private void registerDefaultHandlers() {
        registerTypeHandlerClassFor(String.class, DefaultStringHandler.class);
        registerTypeHandlerClassFor(Character.class, DefaultCharacterHandler.class);
        registerTypeHandlerClassFor(char.class, DefaultCharacterHandler.class);
        registerTypeHandlerClassFor(Integer.class, DefaultIntegerHandler.class);
        registerTypeHandlerClassFor(int.class, DefaultIntegerHandler.class);
        registerTypeHandlerClassFor(Long.class, DefaultLongHandler.class);
        registerTypeHandlerClassFor(long.class, DefaultLongHandler.class);
        registerTypeHandlerClassFor(Date.class, DefaultDateHandler.class);
        registerTypeHandlerClassFor(Float.class, DefaultFloatHandler.class);
        registerTypeHandlerClassFor(float.class, DefaultFloatHandler.class);
        registerTypeHandlerClassFor(Double.class, DefaultDoubleHandler.class);
        registerTypeHandlerClassFor(double.class, DefaultDoubleHandler.class);
        registerTypeHandlerClassFor(BigDecimal.class, DefaultBigDecimalHandler.class);
        registerTypeHandlerClassFor(Boolean.class, DefaultBooleanHandler.class);
        registerTypeHandlerClassFor(boolean.class, DefaultBooleanHandler.class);
        registerTypeHandlerClassFor(Enum.class, DefaultEnumHandler.class);
    }

    @Override
    public void clearTypeHandlerMap() {
        handlers.clear();
    }

    @Override
    public void clearRegisterTypeHandlerClass() {
        defaultHandlers.clear();
        registerDefaultHandlers();
    }
}
