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
import coffeepot.bean.wr.annotation.NestedField;
import coffeepot.bean.wr.annotation.Record;
import coffeepot.bean.wr.annotation.Records;
import coffeepot.bean.wr.typeHandler.DefaultEnumHandler;
import coffeepot.bean.wr.typeHandler.DefaultStringHandler;
import coffeepot.bean.wr.typeHandler.HandlerParseException;
import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.Align;
import coffeepot.bean.wr.types.FormatType;
import coffeepot.bean.wr.writer.customHandler.CustomDoubleHandler;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Records({
    @Record(groupId = "testGroupRecord", fields = {
        @Field(name = "", constantValue = "TESTE 1"),
        @Field(name = "name"),
        @Field(name = "age"),
        @Field(name = "children")
    }),
    @Record(fields = {
        @Field(name = "", constantValue = "PERSON"),
        @Field(name = "name"),
        @Field(name = "age"),
        @Field(name = "birthday", params = {"dd/MM/yyyy"}),
        @Field(name = "", getter = "childrenCount", classType = Integer.class),
        //Jobs inline
        @Field(name = "jobs", segmentBeginNewRecord = false, nestedFields = {@NestedField(name = "test1"), @NestedField(name = "test4")}),
        //Jobs in list
        @Field(name = "jobs", nestedFields = {@NestedField(name = "test1"), @NestedField(name = "test4")})
    }),
    @Record(forFormat = FormatType.FIXED_LENGTH,
            accessorType = AccessorType.FIELD,
            fields = {
                @Field(name = "name", length = 30),
                //for inherited fields, access must be through the PROPERTY and classType must be declared
                @Field(name = "lastName", length = 30, accessorType = AccessorType.PROPERTY, classType = String.class),
                @Field(name = "age", length = 6, align = Align.RIGHT, padding = '0'),
                @Field(name = "birthday", length = 8, padding = '0'),
                @Field(name = "testNumberOnly", length = 5, params = {DefaultStringHandler.PARAM_FILTER_NUMBER_ONLY}),
                @Field(name = "testNumberOnly", length = 8, params = {DefaultStringHandler.PARAM_FILTER_NUMBER_LETTERS_ONLY}),
                @Field(name = "longNumber", length = 5, align = Align.RIGHT),
                @Field(name = "jodaDateTime", length = 10),
                @Field(name = "salary", length = 10, align = Align.RIGHT, padding = '0', typeHandler = CustomDoubleHandler.class),
                @Field(name = "gender", length = 1),
                @Field(name = "filler", constantValue = "FFFFFFF"),
                @Field(name = "filler", constantValue = "1234567890", length = 5, align = Align.RIGHT),
                @Field(name = "childrenCount", length = 3, align = Align.RIGHT, padding = '0', getter = "childrenCount", classType = Integer.class)
            })
})
//@Record
public class Person extends Parent {

    private String name;

    private int age;

    private List<Child> children;

    private Long longNumber;

    private Date birthday;

    private DateTime jodaDateTime;

    private Double salary;

    private Gender gender;

    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public DateTime getJodaDateTime() {
        return jodaDateTime;
    }

    public void setJodaDateTime(DateTime jodaDateTime) {
        this.jodaDateTime = jodaDateTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Long getLongNumber() {
        return longNumber;
    }

    public void setLongNumber(Long longNumber) {
        this.longNumber = longNumber;
    }
    private String testNumberOnly;

    public String getTestNumberOnly() {
        return testNumberOnly;
    }

    public void setTestNumberOnly(String testNumberOnly) {
        this.testNumberOnly = testNumberOnly;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return 5;
    }

    public Integer getAgeTest() {
        return 50;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer childrenCount() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    public interface EncodedEnum {

        String getCode();

        Enum parse(String text);
    }

    public enum Gender implements EncodedEnum {

        MALE("1"),
        FEMALE("2");

        private String code;

        private Gender(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Enum parse(String text) {
            if ("1".equals(text)) {
                return MALE;
            }
            if ("2".equals(text)) {
                return FEMALE;
            }
            return null;
        }
    }

    public static class EncodedEnumHandler extends DefaultEnumHandler {

        @Override
        public Enum parse(String text) throws HandlerParseException {
            if (text == null || "".equals(text)) {
                return null;
            }

            if (EncodedEnum.class.isAssignableFrom(type)) {
                Enum[] enumConstants = type.getEnumConstants();
                return ((EncodedEnum) enumConstants[0]).parse(text);
            }

            return super.parse(text);
        }

        @Override
        public String toString(Enum obj) {
            if (obj == null) {
                return null;
            }

            if (obj instanceof EncodedEnum) {
                return ((EncodedEnum) obj).getCode();
            }
            return super.toString(obj);
        }
    }
}
