/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 - 2018 Jeandeson O. Merelis
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

import coffeepot.bean.wr.mapper.Command;
import coffeepot.bean.wr.mapper.Metadata;
import java.time.Instant;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultInstantHandler implements TypeHandler<Instant> {

    @Override
    public Instant parse(String text, Metadata metadata) throws HandlerParseException {
        if (text == null || (text = text.trim()).isEmpty()) {
            return null;
        }

        try {
            return Instant.parse(text);
        } catch (Exception ex) {
            throw new HandlerParseException(ex);
        }
    }

    @Override
    public String toString(Instant obj, Metadata metadata) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public void config(Command[] commands) {
    }

}
