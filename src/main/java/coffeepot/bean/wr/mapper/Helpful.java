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


import coffeepot.bean.wr.annotation.Field;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class Helpful {


    public static FieldModel toFieldImpl(Field nf) {
        FieldModel f = new FieldModel();
        f.setId(nf.id());
        f.setAccessorType(nf.accessorType());
        f.setAlign(nf.align());
        f.setClassType(nf.classType());
        f.setCollection(false);
        f.setConstantValue(nf.constantValue());
        f.setGetter(nf.getter());
        f.setLength(nf.length());
        f.setMaxLength(nf.maxLength());
        f.setMinLength(nf.minLength());
        f.setName(nf.name());
        f.setPadding(nf.padding());
        f.setPaddingIfNullOrEmpty(nf.paddingIfNullOrEmpty());
        f.setParams(nf.params());
        f.setSetter(nf.setter());
        f.setTrim(nf.trim());
        f.setTypeHandlerClass(nf.typeHandler());
        f.setRequired(nf.required());
        f.setIgnoreOnRead(nf.ignoreOnRead());
        f.setIgnoreOnWrite(nf.ignoreOnWrite());
        if (nf.length()> 0){
            f.setMinLength(nf.length());
            f.setMaxLength(nf.length());
        }


        return f;
    }


}
