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
import coffeepot.bean.wr.parser.ObjectParserFactory;
import coffeepot.bean.wr.parser.UnresolvedObjectParserException;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public interface ObjectWriter {

    FormatType getFormatType();

    ObjectParserFactory getObjectParserFactory();

    void write(Object obj) throws IOException;

    void write(Object obj, String recordGroupId) throws IOException;

    void writeRecord(List<String> values) throws IOException;

    void writeRecord(String value) throws IOException;

    void flush() throws IOException;
    
    void close() throws IOException;

    int getAutoFlush();

    void setAutoFlush(int recordCounts);

    Writer getWriter();

    void setWriter(Writer w);

    void clearParsers();

    void createParser(Class<?> clazz) throws UnresolvedObjectParserException, NoSuchFieldException, Exception;

    void createParser(Class<?> clazz, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception;

    void createParserByAnotherClass(Class<?> fromClass, Class<?> targetClass) throws UnresolvedObjectParserException, NoSuchFieldException, Exception;

    void createParserByAnotherClass(Class<?> fromClass, Class<?> targetClass, String recordGroupId) throws UnresolvedObjectParserException, NoSuchFieldException, Exception;
}
