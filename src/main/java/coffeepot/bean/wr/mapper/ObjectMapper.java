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
import coffeepot.bean.wr.annotation.Record;
import coffeepot.bean.wr.annotation.Records;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.FormatType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class ObjectMapper {

    private AccessorType accessorType = AccessorType.DEFAULT;
    private final List<FieldModel> fields = new LinkedList<>();
    private Class<?> rootClass;

    /**
     * Builds a parser for the class using your annotations.
     *
     * @param clazz
     * @param groupId
     * @param factory
     * @throws UnresolvedObjectMapperException
     * @throws NoSuchFieldException
     * @throws Exception
     */
    public ObjectMapper(Class<?> clazz, String groupId, ObjectMapperFactory factory) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("Object to mapped can't be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("ObjectMapperFactory can't be null");
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            if (!List.class.isAssignableFrom(clazz) && !Set.class.isAssignableFrom(clazz)) {
                throw new RuntimeException("Only classes derived from Set and List are supported");
            }
            this.rootClass = (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        } else {
            this.rootClass = clazz;
        }
        this.perform(factory, getRecordFromClass(this.rootClass, groupId, factory), groupId);
    }

    public ObjectMapper(Class<?> clazz, String groupId, ObjectMapperFactory factory, RecordModel record) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("Object to mapped can't be null");
        }
        if (record == null) {
            throw new IllegalArgumentException("Record can't be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("ObjectMapperFactory can't be null");
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            if (!List.class.isAssignableFrom(clazz) && !Set.class.isAssignableFrom(clazz)) {
                throw new RuntimeException("Only classes derived from Set and List are supported");
            }
            this.rootClass = (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        } else {
            this.rootClass = clazz;
        }
        this.perform2(factory, record, groupId);
    }

    private Record getRecordFromClass(Class<?> clazz, String groupId, ObjectMapperFactory factory) {
        Record record = null;
        Records records = clazz.getAnnotation(Records.class);
        if (records != null) {
            Record[] value = records.value();
            for (Record rec : value) {
                if (rec.forFormat().equals(FormatType.ANY)) {
                    record = rec;
                } else if (rec.forFormat().equals(factory.getFormatType())) {
                    record = rec;
                    break;
                }
            }
            if (groupId != null && !"".equals(groupId)) {
                for (Record rec : value) {
                    if (rec.groupId().equals(groupId)) {
                        record = rec;
                        break;
                    }
                }
            }
        }
        if (record == null) {
            record = clazz.getAnnotation(Record.class);
        }
        return record;
    }

    //step2
    private void perform(ObjectMapperFactory factory, Record record, String groupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        if (record != null) {
            accessorType = record.accessorType();

            coffeepot.bean.wr.annotation.Field[] fields = record.fields();
            if (fields != null) {
                try {
                    mappingFields(fields, factory, groupId);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ObjectMapper.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex);
                }
            }
        }
        if (fields.isEmpty()) {
            throw new UnresolvedObjectMapperException("Class " + rootClass.getName() + " can't be mapped");
        }
    }

    private void perform2(ObjectMapperFactory factory, RecordModel record, String groupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        if (record != null) {
            accessorType = record.getAccessorType();

            List<FieldModel> fields = record.getFields();
            if (fields != null) {
                try {
                    mappingFields(fields, factory, groupId);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ObjectMapper.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex);
                }
            }
        }
        if (fields.isEmpty()) {
            throw new UnresolvedObjectMapperException("Class " + rootClass.getName() + " can't be mapped");
        }
    }

    private void mappingFields(coffeepot.bean.wr.annotation.Field[] fields, ObjectMapperFactory factory, String groupId) throws Exception {
        for (coffeepot.bean.wr.annotation.Field f : fields) {
            this.fields.add(mappingField(null, Helpful.toFieldImpl(f), factory, rootClass, groupId));
        }
    }

    private void mappingFields(List<FieldModel> fields, ObjectMapperFactory factory, String groupId) throws Exception {
        for (FieldModel f : fields) {
            if (f.getLength() > 0) {
                f.setMinLength(f.getLength());
                f.setMaxLength(f.getLength());
            }
            this.fields.add(mappingField(null, f, factory, rootClass, groupId));
        }
    }

    private FieldModel mappingField(String fieldName, FieldModel f, ObjectMapperFactory factory, Class<?> clazz, String groupId) throws Exception {

        AccessorType at;
        if (f.getAccessorType().equals(AccessorType.DEFAULT)) {
            at = accessorType.equals(AccessorType.PROPERTY) ? AccessorType.PROPERTY : AccessorType.FIELD;
        } else {
            at = f.getAccessorType();
        }

        FieldModel mappedField = f.clone();
        mappedField.setAccessorType(at);

        if (factory.getFormatType().equals(FormatType.FIXED_LENGTH)) {
            mappedField.setPaddingIfNullOrEmpty(true);
        }

        if (f.getConstantValue() != null && !"".equals(f.getConstantValue())) {
            mappedField.setClassType(String.class);
            if (f.isId()) {
                ObjectMapper old = factory.getIdsMap().put(f.getConstantValue().trim(), this);
                if (old != null && !old.getRootClass().equals(this.rootClass)) {
                    throw new IllegalStateException("Conflict mapping ids. There is already a class mapped to the id '"
                            + f.getConstantValue() + "' -\n" + old.getRootClass().getName() + "\n-" + clazz.getName());
                }
            }
        } else {

            if (fieldName == null || fieldName.isEmpty()) {
                fieldName = f.getName();
            }

            mappedField.setName(fieldName);

            try {
                if (mappedField.getAccessorType().equals(AccessorType.FIELD)) {
                    java.lang.reflect.Field declaredField = clazz.getDeclaredField(fieldName);
                    mappedField.setClassType(declaredField.getType());

                    if (!Class.class.equals(f.getClassType())) {
                        mappedField.setClassType(f.getClassType());
                    } else if (Collection.class.isAssignableFrom(mappedField.getClassType())) {
                        mappedField.setCollection(true);
                        mappedField.setCollectionType(mappedField.getClassType());
                        Type genericType = declaredField.getGenericType();
                        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Type[] actualTypeArguments = pt.getActualTypeArguments();
                            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                //FIXME: support for generics with multiple params.
                                mappedField.setClassType((Class<?>) actualTypeArguments[0]);
                                factory.getNoResolved().add(mappedField.getClassType());
                            }
                        }
                    }

                } else {
                    // accessor type is PROPERTY
                    Method m = null;

                    //define getter
                    if (f.getGetter() != null && !f.getGetter().isEmpty()) {
                        m = clazz.getMethod(f.getGetter());
                    } else {
                        try {
                            String methodName = "get" + mappedField.getName().substring(0, 1).toUpperCase();
                            if (mappedField.getName().length() > 1) {
                                methodName += mappedField.getName().substring(1);
                            }
                            m = clazz.getMethod(methodName);
                        } catch (NoSuchMethodException ex) {
                            String methodName = "is" + mappedField.getName().substring(0, 1).toUpperCase();
                            if (mappedField.getName().length() > 1) {
                                methodName += mappedField.getName().substring(1);
                            }
                            m = clazz.getMethod(methodName);
                        }
                    }

                    mappedField.setGetterMethod(m);

                    mappedField.setClassType(m.getReturnType());

                    if (!Class.class.equals(f.getClassType())) {
                        mappedField.setClassType(f.getClassType());
                    } else if (Collection.class.isAssignableFrom(mappedField.getClassType())) {
                        mappedField.setCollection(true);
                        mappedField.setCollectionType(mappedField.getClassType());

                        Type genericType = m.getGenericReturnType();
                        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Type[] actualTypeArguments = pt.getActualTypeArguments();
                            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                mappedField.setClassType((Class<?>) actualTypeArguments[0]);
                                factory.getNoResolved().add(mappedField.getClassType());
                            }
                        }
                    }

                    Class<?> classForSetterMethodParam;
                    classForSetterMethodParam = m.getReturnType();

                    //define setter
                    try {
                        if (f.getSetter() != null && !f.getSetter().isEmpty()) {
                            m = clazz.getMethod(f.getSetter(), classForSetterMethodParam);
                        } else if (mappedField.getName() == null || mappedField.getName().isEmpty()) {
                            mappedField.setIgnoreOnRead(true);
                        } else {
                            String methodName = "set" + mappedField.getName().substring(0, 1).toUpperCase();
                            if (mappedField.getName().length() > 1) {
                                methodName += mappedField.getName().substring(1);
                            }
                            m = clazz.getMethod(methodName, classForSetterMethodParam);
                        }

                        mappedField.setSetterMethod(m);
                    } catch (NoSuchMethodException exc) {
                        mappedField.setIgnoreOnRead(true);
                    }
                }
            } catch (NoSuchFieldException | NoSuchMethodException ex) {
                if ("".equals(f.getGetter()) && "".equals(f.getSetter()) && !mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                    throw ex;
                }

                if (Class.class.equals(mappedField.getClassType())) {
                    throw new NoSuchFieldException("Class not defined for method mode");
                }

                if (!"".equals(f.getGetter())) {
                    mappedField.setGetterMethod(clazz.getDeclaredMethod(f.getGetter()));
                } else if (mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                    // for inherited methods
                    try {
                        String methodName = "get" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }
                        mappedField.setGetterMethod(clazz.getMethod(methodName));

                    } catch (Exception e) {
                        mappedField.setIgnoreOnWrite(true);
                    }
                } else {
                    mappedField.setIgnoreOnWrite(true);
                }

                if (!"".equals(f.getSetter())) {
                    mappedField.setSetterMethod(clazz.getDeclaredMethod(f.getSetter(), f.getClassType()));
                } else if (mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                    // for inherited methods
                    try {
                        String methodName = "set" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }
                        mappedField.setSetterMethod(clazz.getMethod(methodName, mappedField.getClassType()));
                    } catch (Exception e) {
                        mappedField.setIgnoreOnRead(true);
                    }
                } else {
                    mappedField.setIgnoreOnRead(true);
                }

            }
        }

        if (!Class.class.equals(f.getClassType())) {
            mappedField.setClassType(f.getClassType());
        }

        TypeHandler handler;

        handler = factory.getHandlerFactory().create(mappedField.getClassType(), f.getTypeHandlerClass(), f.getParams());
        mappedField.setTypeHandler(handler);

        if (mappedField.getTypeHandler() == null && (mappedField.getClassType().isEnum())) {
            //set default EnumTypeHandler
            boolean defEnum = false;
            if (f.getParams() != null) {
                for (String s : f.getParams()) {
                    if (s.startsWith("enum") || s.startsWith("class")) {
                        defEnum = true;
                        break;
                    }
                }
            }
            String[] newParams;
            if (!defEnum) {
                if (f.getParams() == null) {
                    newParams = new String[1];
                } else {
                    newParams = Arrays.copyOf(f.getParams(), f.getParams().length + 1);
                }
                newParams[newParams.length - 1] = "enumClass=" + mappedField.getClassType().getName();
            } else {
                newParams = f.getParams();
            }
            handler = factory.getHandlerFactory().create(Enum.class, f.getTypeHandlerClass(), newParams);
            mappedField.setTypeHandler(handler);
        }

        if (mappedField.getTypeHandler() == null) {
            mappedField.setNestedObject(true);
            factory.getNoResolved().add(mappedField.getClassType());
        }
        return mappedField;
    }

    public Class<?> getRootClass() {
        return rootClass;
    }

    public AccessorType getAccessorType() {
        return accessorType;
    }

    public List<FieldModel> getFields() {
        return fields;
    }
}
