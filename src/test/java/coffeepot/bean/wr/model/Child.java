/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.model;

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
import coffeepot.bean.wr.annotation.Record;
import coffeepot.bean.wr.annotation.Records;
import coffeepot.bean.wr.types.Align;
import coffeepot.bean.wr.types.FormatType;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Records({
    @Record(forFormat = FormatType.DELIMITED ,fields = {
        @Field(name = "", constantValue = "CHILD"),
        @Field(name = "name", params = {"CharCase.UPPER"}),
        @Field(name = "age")
    }),
    @Record(forFormat = FormatType.FIXED_LENGTH ,fields = {
        @Field(name = "", constantValue = "CHILD"),
        @Field(name = "name", length = 30, params = {"CharCase.UPPER"}),
        @Field(name = "age", length = 5, align = Align.RIGHT)
    }),
    @Record( groupId = "testGroupRecord",fields = {
        @Field(name = "", constantValue = "testGroupRecord"),
        @Field(name = "", constantValue = "CHILD"),
        @Field(name = "name", params = {"CharCase.UPPER"}),
        @Field(name = "age")
    })
})
public class Child {

    private String name;

    private int age;

    private Child child;

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
