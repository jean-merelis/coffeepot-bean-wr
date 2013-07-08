/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer;

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


import coffeepot.bean.wr.types.FormatType;
import coffeepot.bean.wr.parser.ObjectParser;
import coffeepot.bean.wr.parser.ObjectParserFactory;
import coffeepot.bean.wr.parser.UnresolvedObjectParserException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DelimitedWriter implements ObjectWriter {

    protected char delimiter = ',';

    protected String recordInitializator = null;

    protected String recordTerminator = "\r\n";

    protected Writer writer;

    protected ObjectParserFactory parserFactory = new ObjectParserFactory(FormatType.DELIMITED);

    protected int autoFlush = 0;

    protected int recordCount = 0;

    public DelimitedWriter(Writer writer) {
        this.writer = writer;
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
    public void clearParsers() {
        parserFactory.getParsers().clear();
    }

    @Override
    public void createParser(Class<?> clazz) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        parserFactory.create(clazz);
    }

    @Override
    public void createParser(Class<?> clazz, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        parserFactory.create(clazz, recordGroupId);
    }

    @Override
    public void createParserByAnotherClass(Class<?> fromClass, Class<?> targetClass) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        parserFactory.createByAnotherClass(fromClass, targetClass);
    }

    @Override
    public void createParserByAnotherClass(Class<?> fromClass, Class<?> targetClass, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception {
        parserFactory.createByAnotherClass(fromClass, targetClass, recordGroupId);
    }

    @Override
    public void write(Object obj) throws IOException {
        write(obj, null);
    }

    @Override
    public void write(Object obj, String recordGroupId) throws IOException {
        ObjectParser op = parserFactory.getParsers().get(obj.getClass());
        if (op == null) {
            try {
                createParser(obj.getClass(), recordGroupId);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            op = parserFactory.getParsers().get(obj.getClass());
            if (op == null) {
                throw new RuntimeException("Parser for class has not been set.");
            }
        }
        op.marshal(this, obj);
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public String getRecordInitializator() {
        return recordInitializator;
    }

    public void setRecordInitializator(String recordInitializator) {
        this.recordInitializator = recordInitializator;
    }

    public String getRecordTerminator() {
        return recordTerminator;
    }

    public void setRecordTerminator(String recordTerminator) {
        this.recordTerminator = recordTerminator;
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
    public FormatType getFormatType() {
        return FormatType.DELIMITED;
    }

    @Override
    public void writeRecord(List<String> values) throws IOException {
        if (values == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (recordInitializator != null) {
            sb.append(recordInitializator);
        }
        Iterator<String> iterator = values.iterator();
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            sb.append(delimiter).append(iterator.next());
        }
        sb.append(recordTerminator);

        writer.write(sb.toString());

        if (autoFlush > 0) {
            recordCount++;
            if (recordCount >= autoFlush) {
                flush();
                recordCount = 0;
            }
        }
    }

    @Override
    public void writeRecord(String value) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (recordInitializator != null) {
            sb.append(recordInitializator);
        }
        if (value != null) {
            sb.append(value);
        }
        sb.append(recordTerminator);

        writer.write(sb.toString());

        if (autoFlush > 0) {
            recordCount++;
            if (recordCount >= autoFlush) {
                flush();
                recordCount = 0;
            }
        }
    }

    @Override
    public ObjectParserFactory getObjectParserFactory() {
        return parserFactory;
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

    public static DelimitedWriter create(Writer w) {
        return new DelimitedWriter(w);
    }

    public DelimitedWriter withAutoFlush(int autoFlush) {
        setAutoFlush(autoFlush);
        return this;
    }

    public DelimitedWriter withDelimiter(char delimiter) {
        setDelimiter(delimiter);
        return this;
    }

    public DelimitedWriter withRecordInitializator(String recordInitializator) {
        setRecordInitializator(recordInitializator);
        return this;
    }

    public DelimitedWriter withRecordTerminator(String recordTerminator) {
        setRecordTerminator(recordTerminator);
        return this;
    }
}
