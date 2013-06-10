/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.anotation;

import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.FormatType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Record {

    FormatType forFormat() default FormatType.ANY;

    AccessorType accessorType() default AccessorType.FIELD;

    Field[] fields() default {};

    String[] ignoredFields() default {};
}
