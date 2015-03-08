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
import coffeepot.bean.wr.model.ItemDet;
import coffeepot.bean.wr.model.Order;
import coffeepot.bean.wr.model.SingleClass;
import coffeepot.bean.wr.typeHandler.DefaultDoubleHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class FixedLengthReaderTest {

    public FixedLengthReaderTest() {
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
    public void testRead() throws Exception {
        DefaultDoubleHandler.setDecimalSeparatorDefault(',');
        DefaultDoubleHandler.setGroupingSeparatorDefault('.');

        Order order = new Order();
        order.setCustomer("John B");
        order.setDate(new Date());
        order.setId(123);
        order.setItems(new ArrayList<Item>());

        Item item = new Item();
        item.setNumber(1);
        item.setProduct("Product 1");
        item.setQuantity(10);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("something"));
        item.getDetails().add(new ItemDet("another something"));
        order.getItems().add(item);

        item = new Item();
        item.setNumber(2);
        item.setProduct("Product 002");
        item.setQuantity(5);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("blue"));
        item.getDetails().add(new ItemDet("yellow"));
        order.getItems().add(item);

        item = new Item();
        item.setNumber(3);
        item.setProduct("Product 003");
        item.setQuantity(2);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("red"));
        item.getDetails().add(new ItemDet("white"));
        order.getItems().add(item);

        File file = new File("ORDER_fixed_to_reader.tmp");
        try (Writer w = new FileWriter(file)) {

            FixedLengthWriter writer = new FixedLengthWriter(w);
            writer.setRecordTerminator("\r\n");

            writer.write(order);

            w.flush();
            w.close();
        }
        FixedLengthReader reader = new FixedLengthReader();

        Order o = reader.read(new FileInputStream(file), Order.class);

        assertNotNull(o);
        //TODO: check field values

    }

    @Test
    public void singleClassTest() throws Exception {
        DefaultDoubleHandler.setDecimalSeparatorDefault(',');
        DefaultDoubleHandler.setGroupingSeparatorDefault('.');
        
        List<SingleClass> list = new ArrayList<>();

        SingleClass s;

        s = new SingleClass();
        s.setField1("111111");
        s.setField2("22222299999999999999999999999999");
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

            FixedLengthWriter writer = new FixedLengthWriter(w);
            writer.setRecordTerminator("\r\n");

            for (SingleClass sc : list) {
                writer.write(sc);
            }

            w.flush();
            w.close();
        }

        FixedLengthReader reader = new FixedLengthReader();

        DelimitedReaderTest.SingleClassList o = reader.read(new FileInputStream(file), DelimitedReaderTest.SingleClassList.class);

        //Vai ler somente a primeira linha
        //It will read only the first line
        SingleClass sc = reader.read(new FileInputStream(file), SingleClass.class);

        Assert.assertNotNull(o);
        //TODO: check field values
    }

}
