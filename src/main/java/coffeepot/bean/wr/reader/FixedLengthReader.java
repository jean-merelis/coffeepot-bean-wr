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
import coffeepot.bean.wr.mapper.FieldImpl;
import coffeepot.bean.wr.mapper.ObjectMapper;
import coffeepot.bean.wr.mapper.ObjectMapperFactory;
import coffeepot.bean.wr.types.FormatType;
import java.io.BufferedReader;
import java.util.Iterator;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class FixedLengthReader extends AbstractReader {

    private final ObjectMapperFactory mapperFactory = new ObjectMapperFactory(FormatType.FIXED_LENGTH);
    private int idStart;
    private int idLength;
    private boolean objectById; //

    @Override
    public ObjectMapperFactory getObjectMapperFactory() {
        return mapperFactory;
    }

    @Override
    protected void beforeUnmarshal() {
        objectById = !mapperFactory.getIdsMap().isEmpty();
        if (objectById) {
            ObjectMapper om = mapperFactory.getIdsMap().values().iterator().next();
            Iterator<FieldImpl> it = om.getMappedFields().iterator();
            int pos = 0;
            while (it.hasNext()) {
                FieldImpl f = it.next();
                if (f.isId()) {
                    idStart = pos;
                    idLength = f.getLength();
                    break;
                }
                pos = pos + f.getLength();
            }
        }
    }

    private String current;
    private String next;
    private String[] currentRecord;

    @Override
    protected void clear() {
        super.clear();
        current = null;
        next = null;
        currentRecord = null;
    }

    @Override
    protected void beforeFill(ObjectMapper om) {
        currentRecord = new String[om.getMappedFields().size()];
        int idx = 0;
        int pos = 0;
        int endIdx;
        Iterator<FieldImpl> it = om.getMappedFields().iterator();
        while (it.hasNext()){
            FieldImpl f = it.next();
            endIdx = pos + f.getLength();
            currentRecord[idx] = current.substring(pos, endIdx);
            pos = endIdx;
            idx++;
        }
    }

    @Override
    protected String getIdValue(boolean fromNext) {
        if (objectById) {
            String s = fromNext ? next.substring(idStart, idStart + idLength) : current.substring(idStart, idStart + idLength);
            return s.trim();
        } else {
            return null;
        }
    }

    @Override
    protected String getValueByIndex(int idx) {
        return currentRecord[idx];
    }

    @Override
    protected void readLine(BufferedReader reader) throws Exception {
        current = next;
        next = getNextRecord(reader);
    }

    @Override
    protected boolean currentRecordIsNull() {
        return current == null;
    }

    @Override
    protected boolean hasNext() {
        return next != null;
    }

    protected String getNextRecord(BufferedReader reader) throws Exception {
        String line = null;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                return null;
            }

            actualLine++;

            String s = line.trim();
            if (!s.isEmpty()) {
                break;
            }
        }

        if (recordInitializator != null && removeRecordInitializator) {
            if (line.startsWith(recordInitializator)) {
                line = line.substring(recordInitializator.length());
            }
        }

        return line;
    }

}
