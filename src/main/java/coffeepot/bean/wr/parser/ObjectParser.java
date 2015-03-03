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
import coffeepot.bean.wr.annotation.Record;
import coffeepot.bean.wr.annotation.Records;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.FormatType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class ObjectParser {

    //private Class<?> clazz;
    private AccessorType accessorType = AccessorType.DEFAULT;
    private Set<String> ignoredFields;
    private final List<FieldImpl> mappedFields = new LinkedList<>();
    private Class<?> rootClass;
    private final List<FieldImpl> ids = new LinkedList<>();

    /**
     * Uses annotations of a class to create a parser for the target class. Of
     * course that the fields of the target class must be compatible with the
     * annotations of another class.
     *
     * @param fromClass   Class with annotations.
     * @param targetClass Target class.
     * @param groupId
     * @param factory
     */
    public ObjectParser(Class<?> fromClass, Class<?> targetClass, String groupId, ObjectParserFactory factory) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        if (fromClass == null) {
            throw new IllegalArgumentException("Object to mapped can't be null");
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("Target class can't be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("ObjectParserFactory can't be null");
        }

        this.rootClass = targetClass;
        this.perform(factory, getRecordFromClass(fromClass, groupId, factory));
    }

    /**
     * Builds a parser for the class using your annotations.
     *
     * @param clazz
     * @param groupId
     * @param factory
     * @throws UnresolvedObjectParserException
     * @throws NoSuchFieldException
     * @throws Exception
     */
    public ObjectParser(Class<?> clazz, String groupId, ObjectParserFactory factory) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("Object to mapped can't be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("ObjectParserFactory can't be null");
        }

        this.rootClass = clazz;
        this.perform(factory, getRecordFromClass(clazz, groupId, factory));
    }

    private Record getRecordFromClass(Class<?> clazz, String groupId, ObjectParserFactory factory) {
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

    private void perform(ObjectParserFactory factory, Record record) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        if (record != null) {
            accessorType = record.accessorType();
            String[] ig = record.ignoredFields();
            if (ig != null) {
                ignoredFields = new HashSet<>();
                ignoredFields.addAll(Arrays.asList(ig));
            }
            coffeepot.bean.wr.annotation.Field[] fields = record.fields();
            if (fields != null) {
                try {
                    mappingFields(fields, factory);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex);
                }
            }
        }
        if (mappedFields.isEmpty()) {
            throw new UnresolvedObjectParserException("Class " + rootClass.getName() + " can't be mapped");
        }
    }

    private void mappingFields(coffeepot.bean.wr.annotation.Field[] fields, ObjectParserFactory factory) throws NoSuchFieldException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        for (coffeepot.bean.wr.annotation.Field f : fields) {
            if (ignoredFields != null && ignoredFields.contains(f.name())) {
                continue;
            }

            this.mappedFields.add(mappingField(null, Helpful.toFieldImpl(f), factory, rootClass));
        }
    }

    private FieldImpl mappingField(String fieldName, FieldImpl f, ObjectParserFactory factory, Class<?> clazz) throws NoSuchFieldException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        AccessorType at;
        if (f.getAccessorType().equals(AccessorType.DEFAULT)) {
            at = accessorType.equals(AccessorType.PROPERTY) ? AccessorType.PROPERTY : AccessorType.FIELD;
        } else {
            at = f.getAccessorType();
        }

        FieldImpl mappedField = f.clone();
        mappedField.setAccessorType(at);

        if (factory.getFormatType().equals(FormatType.FIXED_LENGTH)) {
            mappedField.setPaddingIfNullOrEmpty(true);
        }

        boolean isNested = false;
        String nestedField = null;

        if (!"".equals(f.getConstantValue())) {
            mappedField.setClassType(String.class);
            if (f.isId()) {
                Class old = factory.getIdsMap().put(f.getConstantValue(), clazz);
                if (old != null) {
                    throw new IllegalStateException("Conflict mapping ids. There is already a class mapped to the id '"
                            + f.getConstantValue() + "' -\n" + old.getName() + "\n-" + clazz.getName());
                }
            }
        } else {

            if (fieldName == null || fieldName.isEmpty()) {
                fieldName = f.getName();
            }

            //check if it is nested field
            int idx = fieldName.indexOf(".");
            if (idx > -1) {
                nestedField = fieldName.substring(idx + 1);
                fieldName = fieldName.substring(0, idx);
                if (!nestedField.isEmpty()) {
                    isNested = true;
                }
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

                        Type genericType = declaredField.getGenericType();
                        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Type[] actualTypeArguments = pt.getActualTypeArguments();
                            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                //FIXME: support for generics with multiple params.
                                mappedField.setClassType((Class<?>) actualTypeArguments[0]);
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

                        Type genericType = m.getGenericReturnType();
                        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Type[] actualTypeArguments = pt.getActualTypeArguments();
                            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                //FIXME: support for generics with multiple params.
                                mappedField.setClassType((Class<?>) actualTypeArguments[0]);
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

        if (f.getNestedFields() != null && f.getNestedFields().size() > 0) {
            isNested = true;
        }

        TypeHandler handler;
        if (!isNested) {
            handler = factory.getHandlerFactory().create(mappedField.getClassType(), f.getTypeHandler(), f.getParams());
            mappedField.setTypeHandlerImpl(handler);

            if (mappedField.getTypeHandlerImpl() == null && (mappedField.getClassType().isEnum())) {
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
                handler = factory.getHandlerFactory().create(Enum.class, f.getTypeHandler(), newParams);
                mappedField.setTypeHandlerImpl(handler);
            }
        }

        if (mappedField.getTypeHandlerImpl() == null && !isNested) {
            factory.getNoResolved().add(mappedField.getClassType());
        } else if (isNested) {
            if (nestedField != null) {
                FieldImpl n = mappingField(nestedField, f, factory, mappedField.getClassType());
                if (n != null) {
                    List<FieldImpl> nfs = new ArrayList<>();
                    nfs.add(n);
                    mappedField.setNestedFields(nfs);
                } else {
                    factory.getNoResolved().add(mappedField.getClassType());
                }
            } else {
                List<FieldImpl> nfs = new ArrayList<>();
                for (FieldImpl nf : f.getNestedFields()) {
                    nfs.add(mappingField(nf.getName(), nf, factory, mappedField.getClassType()));
                }
                mappedField.setNestedFields(nfs);
            }
            mappedField.setTypeHandlerImpl(null);
        }
        return mappedField;
    }

    public Class<?> getRootClass() {
        return rootClass;
    }

    public AccessorType getAccessorType() {
        return accessorType;
    }

    public Set<String> getIgnoredFields() {
        return ignoredFields;
    }

    public List<FieldImpl> getMappedFields() {
        return mappedFields;
    }
}
