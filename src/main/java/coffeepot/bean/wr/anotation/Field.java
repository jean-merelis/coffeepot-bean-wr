/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.anotation;

import coffeepot.bean.wr.typeHandler.DefaultHandler;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Field {

    String name();

    String constantValue() default "";

    int minLength() default 0;

    int maxLength() default 0;

    int length() default 0;

    char padding() default ' ';

    boolean paddingIfNullOrEmpty() default false;

    boolean trim() default true;

    boolean segmentBeginNewRecord() default true;

    boolean beginNewRecord() default false;

    Align align() default Align.LEFT;

    String getter() default "";

    String setter() default "";

    Class<? extends TypeHandler> typeHandler() default DefaultHandler.class;

    /**
     * Used only when there isn't a field set.
     *
     * @return
     */
    Class<?> classType() default Class.class;

    /**
     * Params to config.
     *
     * @return
     */
    String[] params() default {};

    AccessorType accessorType() default AccessorType.DEFAULT;
}
