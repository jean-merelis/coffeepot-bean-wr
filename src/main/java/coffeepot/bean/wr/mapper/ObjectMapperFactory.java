/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.mapper;

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
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactoryImpl;
import coffeepot.bean.wr.types.FormatType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeandeson O. Merelis
 */
public final class ObjectMapperFactory {

    private final Set<Class> unresolved = new HashSet<>();
    private final Map<Class, ObjectMapper> mappers = new HashMap<>();
    private final Map<String, ObjectMapper> idsMap = new HashMap<>();
    private final FormatType formatType;
    private TypeHandlerFactory handlerFactory = new TypeHandlerFactoryImpl();

    public ObjectMapperFactory(FormatType formatType) {
        this.formatType = formatType;
    }

    public ObjectMapper create(Class<?> clazz) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        return create(clazz, (String) null, (Callback) null);
    }

    public ObjectMapper create(Class<?> clazz, String recordGroupId, Callback<Class, RecordModel> callback) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        ObjectMapper objectMapper = mappers.get(clazz);
        if (objectMapper != null) {
            return objectMapper;
        }

        if (callback != null) {
            RecordModel rm = callback.call(clazz);
            if (rm != null) {
                objectMapper = new ObjectMapper(clazz, recordGroupId, this, rm);
            }
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(clazz, recordGroupId, this);
        }

        mappers.put(clazz, objectMapper);
        unresolved.remove(clazz);
        while (!unresolved.isEmpty()) {
            Class next = unresolved.iterator().next();
            createHelper(next, recordGroupId, callback);
        }
        return objectMapper;
    }

    public ObjectMapper create(Class<?> clazz, RecordModel record) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        return create(clazz, record, (String) null, (Callback) null);
    }

    public ObjectMapper create(Class<?> clazz, RecordModel record, String recordGroupId, Callback<Class, RecordModel> callback) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        mappers.remove(clazz);

        ObjectMapper objectMapper = null;
        if (callback != null) {
            RecordModel rm = callback.call(clazz);
            if (rm != null) {
                objectMapper = new ObjectMapper(clazz, recordGroupId, this, rm);
            }
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(clazz, recordGroupId, this);
        }

        mappers.put(clazz, objectMapper);
        unresolved.remove(clazz);
        while (!unresolved.isEmpty()) {
            Class next = unresolved.iterator().next();
            createHelper(next, recordGroupId, callback);
        }
        return objectMapper;

    }

    private void createHelper(Class<?> clazz, String recordGroupId, Callback<Class, RecordModel> callback) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        ObjectMapper objectMapper = mappers.get(clazz);
        if (objectMapper != null) {
            unresolved.remove(clazz);
            return;
        }
        objectMapper = null;
        if (callback != null) {
            RecordModel rm = callback.call(clazz);
            if (rm != null) {
                objectMapper = new ObjectMapper(clazz, recordGroupId, this, rm);
            }
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(clazz, recordGroupId, this);
        }

        mappers.put(clazz, objectMapper);
        unresolved.remove(clazz);
    }

    public Set<Class> getUnresolved() {
        return unresolved;
    }

    public Map<Class, ObjectMapper> getMappers() {
        return mappers;
    }

    public TypeHandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    public void setHandlerFactory(TypeHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public FormatType getFormatType() {
        return formatType;
    }

    public Map<String, ObjectMapper> getIdsMap() {
        return idsMap;
    }

}
