/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.parser;

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

    private final Set<Class> noResolved = new HashSet<>();
    private final Map<Class, ObjectMapper> mappers = new HashMap<>();
    private final Map<String, ObjectMapper> idsMap = new HashMap<>();
    private final FormatType formatType;
    private TypeHandlerFactory handlerFactory = new TypeHandlerFactoryImpl();

    public ObjectMapperFactory(FormatType formatType) {
        this.formatType = formatType;
    }

    public ObjectMapper create(Class<?> clazz) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        return create(clazz, null);
    }

    public ObjectMapper create(Class<?> clazz, String recordGroupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        ObjectMapper objectMapper = mappers.get(clazz);
        if (objectMapper != null) {
            return objectMapper;
        }

        try {
            objectMapper = new ObjectMapper(clazz, recordGroupId, this);
            mappers.put(clazz, objectMapper);
            noResolved.remove(clazz);
            while (!noResolved.isEmpty()) {
                Class next = noResolved.iterator().next();
                createHelper(next, recordGroupId);
            }
            return objectMapper;
        } catch (UnresolvedObjectMapperException ex) {
            //Logger.getLogger(ObjectMapperFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    private void createHelper(Class<?> clazz, String recordGroupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        ObjectMapper objectMapper = mappers.get(clazz);
        if (objectMapper != null) {
            noResolved.remove(clazz);
            return;
        }

        objectMapper = new ObjectMapper(clazz, recordGroupId, this);
        mappers.put(clazz, objectMapper);
        noResolved.remove(clazz);
    }

    public Set<Class> getNoResolved() {
        return noResolved;
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
