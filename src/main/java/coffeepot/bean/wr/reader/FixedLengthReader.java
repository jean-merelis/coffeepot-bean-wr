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
import coffeepot.bean.wr.mapper.FieldModel;
import coffeepot.bean.wr.mapper.ObjectMapper;
import coffeepot.bean.wr.mapper.ObjectMapperFactory;
import coffeepot.bean.wr.types.FormatType;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class FixedLengthReader extends AbstractReader {

    private final ObjectMapperFactory mapperFactory = new ObjectMapperFactory(FormatType.FIXED_LENGTH);
    private int idStart;
    private int idLength;
    private boolean objectById;

    public FixedLengthReader(Reader reader) {
        super(reader);
    }

    @Override
    public ObjectMapperFactory getObjectMapperFactory() {
        return mapperFactory;
    }

    @Override
    protected void beforeUnmarshal() {
        objectById = !mapperFactory.getIdsMap().isEmpty();
        if (objectById) {
            ObjectMapper om = mapperFactory.getIdsMap().values().iterator().next();
            Iterator<FieldModel> it = om.getFields().iterator();
            int pos = 0;
            while (it.hasNext()) {
                FieldModel f = it.next();
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
    }

    @Override
    protected void beforeFill(ObjectMapper mapper) {
        List<FieldModel> fields = filterFieldsByVersion(mapper);
        currentRecord = new String[fields.size()];
        int idx = 0;
        int pos = 0;
        int endIdx;
        Iterator<FieldModel> it = fields.iterator();
        while (it.hasNext()) {
            FieldModel f = it.next();
            if (f.isNestedObject()) {
                idx++;
                continue;
            }
            endIdx = pos + f.getLength();
            currentRecord[idx] = current.substring(pos, endIdx);
            pos = endIdx;
            idx++;
        }
    }

    @Override
    protected String getIdValue(boolean fromNext) {
        if (objectById) {
            if (idResolver != null) {
                String id = idResolver.call(fromNext ? next : current);
                if (id != null) {
                    return id.trim();
                }
            }

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
    protected void readLine() throws IOException {
        current = next;
        next = getNextRecord();
    }

    @Override
    protected boolean isCurrentRecordNull() {
        return current == null;
    }

    @Override
    protected boolean hasNext() {
        return next != null;
    }

    protected String getNextRecord() throws IOException {
        String line = null;
        while (true) {
            line = getLine();
            if (line == null) {
                return null;
            }

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
