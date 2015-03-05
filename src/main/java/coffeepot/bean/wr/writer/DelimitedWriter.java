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
public class DelimitedWriter extends AbstractWriter {

    protected char delimiter = ';';

    protected String recordInitializator = null;

    protected String recordTerminator = "\r\n";

    private Character escape;

    private boolean removeDelimiter;

    protected ObjectParserFactory parserFactory = new ObjectParserFactory(FormatType.DELIMITED);

    public DelimitedWriter(Writer writer) {
        this.writer = writer;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Gets escape for the delimiter.
     *
     * @return
     */
    public Character getEscape() {
        return escape;
    }

    /**
     * Sets escape for the delimiter.
     *
     * @param escape
     */
    public void setEscape(Character escape) {
        this.escape = escape;
    }

    public boolean isRemoveDelimiter() {
        return removeDelimiter;
    }

    /**
     * Sets whether to remove delimiter on field values.
     *
     * @param removeDelimiter
     */
    public void setRemoveDelimiter(boolean removeDelimiter) {
        this.removeDelimiter = removeDelimiter;
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
        return FormatType.DELIMITED;
    }

    @Override
    protected void writeRecord(List<String> values) throws IOException {
        if (values == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (recordInitializator != null) {
            sb.append(recordInitializator);
        }

        String escDelimiter = null;
        String _esc = null;
        String esc2esc = null;
        String _delimiter = String.valueOf(delimiter);

        Iterator<String> iterator = values.iterator();
        String fieldValue = iterator.next();

        if (removeDelimiter) {
            fieldValue = fieldValue.replace(_delimiter, "");
        }

        if (escape != null) {
            escDelimiter = escape + _delimiter;
            _esc = String.valueOf(escape);
            esc2esc = "" + escape + escape;
            if (fieldValue != null) {
                fieldValue = fieldValue.replace(_esc, esc2esc);
                if (!removeDelimiter) {
                    fieldValue = fieldValue.replace(_delimiter, escDelimiter);
                }
            }
        }
        sb.append(fieldValue);
        while (iterator.hasNext()) {
            fieldValue = iterator.next();

            if (removeDelimiter) {
                fieldValue = fieldValue.replace(_delimiter, "");
            }

            if (escape != null && fieldValue != null) {
                fieldValue = fieldValue.replace(_esc, esc2esc);

                if (!removeDelimiter) {
                    fieldValue = fieldValue.replace(_delimiter, escDelimiter);
                }
            }
            sb.append(delimiter).append(fieldValue);
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

    /**
     * Sets whether to remove delimiter on field values.
     * @param r
     * @return 
     */
    public DelimitedWriter removeDelimiter(boolean r) {
        setRemoveDelimiter(r);
        return this;
    }
}
