/*
 * Copyright 2017 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.annotation;

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
public @interface FieldCondition {

    /**
     * If false the condition will not be evaluated.
     *
     * @return
     */
    boolean active() default false;

    /**
     * If true and the condition is active, the condition will always be
     * evaluated as true and the other predicates will not be parsed.
     *
     * @return
     */
    boolean always() default false;

    int minVersion() default 0;

    int maxVersion() default Integer.MAX_VALUE;
        
}
