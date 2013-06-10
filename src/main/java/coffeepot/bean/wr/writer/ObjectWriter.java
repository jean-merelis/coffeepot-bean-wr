/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer;

import coffeepot.bean.wr.types.FormatType;
import coffeepot.bean.wr.parser.ObjectParserFactory;
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

    void writeRecord(List<String> values) throws IOException;

    void flush() throws IOException;

    int getAutoFlush();

    void setAutoFlush(int recordCounts);

    Writer getWriter();

    void setWriter(Writer w);
}
