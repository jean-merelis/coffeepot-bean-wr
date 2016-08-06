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
import coffeepot.bean.wr.mapper.RecordModel;
import coffeepot.bean.wr.mapper.UnresolvedObjectMapperException;
import coffeepot.bean.wr.typeHandler.HandlerParseException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public interface ObjectReader {

    public <T> T parse(Class<T> clazz) throws IOException, UnknownRecordException, HandlerParseException, Exception;

    public <T> T parse(Class<T> clazz, String recordGroupId) throws IOException, UnknownRecordException, HandlerParseException, Exception;

    public <T> List<T> parseAsListOf(Class<T> clazz) throws IOException, UnknownRecordException, HandlerParseException, Exception;

    public <T> List<T> parseAsListOf(Class<T> clazz, String recordGroupId) throws IOException, UnknownRecordException, HandlerParseException, Exception;

    public void findLineStartsWith(String s) throws IOException;

    public void processUpToTheLineStartsWith(String s);

    public void clearMappers();

    public int getActualLine();

    public Callback<Class, RecordModel> getCallback();

    public void setCallback(Callback<Class, RecordModel> callback);

    public void createMapper(Class<?> clazz) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception;

    public void createMapper(Class<?> clazz, String recordGroupId) throws UnresolvedObjectMapperException, NoSuchFieldException, Exception;
}
