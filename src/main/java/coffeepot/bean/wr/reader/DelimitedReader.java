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
import coffeepot.bean.wr.parser.ObjectMapperFactory;
import coffeepot.bean.wr.types.FormatType;
import java.io.BufferedReader;
import java.util.regex.Pattern;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DelimitedReader extends AbstractReader {

    protected char delimiter = ';';
    private Character escape;
    private String regexSplit;
    private String escOld;
    private String escNew;
    private String delimOld;
    private String delimNew;

    private final ObjectMapperFactory mapperFactory = new ObjectMapperFactory(FormatType.DELIMITED);

    @Override
    public ObjectMapperFactory getObjectMapperFactory() {
        return mapperFactory;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public Character getEscape() {
        return escape;
    }

    public void setEscape(Character escape) {
        this.escape = escape;
    }

    @Override
    protected void config() {
        String delim = "" + delimiter;
        if (escape != null) {
            String esc = String.valueOf(escape);
            regexSplit = "(?<!" + Pattern.quote(esc) + ")" + Pattern.quote(delim);

            escOld = esc + esc;
            escNew = esc;

            delimOld = esc + delim;
            delimNew = delim;
        } else {
            regexSplit = Pattern.quote(delim);
        }
    }

    @Override
    protected String[] getNextRecord(BufferedReader reader) throws Exception {
        String line = null;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                return null;
            }

            actualLine++;

            line = line.trim();
            if (!line.isEmpty()) {
                break;
            }
        }

        if (recordInitializator != null && removeRecordInitializator) {
            if (line.startsWith(recordInitializator)) {
                line = line.substring(recordInitializator.length());
            }
        }

        String[] values = line.split(regexSplit);

        if (escape != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].replace(escOld, escNew);
                values[i] = values[i].replace(delimOld, delimNew);
            }
        }
        
        return values;
    }

}
