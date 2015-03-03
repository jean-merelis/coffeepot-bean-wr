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
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class FixedLengthWriter extends AbstractWriter{

    protected String recordInitializator = null;

    protected String recordTerminator = "\r\n";

    protected ObjectParserFactory parserFactory;

    public FixedLengthWriter(Writer writer) {
        this.writer = writer;
        parserFactory = new ObjectParserFactory(FormatType.FIXED_LENGTH);
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
    public FormatType getFormatType() {
        return FormatType.FIXED_LENGTH;
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
        while (iterator.hasNext()) {
            sb.append(iterator.next());
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

    public static FixedLengthWriter create(Writer w) {
        return new FixedLengthWriter(w);
    }

    public FixedLengthWriter withAutoFlush(int autoFlush) {
        setAutoFlush(autoFlush);
        return this;
    }

    public FixedLengthWriter withRecordInitializator(String recordInitializator) {
        setRecordInitializator(recordInitializator);
        return this;
    }

    public FixedLengthWriter withRecordTerminator(String recordTerminator) {
        setRecordTerminator(recordTerminator);
        return this;
    }
}
