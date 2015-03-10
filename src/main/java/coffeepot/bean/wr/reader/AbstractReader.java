/*
 * Copyright 2015 Jeandeson O. Merelis.
 *
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
 */
package coffeepot.bean.wr.reader;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 - 2015 Jeandeson O. Merelis
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
import coffeepot.bean.wr.mapper.Callback;
import coffeepot.bean.wr.mapper.FieldModel;
import coffeepot.bean.wr.mapper.ObjectMapper;
import coffeepot.bean.wr.mapper.ObjectMapperFactory;
import coffeepot.bean.wr.mapper.RecordModel;
import coffeepot.bean.wr.mapper.UnresolvedObjectMapperException;
import coffeepot.bean.wr.typeHandler.HandlerParseException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public abstract class AbstractReader implements ObjectReader {

    protected String charsetName;
    protected boolean ignoreUnknownRecords;
    protected boolean removeRecordInitializator = true;
    protected boolean trim = true;
    protected String recordInitializator;
    protected int actualLine;
    protected Callback<Class, RecordModel> callback;

    public abstract ObjectMapperFactory getObjectMapperFactory();

    protected void config() {
    }

    @Override
    public <T> T read(InputStream src, Class<T> clazz) {
        return read(src, clazz, null);
    }

    @Override
    public <T> T read(InputStream src, Class<T> clazz, String recordGroupId) {
        clear();
        config();
        try {
            ObjectMapper om = getObjectMapperFactory().create(clazz, recordGroupId, null);
            if (getObjectMapperFactory().getIdsMap().isEmpty()) {
                if (om == null) {
                    throw new RuntimeException("Unable to map the class");
                }
                return unmarshalWithoutId(src, clazz, om);
            }
            return unmarshal(src, clazz);
        } catch (Exception ex) {
            Logger.getLogger(DelimitedReader.class.getName()).log(Level.SEVERE, "Line: " + actualLine, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Callback<Class, RecordModel> getCallback() {
        return callback;
    }

    @Override
    public void setCallback(Callback<Class, RecordModel> callback) {
        this.callback = callback;
    }

    @Override
    public void clearMappers() {
        getObjectMapperFactory().getMappers().clear();
    }

    @Override
    public void createMapper(Class<?> clazz) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        getObjectMapperFactory().create(clazz, null, callback);
    }

    @Override
    public void createMapper(Class<?> clazz, String recordGroupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        getObjectMapperFactory().create(clazz, recordGroupId, callback);
    }

    @Override
    public <T> T parse(String line, Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T parse(String line, Class<T> clazz, String recordGroupId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void clear() {
        getObjectMapperFactory().getIdsMap().clear();
        getObjectMapperFactory().getMappers().clear();
        actualLine = 0;
    }

    protected InputStreamReader createInputStreamReader(InputStream src) throws UnsupportedEncodingException {
        if (charsetName != null && !charsetName.isEmpty()) {
            return new InputStreamReader(src, charsetName);
        }
        return new InputStreamReader(src);
    }

    protected abstract void readLine(BufferedReader reader) throws Exception;

//    protected abstract String[] getNextRecord(BufferedReader reader) throws Exception;
    protected void beforeUnmarshal() {
    }

    protected abstract String getIdValue(boolean fromNext);

    protected abstract String getValueByIndex(int idx);

    protected abstract boolean currentRecordIsNull();

    protected abstract boolean hasNext();

    private <T> T unmarshal(InputStream src, Class<T> clazz) throws Exception {
        beforeUnmarshal();
        T product;

        try (BufferedReader reader = new BufferedReader(createInputStreamReader(src))) {

            if (Collection.class.isAssignableFrom(clazz)) {
                readLine(reader);
                actualLine = 0;
                if (currentRecordIsNull() && hasNext()) {
                    readLine(reader);
                }
                if (currentRecordIsNull()) {
                    return null;
                }
                product = clazz.newInstance();
                while (!currentRecordIsNull()) {
                    Object o = processRecord(reader);
                    if (o != null) {
                        ((Collection) product).add(o);
                    }
                    readLine(reader);
                }
                return product;
            } else {
                readLine(reader);
                actualLine = 0;
                if (!hasNext()) {
                    return null;
                }

                //Check if clazz is a wrapper
                ObjectMapper om = getObjectMapperFactory().getMappers().get(clazz);
                ObjectMapper omm = getObjectMapperFactory().getIdsMap().get(getIdValue(true));

                if (omm == null) {
                    if (!ignoreUnknownRecords) {
                        throw new UnknownRecordException("The record with ID '" + getIdValue(true) + "' is unknown.");
                    }
                } else if (om.getRootClass().equals(omm.getRootClass())) {
                    readLine(reader);

                }
                //--

                product = clazz.newInstance();
                fill(product, om, reader);
                return product;
            }

        }
    }

    //for single class
    private <T> T unmarshalWithoutId(InputStream src, Class<T> clazz, ObjectMapper om) throws Exception {
        beforeUnmarshal();
        T product;

        try (BufferedReader reader = new BufferedReader(createInputStreamReader(src))) {

            if (Collection.class.isAssignableFrom(clazz)) {
                readLine(reader);
                if (currentRecordIsNull() && hasNext()) {
                    readLine(reader);
                }
                if (currentRecordIsNull()) {
                    return null;
                }
                product = clazz.newInstance();
                while (!currentRecordIsNull()) {
                    Object o = processRecordWithoutId(reader, om);
                    if (o != null) {
                        ((Collection) product).add(o);
                    }
                    readLine(reader);
                }
                return product;
            } else {
                readLine(reader);
                if (!hasNext()) {
                    return null;
                }

                //Check if clazz is not a wrapper
                if (om.getRootClass().equals(clazz)) {
                    readLine(reader);
                }
                //--

                product = clazz.newInstance();
                fillWithoutId(product, om, reader);
                return product;
            }

        }
    }

    protected void beforeFill(ObjectMapper om) {
    }

    protected void fill(Object product, ObjectMapper om, BufferedReader reader) throws Exception {
        beforeFill(om);
        List<FieldModel> mappedFields = om.getMappedFields();

        int i = 0;

        for (FieldModel f : mappedFields) {
            if (!f.getConstantValue().isEmpty() || f.isIgnoreOnRead()) {
                i++;
                continue;
            }

            if (!f.isCollection() && !f.isNestedObject() && f.getTypeHandler() != null) {
                try {
                    String v = trim ? getValueByIndex(i).trim() : getValueByIndex(i);
                    Object value = f.getTypeHandler().parse(v);
                    setValue(product, value, f);
                } catch (HandlerParseException ex) {
                    throw new HandlerParseException("Line: " + actualLine + ", field: " + (i + 1), ex);
                }
            } else if (f.isCollection()) {
                if (hasNext()) {
                    //se o proximo registro é um objeto desta collection
                    String nextId = getIdValue(true);
                    ObjectMapper mc = getObjectMapperFactory().getIdsMap().get(nextId);
                    if (mc == null) {
                        if (!ignoreUnknownRecords) {
                            throw new UnknownRecordException("The record with ID '" + nextId + "' is unknown. Line:" + actualLine);
                        }
                    } else {

                        if (mc.getRootClass().equals(f.getClassType())) {
                            Collection c = getCollection(product, f);
                            do {
                                readLine(reader);
                                Object r = processRecord(reader);
                                if (r != null) {
                                    c.add(r);
                                }
                            } while (hasNext() && getIdValue(true).equals(nextId));
                        }
                    }
                }

            } else if (f.isNestedObject() && hasNext()) {
                //se o proximo registro é um objeto deste mesmo tipo
                String nextId = getIdValue(true);
                ObjectMapper mc = getObjectMapperFactory().getIdsMap().get(nextId);
                if (mc == null) {
                    if (!ignoreUnknownRecords) {
                        throw new UnknownRecordException("The record with ID '" + nextId + "' is unknown. Line:" + actualLine);
                    }
                } else {
                    if (mc.getRootClass().equals(f.getClassType())) {
                        readLine(reader);
                        Object r = processRecord(reader);
                        setValue(product, r, f);
                    }
                }
            }

            i++;
        }

    }

    protected void fillWithoutId(Object product, ObjectMapper om, BufferedReader reader) throws Exception {
        beforeFill(om);

        List<FieldModel> mappedFields = om.getMappedFields();
        int i = 0;

        for (FieldModel f : mappedFields) {
            if (!f.getConstantValue().isEmpty() || f.isIgnoreOnRead()) {
                i++;
                continue;
            }

            if (!f.isCollection() && !f.isNestedObject() && f.getTypeHandler() != null) {

                try {
                    String v = trim ? getValueByIndex(i).trim() : getValueByIndex(i);
                    Object value = f.getTypeHandler().parse(v);
                    setValue(product, value, f);
                } catch (HandlerParseException ex) {
                    throw new HandlerParseException("Line: " + actualLine + ", field: " + (i + 1), ex);
                }
            } else if (f.isCollection()) {
                if (hasNext()) {
                    //Desde que o root nao seja uma collection, o objeto poderá ter uma collection, mas somente uma e deve ser o último a ser processado
                    //if the root is not one collection, the object may have a collection, but only one and should be the last to be processed
                    ObjectMapper oc = getObjectMapperFactory().getMappers().get(f.getClassType());
                    if (oc != null) {
                        Collection c = getCollection(product, f);
                        do {
                            readLine(reader);
                            Object r = processRecordWithoutId(reader, oc);
                            if (r != null) {
                                c.add(r);
                            }
                        } while (hasNext());
                    }
                }

            } else if (f.isNestedObject() && hasNext()) {
                ObjectMapper oc = getObjectMapperFactory().getMappers().get(f.getClassType());
                if (oc != null) {
                    readLine(reader);
                    Object r = processRecordWithoutId(reader, oc);
                    setValue(product, r, f);
                }
            }

            i++;
        }
    }

    private Object processRecord(BufferedReader reader) throws Exception {

        if (currentRecordIsNull()) {
            return null;
        }

        ObjectMapper om = getObjectMapperFactory().getIdsMap().get(getIdValue(false));

        if (om == null) {
            if (!ignoreUnknownRecords) {
                throw new UnknownRecordException("The record with ID '" + getIdValue(false) + "' is unknown. Line:" + actualLine);
            }
            return null;
        }

        Object product = om.getRootClass().newInstance();
        fill(product, om, reader);
        return product;
    }

    private Object processRecordWithoutId(BufferedReader reader, ObjectMapper om) throws Exception {

        if (currentRecordIsNull()) {
            return null;
        }

        Object product = om.getRootClass().newInstance();
        fillWithoutId(product, om, reader);
        return product;
    }

    protected Collection getCollection(final Object obj, final FieldModel f) {
        Object o = null;
        if (f.getGetterMethod() != null) {
            //PROPERTY
            o = AccessController.doPrivileged(new PrivilegedAction() {
                @Override
                public Object run() {
                    boolean wasAccessible = f.getGetterMethod().isAccessible();
                    try {
                        f.getGetterMethod().setAccessible(true);
                        Object c = f.getGetterMethod().invoke(obj);
                        if (c == null) {
                            if (List.class.isAssignableFrom(f.getCollectionType())) {
                                c = new LinkedList();
                            } else if (Set.class.isAssignableFrom(f.getCollectionType())) {
                                c = new LinkedHashSet();
                            }
                            f.getSetterMethod().setAccessible(true);
                            f.getSetterMethod().invoke(obj, c);
                        }
                        return c;
                    } catch (Exception ex) {
                        throw new IllegalStateException("Cannot invoke method for collection", ex);
                    } finally {
                        f.getGetterMethod().setAccessible(wasAccessible);
                        f.getSetterMethod().setAccessible(wasAccessible);
                    }
                }
            });
        } else {
            try {
                //FIELD
                final java.lang.reflect.Field declaredField;

                declaredField = obj.getClass().getDeclaredField(f.getName());
                o = AccessController.doPrivileged(new PrivilegedAction() {
                    @Override
                    public Object run() {
                        boolean wasAccessible = declaredField.isAccessible();
                        try {
                            declaredField.setAccessible(true);
                            Object c = declaredField.get(obj);

                            if (c == null) {
                                if (List.class.isAssignableFrom(f.getCollectionType())) {
                                    c = new LinkedList();
                                } else if (Set.class.isAssignableFrom(f.getCollectionType())) {
                                    c = new LinkedHashSet();
                                }
                                declaredField.set(obj, c);
                            }
                            return c;
                        } catch (Exception ex) {
                            throw new IllegalStateException("Cannot access set field", ex);
                        } finally {
                            declaredField.setAccessible(wasAccessible);
                        }

                    }
                });
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(DelimitedReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(DelimitedReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return (Collection) o;
    }

    protected void setValue(final Object obj, final Object fieldValue, final FieldModel f) {
        if (f.getSetterMethod() != null) {
            //PROPERTY
            AccessController.doPrivileged(new PrivilegedAction() {
                @Override
                public Object run() {
                    boolean wasAccessible = f.getSetterMethod().isAccessible();
                    try {
                        f.getSetterMethod().setAccessible(true);
                        return f.getSetterMethod().invoke(obj, fieldValue);
                    } catch (Exception ex) {
                        throw new IllegalStateException("Cannot invoke method set", ex);
                    } finally {
                        f.getSetterMethod().setAccessible(wasAccessible);
                    }
                }
            });
        } else {
            try {
                //FIELD
                final java.lang.reflect.Field declaredField;

                declaredField = obj.getClass().getDeclaredField(f.getName());
                AccessController.doPrivileged(new PrivilegedAction() {
                    @Override
                    public Object run() {
                        boolean wasAccessible = declaredField.isAccessible();
                        try {
                            declaredField.setAccessible(true);
                            declaredField.set(obj, fieldValue);
                        } catch (Exception ex) {
                            throw new IllegalStateException("Cannot access set field", ex);
                        } finally {
                            declaredField.setAccessible(wasAccessible);
                        }
                        return null;
                    }
                });
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(DelimitedReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(DelimitedReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public boolean isIgnoreUnknownRecords() {
        return ignoreUnknownRecords;
    }

    public void setIgnoreUnknownRecords(boolean ignoreUnknownRecords) {
        this.ignoreUnknownRecords = ignoreUnknownRecords;
    }

    public boolean isRemoveRecordInitializator() {
        return removeRecordInitializator;
    }

    public void setRemoveRecordInitializator(boolean removeRecordInitializator) {
        this.removeRecordInitializator = removeRecordInitializator;
    }

    public String getRecordInitializator() {
        return recordInitializator;
    }

    public void setRecordInitializator(String recordInitializator) {
        this.recordInitializator = recordInitializator;
    }

    /**
     * If {@code true} then the values are trimmed before being analyzed by the handler.
     * Default is {@code true}.
     *
     * @return
     */
    public boolean isTrim() {
        return trim;
    }

    /**
     * If {@code true} then the values are trimmed before being analyzed by the handler.
     * Default is {@code true}.
     *
     * @param trim
     */
    public void setTrim(boolean trim) {
        this.trim = trim;
    }

}
