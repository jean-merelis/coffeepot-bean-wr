/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.parser;

import coffeepot.bean.wr.anotation.Record;
import coffeepot.bean.wr.anotation.Records;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import coffeepot.bean.wr.types.FormatType;
import coffeepot.bean.wr.writer.ObjectWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    private Class<?> clazz;
    private AccessorType accessorType = AccessorType.DEFAULT;
    private Set<String> ignoredFields;
    private List<Field> mappedFields = new LinkedList<>();

    public ObjectParser(Class<?> clazz, ObjectParserFactory factory) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("Object to wrapper can't be null");
        }

        if (factory == null) {
            throw new IllegalArgumentException("ObjectParserFactory can't be null");
        }

        this.clazz = clazz;
        this.perform(factory);
    }

    private void perform(ObjectParserFactory factory) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
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
        }
        if (record == null) {
            record = clazz.getAnnotation(Record.class);
        }

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
            throw new UnresolvedObjectParserException("Class " + clazz.getName() + " can't be mapped");
        }
    }

    private void mappingFields(coffeepot.bean.wr.anotation.Field[] fields, ObjectParserFactory factory) throws NoSuchFieldException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        for (coffeepot.bean.wr.anotation.Field f : fields) {
            if (ignoredFields != null && ignoredFields.contains(f.name())) {
                continue;
            }

            AccessorType at;
            if (f.accessorType().equals(AccessorType.DEFAULT)) {
                at = accessorType.equals(AccessorType.PROPERTY) ? AccessorType.PROPERTY : AccessorType.FIELD;
            } else {
                at = f.accessorType();
            }

            Field mappedField = new Field();
            mappedField.setAccessorType(at);
            mappedField.setAlign(f.align());

            mappedField.setMaxLength(f.maxLength());
            mappedField.setMinLength(f.minLength());

            if (f.length() > 0) {
                mappedField.setMaxLength(f.length());
                mappedField.setMinLength(f.length());
            }

            mappedField.setName(f.name());
            mappedField.setPadding(f.padding());
            mappedField.setPaddingIfNullOrEmpty(f.paddingIfNullOrEmpty());

            if (factory.getFormatType().equals(FormatType.FIXED_LENGTH)) {
                mappedField.setPaddingIfNullOrEmpty(true);
            }

            mappedField.setTrim(f.trim());
            mappedField.setSegmentBeginNewRecord(f.segmentBeginNewRecord());
            mappedField.setBeginNewRecord(f.beginNewRecord());
            mappedField.setConstantValue(f.constantValue());

            if (!"".equals(f.constantValue())) {
                mappedField.setClazz(String.class);
            } else {
                try {
                    java.lang.reflect.Field declaredField = clazz.getDeclaredField(f.name());
                    mappedField.setClazz(declaredField.getType());


                    if (Collection.class.isAssignableFrom(declaredField.getType())) {
                        mappedField.setCollection(true);
                        Type genericType = declaredField.getGenericType();
                        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Type[] actualTypeArguments = pt.getActualTypeArguments();
                            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                //FIXME: support for generics with multiple params.
                                mappedField.setClazz((Class<?>) actualTypeArguments[0]);
                            }
                        }
                    }

                    if (!"".equals(f.getter())) {
                        mappedField.setGetter(clazz.getDeclaredMethod(f.getter()));
                    } else if (mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                        String methodName = "get" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }

                        mappedField.setGetter(clazz.getDeclaredMethod(methodName));
                        mappedField.getGetter().setAccessible(true);
                    }
                    if (!"".equals(f.setter())) {
                        mappedField.setSetter(clazz.getDeclaredMethod(f.setter()));
                    } else if (mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                        String methodName = "set" + mappedField.getName().substring(0, 1).toUpperCase();
                        if (mappedField.getName().length() > 1) {
                            methodName += mappedField.getName().substring(1);
                        }
                        Class<?> type = declaredField.getType();
                        mappedField.setSetter(clazz.getDeclaredMethod(methodName, type));
                        mappedField.getSetter().setAccessible(true);
                    }
                } catch (NoSuchFieldException ex) {
                    if ("".equals(f.getter()) && "".equals(f.setter()) && !mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                        throw ex;
                    }

                    if (Class.class.equals(f.classType())) {
                        throw new NoSuchFieldException("Class not defined for method mode");
                    }

                    mappedField.setClazz(f.classType());


                    if (!"".equals(f.getter())) {
                        mappedField.setGetter(clazz.getDeclaredMethod(f.getter()));
                        mappedField.getGetter().setAccessible(true);
                    } else if (mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                        // for inherited methods
                        try {
                            String methodName = "get" + mappedField.getName().substring(0, 1).toUpperCase();
                            if (mappedField.getName().length() > 1) {
                                methodName += mappedField.getName().substring(1);
                            }
                            mappedField.setGetter(clazz.getMethod(methodName));
                            mappedField.getGetter().setAccessible(true);
                        } catch (Exception e) {
                            mappedField.setIgnoreOnWrite(true);
                        }
                    } else {
                        mappedField.setIgnoreOnWrite(true);
                    }


                    if (!"".equals(f.setter())) {
                        mappedField.setSetter(clazz.getDeclaredMethod(f.setter(), f.classType()));
                        mappedField.getSetter().setAccessible(true);
                    } else if (mappedField.getAccessorType().equals(AccessorType.PROPERTY)) {
                        // for inherited methods
                        try {
                            String methodName = "set" + mappedField.getName().substring(0, 1).toUpperCase();
                            if (mappedField.getName().length() > 1) {
                                methodName += mappedField.getName().substring(1);
                            }
                            mappedField.setSetter(clazz.getMethod(methodName, mappedField.getClazz()));
                            mappedField.getSetter().setAccessible(true);
                        } catch (Exception e) {
                            mappedField.setIgnoreOnRead(true);
                        }
                    } else {
                        mappedField.setIgnoreOnRead(true);
                    }


                }
            }

            TypeHandler handler = factory.getHandlerFactory().create(mappedField.getClazz(), f.typeHandler(), f.params());
            mappedField.setTypeHandler(handler);

            if (mappedField.getTypeHandler() == null && (mappedField.getClazz().isEnum())) {
                //set default EnumTypeHandler
                boolean defEnum = false;
                if (f.params() != null) {
                    for (String s : f.params()) {
                        if (s.startsWith("enum") || s.startsWith("class")) {
                            defEnum = true;
                            break;
                        }
                    }
                }
                String[] newParams;
                if (!defEnum) {
                    if (f.params() == null) {
                        newParams = new String[1];
                    } else {
                        newParams = Arrays.copyOf(f.params(), f.params().length + 1);
                    }
                    newParams[ newParams.length - 1] = "enumClass=" + mappedField.getClazz().getName();
                } else {
                    newParams = f.params();
                }
                handler = factory.getHandlerFactory().create(Enum.class, f.typeHandler(), newParams);
                mappedField.setTypeHandler(handler);
            }


            this.mappedFields.add(mappedField);
            if (mappedField.getTypeHandler() == null) {
                factory.getNoResolved().add(mappedField.getClazz());
            }
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public AccessorType getAccessorType() {
        return accessorType;
    }

    public Set<String> getIgnoredFields() {
        return ignoredFields;
    }

    public List<Field> getMappedFields() {
        return mappedFields;
    }

    public void marshal(ObjectWriter w, Object obj) throws IOException {
        List<String> fieldsValue = marshal(w, obj, null);
        if (fieldsValue != null && !fieldsValue.isEmpty()) {
            w.writeRecord(fieldsValue);
        }
    }

    private List<String> marshal(ObjectWriter w, Object obj, List<String> fieldsValue) throws IOException {
        if (obj == null) {
            return fieldsValue;
        }

        try {
            for (Field f : mappedFields) {
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
                Object o;
                java.lang.reflect.Field declaredField;


                if (f.getGetter() != null) {
                    o = f.getGetter().invoke(obj);
                } else {
                    declaredField = clazz.getDeclaredField(f.getName());
                    declaredField.setAccessible(true);
                    o = declaredField.get(obj);
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
                    continue;
                }

                if (f.getTypeHandler() == null) {
                    if (f.isSegmentBeginNewRecord() || f.isBeginNewRecord()) {
                        w.writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                    ObjectParser parser = w.getObjectParserFactory().getParsers().get(f.getClazz());
                    if (parser != null) {
                        fieldsValue = parser.marshal(w, o, fieldsValue);
                        if (f.isSegmentBeginNewRecord() || f.isBeginNewRecord()) {
                            w.writeRecord(fieldsValue);
                            fieldsValue = null;
                        }
                    } else {
                        throw new RuntimeException("Parser not found for class: " + f.getClazz().getName());
                    }
                    continue;
                }

                if (f.isBeginNewRecord()) {
                    w.writeRecord(fieldsValue);
                    fieldsValue = null;
                }

                String s = process(f.getTypeHandler().toString(o), f);
                if (fieldsValue == null) {
                    fieldsValue = new LinkedList<>();
                }
                fieldsValue.add(s);
            }

            //FIXME: Exceptions
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ObjectParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldsValue;
    }

    private List<String> marshalCollection(ObjectWriter w, Object obj, List<String> fieldsValue, Field field) throws IOException {
        if (obj == null) {
            return fieldsValue;
        }
        if (!Collection.class.isAssignableFrom(obj.getClass())) {
            return fieldsValue;
        }
        ObjectParser parser = w.getObjectParserFactory().getParsers().get(field.getClazz());
        if (parser != null) {
            Collection c = (Collection) obj;
            Iterator it = c.iterator();
            while (it.hasNext()) {
                fieldsValue = parser.marshal(w, it.next(), fieldsValue);
                if (field.isSegmentBeginNewRecord()) {
                    w.writeRecord(fieldsValue);
                    fieldsValue = null;
                }

            }
        }
        return fieldsValue;
    }

    private String process(String s, Field field) {
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
