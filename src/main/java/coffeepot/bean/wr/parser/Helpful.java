/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.parser;

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
import coffeepot.bean.wr.annotation.NestedField;
import coffeepot.bean.wr.parser.FieldImpl;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class Helpful {

    public static FieldImpl toFieldImpl(NestedField nf) {
        FieldImpl f = new FieldImpl();
        f.setAccessorType(nf.accessorType());
        f.setAlign(nf.align());
        f.setBeginNewRecord(nf.beginNewRecord());
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
        f.setSegmentBeginNewRecord(nf.segmentBeginNewRecord());
        f.setSetter(nf.setter());
        f.setTrim(nf.trim());
        f.setTypeHandler(nf.typeHandler());
        f.setTypeHandler(nf.typeHandler());
        f.setRequired(nf.required());
        if (nf.length()> 0){
            f.setMinLength(nf.length());
            f.setMaxLength(nf.length());
        }
        return f;
    }
    
    public static FieldImpl toFieldImpl(Field nf) {
        FieldImpl f = new FieldImpl();
        f.setAccessorType(nf.accessorType());
        f.setAlign(nf.align());
        f.setBeginNewRecord(nf.beginNewRecord());
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
        f.setSegmentBeginNewRecord(nf.segmentBeginNewRecord());
        f.setSetter(nf.setter());
        f.setTrim(nf.trim());
        f.setTypeHandler(nf.typeHandler());
        f.setRequired(nf.required());
        if (nf.length()> 0){
            f.setMinLength(nf.length());
            f.setMaxLength(nf.length());
        }
        
        if (nf.nestedFields().length > 0){
            List<FieldImpl> nfs = new LinkedList<>();
            for (NestedField n: nf.nestedFields()){
                nfs.add( toFieldImpl(n));
            }
            f.setNestedFields(nfs);
        }
        
        return f;
    }


}
