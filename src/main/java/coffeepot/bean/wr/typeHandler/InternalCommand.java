/*
 * Copyright 2017 - Jeandeson O. Merelis
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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Getter @Setter @EqualsAndHashCode
public abstract class InternalCommand {

    protected String cmd;
    protected String[] args;
    protected boolean onRead;
    protected boolean onWrite;

    protected String execOnRead(String text) {
        if (!onRead) {
            return text;
        }
        return perform(text);
    }

    protected String execOnWrite(String text) {
        if (!onWrite) {
            return text;
        }
        return perform(text);
    }

    protected abstract String perform(String text);
}
