/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer.customHandler;

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


import coffeepot.bean.wr.mapper.Metadata;
import coffeepot.bean.wr.typeHandler.HandlerParseException;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import org.joda.time.DateTime;

/**
 * Only tests.
 * @author Jeandeson O. Merelis
 */
public class DateTimeHandler implements TypeHandler<DateTime> {

    @Override
    public DateTime parse(String text, Metadata metadata) throws HandlerParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try{
            return DateTime.parse(text);
        }catch(Exception ex){
            throw new HandlerParseException(ex.getMessage());
        }
    }

    @Override
    public String toString(DateTime obj, Metadata metadata) {
        if (obj == null) return null;
        return obj.toString();
    }

    @Override
    public void setConfig(String[] params) {
        //set your config here
    }
}
