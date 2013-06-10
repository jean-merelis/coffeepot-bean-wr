/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.writer;

import coffeepot.bean.wr.Child;
import coffeepot.bean.wr.Person;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.writer.customHandler.LowStringHandler;
import coffeepot.bean.wr.writer.customHandler.DateTimeHandler;
import java.io.FileWriter;
import java.io.IOException;
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
import static org.junit.Assert.*;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DelimitedWriterTest {

    public DelimitedWriterTest() {
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


        DelimitedWriter instance = new DelimitedWriter(w);
        instance.setDelimiter(';');
        instance.setRecordInitializator("");
        instance.setRecordTerminator("\r\n");


        //set new custom TypeHandler as default for a class
        TypeHandlerFactory handlerFactory = instance.getObjectParserFactory().getHandlerFactory();
        handlerFactory.registerTypeHandlerClassFor(DateTime.class, DateTimeHandler.class);

        //set new custom TypeHandler as default for the class String
        handlerFactory.registerTypeHandlerClassFor(String.class, LowStringHandler.class);

        //set new custom TypeHandler as default for Enum
        handlerFactory.registerTypeHandlerClassFor(Enum.class, Person.EncodedEnumHandler.class);

        //instance.createParser(Person.class);

        Person obj = new Person();
        obj.setName("Jean");
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