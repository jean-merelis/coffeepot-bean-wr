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
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class FieldImpl implements Cloneable {

    private String name;
    private String constantValue;
    private int minLength;
    private int maxLength;
    private int length;
    private char padding;
    private boolean paddingIfNullOrEmpty;
    private boolean trim;
    private Align align;
    private String getter;
    private String setter;
    private Class<? extends TypeHandler> typeHandlerClass;
    private Class<?> classType;
    private String[] params;
    private AccessorType accessorType;
    private FieldImpl parent;
    private boolean nestedObject;
    private TypeHandler typeHandler;
    private Method getterMethod;
    private Method setterMethod;
    private Class collectionType;
    private boolean collection = false;
    private boolean ignoreOnRead = false;
    private boolean ignoreOnWrite = false;
    private boolean required = false;
    private boolean id = false;

    @Override
    public FieldImpl clone() {
        try {
            return (FieldImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FieldImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public char getPadding() {
        return padding;
    }

    public void setPadding(char padding) {
        this.padding = padding;
    }

    public boolean isPaddingIfNullOrEmpty() {
        return paddingIfNullOrEmpty;
    }

    public void setPaddingIfNullOrEmpty(boolean paddingIfNullOrEmpty) {
        this.paddingIfNullOrEmpty = paddingIfNullOrEmpty;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public Class<? extends TypeHandler> getTypeHandlerClass() {
        return typeHandlerClass;
    }

    public void setTypeHandlerClass(Class<? extends TypeHandler> typeHandlerClass) {
        this.typeHandlerClass = typeHandlerClass;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public AccessorType getAccessorType() {
        return accessorType;
    }

    public void setAccessorType(AccessorType accessorType) {
        this.accessorType = accessorType;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public void setGetterMethod(Method getterMethod) {
        this.getterMethod = getterMethod;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }

    public void setSetterMethod(Method setterMethod) {
        this.setterMethod = setterMethod;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isIgnoreOnRead() {
        return ignoreOnRead;
    }

    public void setIgnoreOnRead(boolean ignoreOnRead) {
        this.ignoreOnRead = ignoreOnRead;
    }

    public boolean isIgnoreOnWrite() {
        return ignoreOnWrite;
    }

    public void setIgnoreOnWrite(boolean ignoreOnWrite) {
        this.ignoreOnWrite = ignoreOnWrite;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public FieldImpl getParent() {
        return parent;
    }

    public void setParent(FieldImpl parent) {
        this.parent = parent;
    }

    public Class getCollectionType() {
        return this.collectionType;
    }

    public void setCollectionType(Class collectionType) {
        this.collectionType = collectionType;
    }

    public boolean isNestedObject() {
        return nestedObject;
    }

    public void setNestedObject(boolean nestedObject) {
        this.nestedObject = nestedObject;
    }

}
