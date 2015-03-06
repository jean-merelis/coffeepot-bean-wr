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
package coffeepot.bean.wr.writer;

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
import coffeepot.bean.wr.parser.FieldImpl;
import coffeepot.bean.wr.parser.ObjectMapper;
import coffeepot.bean.wr.parser.ObjectMapperFactory;
import coffeepot.bean.wr.parser.UnresolvedObjectMapperException;
import coffeepot.bean.wr.types.Align;
import java.io.IOException;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public abstract class AbstractWriter implements ObjectWriter {

    protected Writer writer;
    protected int autoFlush = 0;

    protected int recordCount = 0;

    protected abstract void writeRecord(List<String> values) throws IOException;

    @Override
    public abstract ObjectMapperFactory getObjectParserFactory();

    @Override
    public void clearMappers() {
        getObjectParserFactory().getMappers().clear();
    }

    @Override
    public void createMapper(Class<?> clazz) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        getObjectParserFactory().create(clazz);
    }

    @Override
    public void createMapper(Class<?> clazz, String recordGroupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception {
        getObjectParserFactory().create(clazz, recordGroupId);
    }

    @Override
    public int getAutoFlush() {
        return autoFlush;
    }

    @Override
    public void setAutoFlush(int autoFlush) {
        this.autoFlush = autoFlush;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public void write(Object obj) throws IOException {
        write(obj, null);
    }

    @Override
    public void write(Object obj, String recordGroupId) throws IOException {
        ObjectMapper op = getObjectParserFactory().getMappers().get(obj.getClass());
        if (op == null) {
            try {
                createMapper(obj.getClass(), recordGroupId);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            op = getObjectParserFactory().getMappers().get(obj.getClass());
            if (op == null) {
                throw new RuntimeException("Parser for class has not been set.");
            }
        }
        marshal(obj, op);
    }

    private void marshal(Object obj, ObjectMapper op) throws IOException {
        List<String> fieldsValue = marshal(obj, null, op);
        if (fieldsValue != null && !fieldsValue.isEmpty()) {
            writeRecord(fieldsValue);
        }
    }

    private List<String> marshal(Object obj, List<String> fieldsValue, ObjectMapper op) throws IOException {
        if (obj == null) {
            return fieldsValue;
        }
        return marshal(obj, fieldsValue, op.getMappedFields(), op.getRootClass(), op);
    }

    private List<String> marshal(Object obj, List<String> fieldsValue, List<FieldImpl> fields, Class<?> clazz, ObjectMapper op) throws IOException {
        for (FieldImpl f : fields) {
            if (f.isIgnoreOnWrite()) {
                continue;
            }

            if (!"".equals(f.getConstantValue())) {
                if (fieldsValue == null) {
                    fieldsValue = new LinkedList<>();
                }
                String s = process(f.getConstantValue(), f);
                fieldsValue.add(s);
                continue;
            }
            fieldsValue = marshalField(obj, fieldsValue, clazz, f, op);
        }

        return fieldsValue;
    }

    private List<String> marshalField(final Object obj, List<String> fieldsValue, Class<?> clazz, final FieldImpl f, ObjectMapper op) throws IOException {
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

                    writeRecord(fieldsValue);
                    fieldsValue = null;

                    fieldsValue = marshalCollection(o, fieldsValue, f, op);

                    writeRecord(fieldsValue);
                    fieldsValue = null;

                    return fieldsValue;
                }

                if (f.getTypeHandler() == null) {
                    if (f.isNestedObject()) {
                        writeRecord(fieldsValue);
                        fieldsValue = null;
                    }
                    ObjectMapper parser = getObjectParserFactory().getMappers().get(f.getClassType());
                    if (parser != null) {
                        fieldsValue = marshal(o, fieldsValue, parser);
                        if (f.isNestedObject()) {
                            writeRecord(fieldsValue);
                            fieldsValue = null;
                        }
                    } else {
                        throw new RuntimeException("Parser not found for class: " + f.getClassType().getName());
                    }
                    return fieldsValue;
                }
            }

            if (f.isNestedObject()) {
                writeRecord(fieldsValue);
                fieldsValue = null;
            }

            String s = process(f.getTypeHandler().toString(o), f);
            if (fieldsValue == null) {
                fieldsValue = new LinkedList<>();
            }
            fieldsValue.add(s);
            //FIXME: Exceptions
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ObjectMapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(ObjectMapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ObjectMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldsValue;
    }

    private List<String> marshalCollection(Object obj, List<String> fieldsValue, FieldImpl field, ObjectMapper op) throws IOException {
        if (obj == null) {
            return fieldsValue;
        }
        if (!Collection.class.isAssignableFrom(obj.getClass())) {
            return fieldsValue;
        }

        ObjectMapper parser = getObjectParserFactory().getMappers().get(field.getClassType());
        if (parser != null) {
            Collection c = (Collection) obj;
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Class<?> cl;
                List<FieldImpl> fi;

                fi = parser.getMappedFields();
                cl = parser.getRootClass();

                fieldsValue = marshal(it.next(), fieldsValue, fi, cl, parser);

                writeRecord(fieldsValue);
                fieldsValue = null;
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
