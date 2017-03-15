/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.mapper;

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
import coffeepot.bean.wr.typeHandler.DefaultHandler;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Getter
@Setter
public class FieldModel implements Cloneable {

    private String name;
    private String constantValue;
    private int minLength;
    private int maxLength;
    private int length;
    private char padding = ' ';
    private boolean paddingIfNullOrEmpty;
    private boolean trim = true;
    private Align align = Align.LEFT;
    private String getter;
    private String setter;
    private Class<? extends TypeHandler> typeHandlerClass = DefaultHandler.class;
    private Class<?> classType = Class.class;
    private String[] params = new String[]{};
    private AccessorType accessorType = AccessorType.DEFAULT;
    private boolean nestedObject;
    private TypeHandler typeHandler;
    private Method getterMethod;
    private Method setterMethod;
    private Class collectionType;
    private boolean collection = false;
    private boolean ignoreOnRead = false;
    private boolean ignoreOnWrite = false;
    private boolean required = true;
    private boolean id = false;
    private int minVersion;
    private int maxVersion;

    @Override
    public FieldModel clone() {
        try {
            return (FieldModel) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FieldModel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
