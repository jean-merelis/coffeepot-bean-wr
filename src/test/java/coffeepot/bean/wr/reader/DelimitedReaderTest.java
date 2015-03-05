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
import coffeepot.bean.wr.model.Person;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.writer.customHandler.LowStringHandler;
import coffeepot.bean.wr.writer.customHandler.DateTimeHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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

    //TODO: MORE TESTS
    @Test
    public void testWrite2() throws Exception {
        Order order = new Order();
        order.setCustomer("John B");
        order.setDate(new Date());
        order.setId(123);
        order.setItems(new ArrayList<Item>());

        Item item = new Item();
        item.setNumber(1);
        item.setProduct("Product \\1");
        item.setQuantity(10);
        item.setDetails(new ArrayList<ItemDet>());
        item.getDetails().add(new ItemDet("something"));
        item.getDetails().add(new ItemDet("another something"));
        order.getItems().add(item);

        item = new Item();
        item.setNumber(2);
        item.setProduct("Product | 002");
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

        File file = new File("ORDER_to_reader.tmp");
        try (Writer w = new FileWriter(file)) {

            DelimitedWriter delimitedWriter = new DelimitedWriter(w);
            delimitedWriter.setDelimiter('|');
            delimitedWriter.setEscape('\\');
            delimitedWriter.setRecordInitializator("");
            delimitedWriter.setRecordTerminator("|\r\n");

            //set new custom TypeHandler as default for a class
            TypeHandlerFactory handlerFactory = delimitedWriter.getObjectParserFactory().getHandlerFactory();
            handlerFactory.registerTypeHandlerClassFor(DateTime.class, DateTimeHandler.class);

            //set new custom TypeHandler as default for the class String
            handlerFactory.registerTypeHandlerClassFor(String.class, LowStringHandler.class);

            //set new custom TypeHandler as default for Enum
            handlerFactory.registerTypeHandlerClassFor(Enum.class, Person.EncodedEnumHandler.class);

            delimitedWriter.write(order);

            w.flush();
            w.close();
        }
        DelimitedReader reader = new DelimitedReader();
        reader.setDelimiter('|');
        reader.setEscape('\\');
        Order o = reader.read(new FileInputStream(file), Order.class);

        Assert.assertNotNull(o);

    }
}
