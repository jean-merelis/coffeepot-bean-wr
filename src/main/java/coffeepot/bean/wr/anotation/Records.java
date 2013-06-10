/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.anotation;

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
public @interface Records {

    Record[] value();
}
