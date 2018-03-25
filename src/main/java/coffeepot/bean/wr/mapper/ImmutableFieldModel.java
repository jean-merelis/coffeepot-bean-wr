/*
 * Copyright 2017 - Jeandeson O. Merelis
 */

package coffeepot.bean.wr.mapper;

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

import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class ImmutableFieldModel {

    private final FieldModel fieldModel;

    public ImmutableFieldModel(FieldModel fieldModel) {
        this.fieldModel = fieldModel;
    }

    public String getName() {
        return fieldModel.getName();
    }

    public String getConstantValue() {
        return fieldModel.getConstantValue();
    }

    public int getMinLength() {
        return fieldModel.getMinLength();
    }

    public int getMaxLength() {
        return fieldModel.getMaxLength();
    }

    public int getLength() {
        return fieldModel.getLength();
    }

    public char getPadding() {
        return fieldModel.getPadding();
    }

    public boolean isPaddingIfNullOrEmpty() {
        return fieldModel.isPaddingIfNullOrEmpty();
    }

    public boolean isTrim() {
        return fieldModel.isTrim();
    }

    public Align getAlign() {
        return fieldModel.getAlign();
    }

    public String getGetter() {
        return fieldModel.getGetter();
    }

    public String getSetter() {
        return fieldModel.getSetter();
    }

    public Class<? extends TypeHandler> getTypeHandlerClass() {
        return fieldModel.getTypeHandlerClass();
    }

    public Class<?> getClassType() {
        return fieldModel.getClassType();
    }

    public String[] getParams() {
        return fieldModel.getParams();
    }

    public AccessorType getAccessorType() {
        return fieldModel.getAccessorType();
    }

    public Class getCollectionType() {
        return fieldModel.getCollectionType();
    }

    public boolean isId() {
        return fieldModel.isId();
    }

    public int getMinVersion() {
        return fieldModel.getMinVersion();
    }

    public int getMaxVersion() {
        return fieldModel.getMaxVersion();
    }
    
    
}
