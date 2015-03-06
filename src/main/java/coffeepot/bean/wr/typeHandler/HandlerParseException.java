/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.typeHandler;

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
/**
 *
 * @author Jeandeson O. Merelis
 */
public class HandlerParseException extends Exception {

    /**
     * Creates a new instance of
     * <code>HandlerParseException</code> without detail message.
     */
    public HandlerParseException() {
    }

    /**
     * Constructs an instance of
     * <code>HandlerParseException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public HandlerParseException(String msg) {
        super(msg);
    }

    public HandlerParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerParseException(Throwable cause) {
        super(cause);
    }

}
