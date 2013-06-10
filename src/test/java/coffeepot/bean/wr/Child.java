/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr;

import coffeepot.bean.wr.anotation.Field;
import coffeepot.bean.wr.anotation.Record;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Record(fields = {
    @Field(name = "", constantValue = "CHILD"),
    @Field(name = "name", params = {"CharCase.UPPER"}),
    @Field(name = "age")
})
public class Child {

    private String name;
    private int age;

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
