/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer;

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
import coffeepot.bean.wr.Child;
import coffeepot.bean.wr.Person;
import coffeepot.bean.wr.typeHandler.DefaultDateHandler;
import coffeepot.bean.wr.typeHandler.TypeHandler;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.writer.customHandler.DateTimeHandler;
import coffeepot.bean.wr.writer.customHandler.LowStringHandler;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class FixedLengthWriterTest {

    public FixedLengthWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWrite() throws Exception {
        System.out.println("write");
        Writer w = new FileWriter("D:\\TESTE_.TXT");

        FixedLengthWriter instance = new FixedLengthWriter(w);

        instance.setRecordTerminator("\r\n");

        //set new custom TypeHandler as default for a class
        TypeHandlerFactory handlerFactory = instance.getObjectParserFactory().getHandlerFactory();
        handlerFactory.registerTypeHandlerClassFor(DateTime.class, DateTimeHandler.class);

        //set new custom TypeHandler as default for the class String
        handlerFactory.registerTypeHandlerClassFor(String.class, LowStringHandler.class);

        //set new custom TypeHandler as default for Enum
        handlerFactory.registerTypeHandlerClassFor(Enum.class, Person.EncodedEnumHandler.class);



        Person obj = new Person();
        obj.setName("Jean");
        obj.setLastName("Merelis");
        obj.setAge(37);
        obj.setTestNumberOnly("ad(*&%¨(*&%fd2---14324.32432adfa");
        obj.setLongNumber(Long.MIN_VALUE);
        obj.setBirthday(new Date());
        obj.setJodaDateTime(DateTime.now());
        obj.setSalary(5999.9);
        obj.setGender(Person.Gender.MALE);

        instance.write(obj);

        obj = new Person();
        obj.setName("John");
        obj.setAge(14);
        instance.write(obj);

        obj = new Person();
        obj.setName("Ana");
        obj.setAge(11);
        instance.write(obj);

        obj = new Person();
        obj.setName("Jean");
        obj.setAge(37);

        List<Child> chidren = new LinkedList<>();

        Child child = new Child();
        child.setName("John");
        child.setAge(14);
        chidren.add(child);

        child = new Child();
        child.setName("Ana");
        child.setAge(11);
        chidren.add(child);

        obj.setChildren(chidren);
        instance.write(obj);

        w.flush();
        w.close();

    }
}
