/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.reader;

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
import coffeepot.bean.wr.writer.*;
import coffeepot.bean.wr.model.Item;
import coffeepot.bean.wr.model.Item2;
import coffeepot.bean.wr.model.ItemDet;
import coffeepot.bean.wr.model.Order;
import coffeepot.bean.wr.model.Person;
import coffeepot.bean.wr.model.Read2;
import coffeepot.bean.wr.model.SingleClass;
import coffeepot.bean.wr.model.UnidentifiedObjWithList;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.writer.customHandler.LowStringHandler;
import coffeepot.bean.wr.writer.customHandler.DateTimeHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DelimitedReaderTest {

    public DelimitedReaderTest() {
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
    public void parse_noVersionInReader_shouldNotFillValueFieldWithMinVersion5() throws Exception {
        Item item = Item.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();
        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setRecordTerminator("|\n");

        writer.write(item);
        writer.flush();
        String s = w.toString();


        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');


        Item i = reader.parse(Item.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Product", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertNull(i.getValue());

    }

    @Test
    public void parse_readerWithVersion1_shouldNotFillValueFieldWithMaxVersion5() throws Exception {
        Item item = Item.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();

        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setEscape('\\');
        writer.setRecordTerminator("|\n");

        writer.setVersion(1);

        writer.write(item);
        writer.flush();
        String s = w.toString();

        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');
        reader.setEscape('\\');

        reader.setVersion(1);

        Item i = reader.parse(Item.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Product", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertNull(i.getValue());
    }

    @Test
    public void parse_readerWithVersion6_shouldNotFillValueFieldWithMaxVersion5() throws Exception {
        Item item = Item.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();
        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setEscape('\\');
        writer.setRecordTerminator("|\n");

        writer.setVersion(6);

        writer.write(item);
        writer.flush();
        String s = w.toString();

        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');
        reader.setEscape('\\');

        reader.setVersion(1);

        Item i = reader.parse(Item.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Product", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertNull(i.getValue());
    }

    @Test
    public void parse_readerWithVersion2_shouldFillValueFieldWithMaxVersion5() throws Exception {
        Item item = Item.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();
        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setEscape('\\');
        writer.setRecordTerminator("|\n");

        writer.setVersion(2);

        writer.write(item);
        writer.flush();
        String s = w.toString();

        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');
        reader.setEscape('\\');

        reader.setVersion(2);

        Item i = reader.parse(Item.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Product", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertNotNull(i.getValue());
        assertTrue(i.getValue().compareTo(50d) == 0);

    }

    @Test
    public void parse_readerWithVersion5_shouldFillValueFieldWithMaxVersion5() throws Exception {
        Item item = Item.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();
        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setEscape('\\');
        writer.setRecordTerminator("|\n");

        writer.setVersion(5);

        writer.write(item);
        writer.flush();
        String s = w.toString();

        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');
        reader.setEscape('\\');

        reader.setVersion(5);

        Item i = reader.parse(Item.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Product", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertNotNull(i.getValue());
        assertTrue(i.getValue().compareTo(50d) == 0);

    }

    @Test
    public void parse_readerWithVersion1_shouldNotUseReadAsFromConditionalFieldWithMinVersion5() throws Exception {
        Item2 item = Item2.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();

        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setEscape('\\');
        writer.setRecordTerminator("|\n");

        writer.setVersion(1);

        writer.write(item);
        writer.flush();
        String s = w.toString();

        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');
        reader.setEscape('\\');

        reader.setVersion(1);

        Item2 i = reader.parse(Item2.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Product", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertTrue(i.getValue().compareTo(50d) == 0);
    }

    @Test
    public void parse_readerWithVersion5_shouldUseReadAsFromConditionalFieldWithMinVersion5() throws Exception {
        Item2 item = Item2.builder()
                .number(1)
                .product("Product")
                .quantity(5d)
                .value(50d)
                .build();

        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter(w);
        writer.setDelimiter('|');
        writer.setEscape('\\');
        writer.setRecordTerminator("|\n");

        writer.setVersion(1);

        writer.write(item);
        writer.flush();
        String s = w.toString();

        StringReader r = new StringReader(s);

        DelimitedReader reader = new DelimitedReader(r);
        reader.setDelimiter('|');
        reader.setEscape('\\');

        reader.setVersion(5);

        Item2 i = reader.parse(Item2.class);

        assertNotNull(i);
        assertEquals(1, i.getNumber());
        assertEquals("Another name from readAs", i.getProduct());
        assertTrue(i.getQuantity().compareTo(5d) == 0);
        assertTrue(i.getValue().compareTo(50d) == 0);
    }

    //TODO: MORE TESTS
    // @Test
    public void testWrite2() throws Exception {
        Order order = new Order();
        order.setCustomer("John B");
        order.setDate(new Date());
        order.setId(123);
        order.setItems(new ArrayList<Item>());

        Item item = new Item();
        item.setNumber(1);
        item.setProduct("Product \\1");
        item.setQuantity(10d);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("something"));
        item.getDetails().add(new ItemDet("another something"));
        order.getItems().add(item);

        item = new Item();
        item.setNumber(2);
        item.setProduct("Product | 002");
        item.setQuantity(5d);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("blue"));
        item.getDetails().add(new ItemDet("yellow"));
        order.getItems().add(item);

        item = new Item();
        item.setNumber(3);
        item.setProduct("Product 003");
        item.setQuantity(2d);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("red"));
        item.getDetails().add(new ItemDet("white"));
        order.getItems().add(item);

        File file = new File("ORDER_to_reader.tmp");
        try (Writer w = new FileWriter(file)) {

            DelimitedWriter delimitedWriter = new DelimitedWriter(w);
            delimitedWriter.setDelimiter('|');
            delimitedWriter.setEscape('\\');
            delimitedWriter.setRecordInitializator("");
            delimitedWriter.setRecordTerminator("|\r\n");

            //set new custom TypeHandler as default for a class
            TypeHandlerFactory handlerFactory = delimitedWriter.getObjectMapperFactory().getHandlerFactory();
            handlerFactory.registerTypeHandler(DateTime.class, DateTimeHandler.class);

            //set new custom TypeHandler as default for the class String
            handlerFactory.registerTypeHandler(String.class, LowStringHandler.class);

            //set new custom TypeHandler as default for Enum
            handlerFactory.registerTypeHandler(Enum.class, Person.EncodedEnumHandler.class);

            delimitedWriter.write(order);

            w.flush();
            w.close();
        }
        DelimitedReader reader = new DelimitedReader(new FileReader(file));
        reader.setDelimiter('|');
        reader.setEscape('\\');

        Order o = reader.parse(Order.class);

        assertNotNull(o);
        //TODO: check field values
    }

    @Test
    public void singleClassTest() throws Exception {
        List<SingleClass> list = new ArrayList<>();

        SingleClass s;

        s = new SingleClass();
        s.setField1("111111");
        s.setField2("222222");
        list.add(s);

        s = new SingleClass();
        s.setField1("111111");
        s.setField2("222222");
        list.add(s);

        s = new SingleClass();
        s.setField1("111111");
        s.setField2("222222");
        list.add(s);

        s = new SingleClass();
        s.setField1("111111");
        s.setField2("222222");
        list.add(s);

        File file = new File("single.tmp");
        try (Writer w = new FileWriter(file)) {

            DelimitedWriter delimitedWriter = new DelimitedWriter(w);
            delimitedWriter.setDelimiter('|');
            delimitedWriter.setRecordTerminator("|\r\n");

            for (SingleClass sc : list) {
                delimitedWriter.write(sc);
            }

            w.flush();
            w.close();
        }

        DelimitedReader reader = new DelimitedReader(new FileReader(file));
        reader.setDelimiter('|');
        reader.setEscape('\\');

        SingleClassList o = reader.parse(SingleClassList.class);

        reader = new DelimitedReader(new FileReader(file));
        reader.setDelimiter('|');
        reader.setEscape('\\');
        //Vai ler somente a primeira linha
        //It will read only the first line
        SingleClass sc = reader.parse(SingleClass.class);

        Assert.assertNotNull(o);
        //TODO: check field values
    }

    @Test
    public void read2() throws Exception {

        List<Read2> list = new ArrayList<>();

        Read2 r;

        r = new Read2();
        r.setLine1("a");
        r.setLine2(new Read2.Test(1));
        list.add(r);

        r = new Read2();
        r.setLine1("b");
        r.setLine2(new Read2.Test(2));
        list.add(r);

        r = new Read2();
        r.setLine1("c");
        r.setLine2(new Read2.Test(3));
        list.add(r);

        r = new Read2();
        r.setLine1("d");
        r.setLine2(new Read2.Test(4));
        list.add(r);

        File file = new File("read2.tmp");
        try (Writer w = new FileWriter(file)) {

            DelimitedWriter delimitedWriter = new DelimitedWriter(w);
            delimitedWriter.setDelimiter('|');
            delimitedWriter.setRecordTerminator("|\r\n");

            for (Read2 sc : list) {
                delimitedWriter.write(sc);
            }

            w.flush();
            w.close();
        }

        DelimitedReader reader = new DelimitedReader(new FileReader(file));
        reader.setDelimiter('|');
        reader.setEscape('\\');

        Read2List o = reader.parse(Read2List.class);

        Assert.assertNotNull(o);
        //TODO: check field values
    }

    static class Read2List extends ArrayList<Read2> {
    }

    @Test
    public void unidentifiedObjWithListTest() throws Exception {

        UnidentifiedObjWithList u;

        u = new UnidentifiedObjWithList();
        u.setField1("111111");
        u.setField2("222222");
        u.setList(new ArrayList<UnidentifiedObjWithList.Name>());
        u.getList().add(new UnidentifiedObjWithList.Name("a"));
        u.getList().add(new UnidentifiedObjWithList.Name("b"));
        u.getList().add(new UnidentifiedObjWithList.Name("c"));
        u.getList().add(new UnidentifiedObjWithList.Name("d"));
        u.getList().add(new UnidentifiedObjWithList.Name("e"));
        u.getList().add(new UnidentifiedObjWithList.Name("f"));

        File file = new File("unidentifiedObjWithList.tmp");
        try (Writer w = new FileWriter(file)) {

            DelimitedWriter delimitedWriter = new DelimitedWriter(w);
            delimitedWriter.setDelimiter('|');
            delimitedWriter.setRecordTerminator("|\r\n");

            delimitedWriter.write(u);

            w.flush();
            w.close();
        }

        DelimitedReader reader = new DelimitedReader(new FileReader(file));
        reader.setDelimiter('|');
        reader.setEscape('\\');

        UnidentifiedObjWithList o = reader.parse(UnidentifiedObjWithList.class);

        assertNotNull(o);
        assertEquals("111111", o.getField1());
        assertEquals("222222", o.getField2());
        assertEquals(6, o.getList().size());
        assertEquals("a", o.getList().get(0).getName());
        assertEquals("b", o.getList().get(1).getName());
        assertEquals("c", o.getList().get(2).getName());
        assertEquals("d", o.getList().get(3).getName());
        assertEquals("e", o.getList().get(4).getName());
        assertEquals("f", o.getList().get(5).getName());

    }

    static class SingleClassList extends ArrayList<SingleClass> {
    }
}
