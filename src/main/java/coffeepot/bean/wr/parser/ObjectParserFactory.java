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
public final class ObjectParserFactory {

    private Set<Class> noResolved = new HashSet<>();

    private Map<Class, ObjectParser> parsers = new HashMap<>();

    private TypeHandlerFactory handlerFactory = new TypeHandlerFactoryImpl();
    
    private Map<String, ObjectMapper> idsMap= new HashMap<>();

    private FormatType formatType;

    public ObjectParserFactory(FormatType formatType) {
        this.formatType = formatType;
    }

    public void createByAnotherClass(Class<?> fromClass, Class<?> targetClass) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        createByAnotherClass(fromClass, targetClass, null);
    }

    public void createByAnotherClass(Class<?> fromClass, Class<?> targetClass, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        ObjectParser objectParser;
        try {
            objectParser = new ObjectParser(fromClass, targetClass, recordGroupId, this);
            parsers.put(targetClass, objectParser);
            noResolved.remove(targetClass);
            while (!noResolved.isEmpty()) {
                Class next = noResolved.iterator().next();
                createHelper(next, recordGroupId);
            }
        } catch (UnresolvedObjectParserException ex) {
            //Logger.getLogger(ObjectParserFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public ObjectParser create(Class<?> clazz) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        return create(clazz, null);
    }

    public ObjectParser create(Class<?> clazz, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        ObjectParser objectParser = parsers.get(clazz);
        if (objectParser != null) {
            return objectParser;
        }

        try {
            objectParser = new ObjectParser(clazz, recordGroupId, this);
            parsers.put(clazz, objectParser);
            noResolved.remove(clazz);
            while (!noResolved.isEmpty()) {
                Class next = noResolved.iterator().next();
                createHelper(next, recordGroupId);
            }
            return objectParser;
        } catch (UnresolvedObjectParserException ex) {
            //Logger.getLogger(ObjectParserFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    private void createHelper(Class<?> clazz, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        ObjectParser objectParser = parsers.get(clazz);
        if (objectParser != null) {
            noResolved.remove(clazz);
            return;
        }

        objectParser = new ObjectParser(clazz, recordGroupId, this);
        parsers.put(clazz, objectParser);
        noResolved.remove(clazz);
    }

    public Set<Class> getNoResolved() {
        return noResolved;
    }

    public Map<Class, ObjectParser> getParsers() {
        return parsers;
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
