/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.parser;

import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import java.lang.reflect.Method;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class Field {

    private String name;
    private Class<?> clazz;
    private TypeHandler typeHandler;
    private Method getter;
    private Method setter;
    private int minLength;
    private int maxLength;
//    private int length;
    private char padding;
    private boolean paddingIfNullOrEmpty;
    private boolean trim;
    private boolean beginNewRecord = false;
    ;
    private boolean segmentBeginNewRecord = true;
    private Align align;
    private AccessorType accessorType;
    private boolean collection = false;
    private String constantValue;
    private boolean ignoreOnRead = false;
    private boolean ignoreOnWrite = false;

    public boolean isSegmentBeginNewRecord() {
        return segmentBeginNewRecord;
    }

    public void setSegmentBeginNewRecord(boolean segmentBeginNewRecord) {
        this.segmentBeginNewRecord = segmentBeginNewRecord;
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

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
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

    public boolean isBeginNewRecord() {
        return beginNewRecord;
    }

    public void setBeginNewRecord(boolean beginNewRecord) {
        this.beginNewRecord = beginNewRecord;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public AccessorType getAccessorType() {
        return accessorType;
    }

    public void setAccessorType(AccessorType accessorType) {
        this.accessorType = accessorType;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
