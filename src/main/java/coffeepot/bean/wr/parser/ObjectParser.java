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


import coffeepot.bean.wr.anotation.Record;
import coffeepot.bean.wr.anotation.Records;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import coffeepot.bean.wr.types.FormatType;
import coffeepot.bean.wr.writer.ObjectWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
    private List<FieldImpl> mappedFields = new LinkedList<>();
    private Class<?> classRoot;

    /**
     * Uses annotations of a class to create a parser for the target class. Of
     * course that the fields of the target class must be compatible with the
     * annotations of another class.
     *
     * @param fromClass Class with annotations.
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

        this.classRoot = targetClass;
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

        this.classRoot = clazz;
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
            coffeepot.bean.wr.anotation.Field[] fields = record.fields();
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
            throw new UnresolvedObjectParserException("Class " + classRoot.getName() + " can't be mapped");
        }
    }

    private void mappingFields(coffeepot.bean.wr.anotation.Field[] fields, ObjectParserFactory factory) throws NoSuchFieldException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        for (coffeepot.bean.wr.anotation.Field f : fields) {
            if (ignoredFields != null && ignoredFields.contains(f.name())) {
                continue;
            }

            this.mappedFields.add(mappingField(null, Helpful.toFieldImpl(f), factory, classRoot));
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

                    Method m;

                    //if define getter
                    if (f.getGetter() != null && !f.getGetter().isEmpty()) {
                        m = clazz.getDeclaredMethod(f.getGetter());
                    } else {
                        String methodName = "get" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }
                        m = clazz.getDeclaredMethod(methodName);
                    }

                    mappedField.setGetterMethod(m);

                    Class<?> classForSetterMethodParam;
                    classForSetterMethodParam = declaredField.getType();


                    //if define setter
                    if (f.getSetter() != null && !f.getSetter().isEmpty()) {
                        m = clazz.getDeclaredMethod(f.getSetter(), classForSetterMethodParam);
                    } else {
                        String methodName = "set" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }
                        m = clazz.getDeclaredMethod(methodName, classForSetterMethodParam);
                    }

                    mappedField.setSetterMethod(m);

                } else {
                    // accessor type is PROPERTY
                    Method m = null;

                    //define getter
                    if (f.getGetter() != null && !f.getGetter().isEmpty()) {
                        m = clazz.getMethod(f.getGetter());
                    } else {

                        String methodName = "get" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }
                        m = clazz.getMethod(methodName);

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
                    newParams[ newParams.length - 1] = "enumClass=" + mappedField.getClassType().getName();
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

    public Class<?> getClazz() {
        return classRoot;
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

    public void marshal(ObjectWriter w, Object obj) throws IOException {
        List<String> fieldsValue = marshal(w, obj, null);
        if (fieldsValue != null && !fieldsValue.isEmpty()) {
            w.writeRecord(fieldsValue);
        }
    }

    private List<String> marshalField(ObjectWriter w, final Object obj, List<String> fieldsValue, Class<?> clazz, final FieldImpl f) throws IOException {
        try {
            Object o = null;
            if (obj != null) {

               final java.lang.reflect.Field declaredField;


                if (f.getGetterMethod() != null) {

                   o = AccessController.doPrivileged(new PrivilegedAction() {
                        @Override
                        public Object run() {
                            boolean wasAccessible = f.getGetterMethod().isAccessible();
                            try {
                                f.getGetterMethod().setAccessible(true);
                                return f.getGetterMethod().invoke(obj);     
                            } catch (Exception ex) {
                                throw new IllegalStateException("Cannot invoke method get", ex);
                            } finally {
                                f.getGetterMethod().setAccessible(wasAccessible);
                            }
                        }
                    });

                } else {
                    declaredField = clazz.getDeclaredField(f.getName());
                    o = AccessController.doPrivileged(new PrivilegedAction() {
                        @Override
                        public Object run() {
                            boolean wasAccessible = declaredField.isAccessible();
                            try {
                                declaredField.setAccessible(true);
                                return declaredField.get(obj);     
                            } catch (Exception ex) {
                                throw new IllegalStateException("Cannot invoke method get", ex);
                            } finally {
                                declaredField.setAccessible(wasAccessible);
                            }
                        }
                    });
                }

                if (f.isCollection()) {
                    if (f.isSegmentBeginNewRecord() || f.isBeginNewRecord()) {
                        w.writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                    fieldsValue = marshalCollection(w, o, fieldsValue, f);
                    if (f.isSegmentBeginNewRecord() || f.isBeginNewRecord()) {
                        w.writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                    return fieldsValue;
                }

                if (//f.getTypeHandlerImpl() == null && 
                        f.getNestedFields() != null && !f.getNestedFields().isEmpty()) {
                    if (f.isBeginNewRecord()) {
                        w.writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                    if (o == null && !f.isRequired()) {
                        return fieldsValue;
                    }
                    return marshal(w, o, fieldsValue, f.getNestedFields(), f.getClassType());

                } else if (f.getTypeHandlerImpl() == null) {
                    if (f.isSegmentBeginNewRecord() || f.isBeginNewRecord()) {
                        w.writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                    ObjectParser parser = w.getObjectParserFactory().getParsers().get(f.getClassType());
                    if (parser != null) {
                        fieldsValue = parser.marshal(w, o, fieldsValue);
                        if (f.isSegmentBeginNewRecord() || f.isBeginNewRecord()) {
                            w.writeRecord(fieldsValue);
                            fieldsValue = null;
                        }
                    } else {
                        throw new RuntimeException("Parser not found for class: " + f.getClassType().getName());
                    }
                    return fieldsValue;
                }
            }
            if (f.isBeginNewRecord()) {
                w.writeRecord(fieldsValue);
                fieldsValue = null;
            }

            String s = process(f.getTypeHandlerRecursively().toString(o), f);
            if (fieldsValue == null) {
                fieldsValue = new LinkedList<>();
            }
            fieldsValue.add(s);


            //FIXME: Exceptions
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldsValue;
    }

    private List<String> marshal(ObjectWriter w, Object obj, List<String> fieldsValue) throws IOException {
        if (obj == null) {
            return fieldsValue;
        }
        return marshal(w, obj, fieldsValue, mappedFields, classRoot);
    }

    private List<String> marshal(ObjectWriter w, Object obj, List<String> fieldsValue, List<FieldImpl> fields, Class<?> clazz) throws IOException {
//        if (obj == null) {
//            return fieldsValue;
//        }

        for (FieldImpl f : fields) {
            if (f.isIgnoreOnWrite()) {
                continue;
            }

            if (!"".equals(f.getConstantValue())) {

                if (f.isBeginNewRecord()) {
                    w.writeRecord(fieldsValue);
                    fieldsValue = null;
                }
                if (fieldsValue == null) {
                    fieldsValue = new LinkedList<>();
                }
                String s = process(f.getConstantValue(), f);
                fieldsValue.add(s);
                continue;
            }
            fieldsValue = marshalField(w, obj, fieldsValue, clazz, f);
        }

        return fieldsValue;
    }

    private List<String> marshalCollection(ObjectWriter w, Object obj, List<String> fieldsValue, FieldImpl field) throws IOException {
        if (obj == null) {
            return fieldsValue;
        }
        if (!Collection.class.isAssignableFrom(obj.getClass())) {
            return fieldsValue;
        }
        if (field.getNestedFields() != null && !field.getNestedFields().isEmpty()) {
            Collection c = (Collection) obj;
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Class<?> cl;
                List<FieldImpl> fi;

                fi = field.getNestedFields();
                cl = field.getClassType();

                fieldsValue = marshal(w, it.next(), fieldsValue, fi, cl);
                if (field.isSegmentBeginNewRecord()) {
                    w.writeRecord(fieldsValue);
                    fieldsValue = null;
                }
            }
        } else {

            ObjectParser parser = w.getObjectParserFactory().getParsers().get(field.getClassType());
            if (parser != null) {
                Collection c = (Collection) obj;
                Iterator it = c.iterator();
                while (it.hasNext()) {
                    Class<?> cl;
                    List<FieldImpl> fi;
                    if (field.getNestedFields() != null && !field.getNestedFields().isEmpty()) {
                        fi = field.getNestedFields();
                        cl = field.getClassType();
                    } else {
                        fi = parser.getMappedFields();
                        cl = parser.getClazz();
                    }
                    fieldsValue = parser.marshal(w, it.next(), fieldsValue, fi, cl);
                    if (field.isSegmentBeginNewRecord()) {
                        w.writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                }
            }
        }
        return fieldsValue;
    }

    private String process(String s, FieldImpl field) {
        if (s == null && !field.isPaddingIfNullOrEmpty()) {
            return "";
        }

        if (s == null) {
            s = "";
        }

        if (field.isTrim()) {
            s = s.trim();
        }

        if (s.isEmpty() && !field.isPaddingIfNullOrEmpty()) {
            return s;
        }

        if (field.getMaxLength() > 0 && s.length() > field.getMaxLength()) {
            if (field.getAlign().equals(Align.LEFT)) {
                s = s.substring(0, field.getMaxLength());
            } else {
                s = s.substring(s.length() - field.getMaxLength(), s.length());
            }
        }

        if (field.getMinLength() > 0) {
            if (s.length() < field.getMinLength()) {
                StringBuilder sb = new StringBuilder();
                int i = field.getMinLength() - s.length();
                if (field.getAlign().equals(Align.LEFT)) {
                    sb.append(s);
                }
                for (int j = 0; j < i; j++) {
                    sb.append(field.getPadding());
                }
                if (!field.getAlign().equals(Align.LEFT)) {
                    sb.append(s);
                }
                s = sb.toString();
            }
        }
        return s;
    }
}
