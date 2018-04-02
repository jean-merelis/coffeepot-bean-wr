/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.annotation;

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
import java.lang.annotation.Documented;
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
@Documented
public @interface Field {

    String name();

    String constantValue() default "";

    boolean id() default false;

    int minLength() default 0;

    int maxLength() default 0;

    int length() default 0;

    char padding() default ' ';

    boolean paddingIfNullOrEmpty() default false;

    boolean trim() default true;

    Align align() default Align.LEFT;

    String getter() default "";

    String setter() default "";

    Class<? extends TypeHandler> typeHandler() default DefaultHandler.class;

    /**
     * Used only when there isn't a field set.
     */
    Class<?> classType() default Class.class;

    Cmd[] commands() default {};

    AccessorType accessorType() default AccessorType.DEFAULT;

    /**
     * Ignores field content and writes as null. The conditionForWriteAs
     * annotation must be active and evaluated as true.
     *
     * @return
     */
    String writeAs() default "";

    FieldCondition conditionForWriteAs() default @FieldCondition();

    /**
     * Ignores field content and writes as null.
     *
     * @return
     */
    FieldCondition writeAsNull() default @FieldCondition();

    /**
     * Ignores the contents of the file's field and reads the value as defined
     * at this annotation. The conditionForReadAs annotation must be active and
     * evaluated as true.
     *
     * @return
     */
    String readAs() default "";

    FieldCondition conditionForReadAs() default @FieldCondition();

    /**
     * Ignores the contents of the file's field and reads the value as null.
     *
     * @return
     */
    FieldCondition readAsNull() default @FieldCondition();

    /**
     * General condition for the field to be written or read.
     *
     * @return
     */
    int minVersion() default 0;

    /**
     * General condition for the field to be written or read.
     *
     * @return
     */
    int maxVersion() default Integer.MAX_VALUE;
}
