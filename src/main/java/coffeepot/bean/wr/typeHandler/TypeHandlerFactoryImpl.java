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
import coffeepot.bean.wr.mapper.Command;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
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

    private final Map<String, TypeHandler> handlers = new HashMap<>();
    private final Map<Class<?>, Class<? extends TypeHandler>> defaultHandlers = new HashMap<>();

    public TypeHandlerFactoryImpl() {
        registerDefaultHandlers();
    }

    private String getHandlerKey(String handlerName, Command[] commands) {
        if (commands == null || commands.length == 0) {
            return handlerName + "{default}";
        }
        int hashCode = Arrays.hashCode(commands);

        StringBuilder sb = new StringBuilder();
        sb.append(handlerName);
        sb.append('{');
        sb.append(hashCode);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public TypeHandler create(Class<?> forClass, Command[] commands) throws InstantiationException, IllegalAccessException {
        String key;
        TypeHandler handler = null;

        if (TypeHandler.class.isAssignableFrom(forClass)) {
            key = getHandlerKey(forClass.getName(), commands);
            handler = handlers.get(key);
            if (handler != null) {
                return handler;
            }
            try {
                handler = (TypeHandler) forClass.newInstance();
                handler.config(commands);
                handlers.put(key, handler);
                return handler;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(TypeHandlerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }

        Class<? extends TypeHandler> defHandler = defaultHandlers.get(forClass);
        if (defHandler != null) {
            key = getHandlerKey(defHandler.getName(), commands);
            handler = handlers.get(key);
            if (handler != null) {
                return handler;
            }

            try {
                handler = defHandler.newInstance();
                handler.config(commands);
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
    public TypeHandler create(Class<?> forClass, Class<? extends TypeHandler> typeHandler, Command[] commands) throws InstantiationException, IllegalAccessException {
        if (DefaultHandler.class.equals(typeHandler)) {
            return create(forClass, commands);
        } else {
            return create(typeHandler, commands);
        }
    }

    @Override
    public void registerTypeHandler(Class<?> forClass, Class<? extends TypeHandler> handlerClass) {
        defaultHandlers.put(forClass, handlerClass);
    }

    @Override
    public void registerTypeHandlerInstance(Class<?> forClass, TypeHandler handlerInstance) {
        if (forClass == null) {
            throw new IllegalArgumentException("Parameter 'forClass' can not be null");
        }
        if (handlerInstance == null) {
            throw new IllegalArgumentException("Parameter 'handlerInstance' can not be null");
        }
        registerTypeHandler(forClass, handlerInstance.getClass());
        String key = getHandlerKey(handlerInstance.getClass().getName(), null);
        handlers.put(key, handlerInstance);
    }

    private void registerDefaultHandlers() {
        registerTypeHandler(String.class, DefaultStringHandler.class);
        registerTypeHandler(Character.class, DefaultCharacterHandler.class);
        registerTypeHandler(char.class, DefaultCharacterHandler.class);
        registerTypeHandler(Integer.class, DefaultIntegerHandler.class);
        registerTypeHandler(int.class, DefaultIntegerHandler.class);
        registerTypeHandler(Long.class, DefaultLongHandler.class);
        registerTypeHandler(long.class, DefaultLongHandler.class);
        registerTypeHandler(Date.class, DefaultDateHandler.class);
        registerTypeHandler(Float.class, DefaultFloatHandler.class);
        registerTypeHandler(float.class, DefaultFloatHandler.class);
        registerTypeHandler(Double.class, DefaultDoubleHandler.class);
        registerTypeHandler(double.class, DefaultDoubleHandler.class);
        registerTypeHandler(BigDecimal.class, DefaultBigDecimalHandler.class);
        registerTypeHandler(Boolean.class, DefaultBooleanHandler.class);
        registerTypeHandler(boolean.class, DefaultBooleanHandler.class);
        registerTypeHandler(Enum.class, DefaultEnumHandler.class);
        registerTypeHandler(LocalDate.class, DefaultLocalDateHandler.class);
        registerTypeHandler(LocalDateTime.class, DefaultLocalDateTimeHandler.class);
        registerTypeHandler(LocalTime.class, DefaultLocalTimeHandler.class);
        registerTypeHandler(Instant.class, DefaultInstantHandler.class);
        registerTypeHandler(ZonedDateTime.class, DefaultZonedDateTimeHandler.class);
        registerTypeHandler(OffsetDateTime.class, DefaultOffsetDateTimeHandler.class);
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
