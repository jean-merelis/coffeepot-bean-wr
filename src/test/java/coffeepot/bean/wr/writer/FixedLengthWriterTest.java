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
import coffeepot.bean.wr.mapper.Callback;
import coffeepot.bean.wr.mapper.RecordModel;
import coffeepot.bean.wr.model.Child;
import coffeepot.bean.wr.model.Item;
import coffeepot.bean.wr.model.ItemDet;
import coffeepot.bean.wr.model.Order;
import coffeepot.bean.wr.model.Person;
import coffeepot.bean.wr.typeHandler.DefaultDoubleHandler;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.writer.customHandler.DateTimeHandler;
import coffeepot.bean.wr.writer.customHandler.LowStringHandler;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
        DefaultDoubleHandler.setPatternDefault( "#0.##########" );
        DefaultDoubleHandler.setDecimalSeparatorDefault( DecimalFormatSymbols.getInstance().getDecimalSeparator() );
        DefaultDoubleHandler.setGroupingSeparatorDefault( DecimalFormatSymbols.getInstance().getGroupingSeparator() );
    }

    @After
    public void tearDown() {
    }

    //TODO: MORE TESTS
    @Test
    public void write_noVersionInWriter_shouldNotWriteValueFieldWithMinVersion2() throws Exception {
        Item item = Item.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();
        StringWriter w = new StringWriter();
        FixedLengthWriter writer = new FixedLengthWriter( w );
        writer.setRecordTerminator( "\n" );
        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM 001Product             00005\n", result );
    }

    @Test
    public void write_writerWithVersion1_shouldNotWriteValueFieldWithMinVersion2() throws Exception {
        Item item = Item.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();
        StringWriter w = new StringWriter();
        FixedLengthWriter writer = new FixedLengthWriter( w );
        writer.setRecordTerminator( "\n" );

        writer.setVersion( 1 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM 001Product             00005\n", result );
    }

    @Test
    public void write_writerWithVersion6_shouldNotWriteValueFieldWithMaxVersion5() throws Exception {
        Item item = Item.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();
        StringWriter w = new StringWriter();
        FixedLengthWriter writer = new FixedLengthWriter( w );
        writer.setRecordTerminator( "\n" );

        writer.setVersion( 6 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM 001Product             00005\n", result );
    }

    @Test
    public void write_writerWithVersion2_shouldWriteValueFieldWithMinVersion2() throws Exception {
        Item item = Item.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();
        StringWriter w = new StringWriter();
        FixedLengthWriter writer = new FixedLengthWriter( w );
        writer.setRecordTerminator( "\n" );

        writer.setVersion( 2 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM 001Product             0005000005\n", result );

    }

    @Test
    public void write_writerWithVersion5_shouldWriteValueFieldWithMinVersion5() throws Exception {
        Item item = Item.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();
        StringWriter w = new StringWriter();
        FixedLengthWriter writer = new FixedLengthWriter( w );
        writer.setRecordTerminator( "\n" );

        writer.setVersion( 5 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM 001Product             0005000005\n", result );

    }

    @Test
    public void testWrite() throws Exception {
        System.out.println( "write" );

        File file = new File( "TEST_FIXEDLENGTH_.tmp" );
        Writer w = new FileWriter( file );

        FixedLengthWriter instance = new FixedLengthWriter( w );

        instance.setRecordTerminator( "\r\n" );

        //set new custom TypeHandler as default for a class
        TypeHandlerFactory handlerFactory = instance.getObjectMapperFactory().getHandlerFactory();
        handlerFactory.registerTypeHandlerClassFor( DateTime.class, DateTimeHandler.class );

        //set new custom TypeHandler as default for the class String
        handlerFactory.registerTypeHandlerClassFor( String.class, LowStringHandler.class );

        //set new custom TypeHandler as default for Enum
        handlerFactory.registerTypeHandlerClassFor( Enum.class, Person.EncodedEnumHandler.class );

        DefaultDoubleHandler.setPatternDefault( "#,##0.000" );
        DefaultDoubleHandler.setDecimalSeparatorDefault( ',' );
        DefaultDoubleHandler.setGroupingSeparatorDefault( '.' );

        Person obj = new Person();
        obj.setName( "Jean" );
        obj.setLastName( "Merelis" );
        obj.setAge( 37 );
        obj.setTestNumberOnly( "ad(*&%¨(*&%fd2---14324.32432adfa" );
        obj.setLongNumber( Long.MIN_VALUE );
        obj.setBirthday( DateTime.parse( "2015-03-21" ).toDate() );
        obj.setJodaDateTime( DateTime.parse( "2015-03-21" ) );
        obj.setSalary( 5999.9 );
        obj.setGender( Person.Gender.MALE );

        instance.write( obj );

        obj = new Person();
        obj.setName( "John" );
        obj.setAge( 14 );
        instance.write( obj );

        obj = new Person();
        obj.setName( "Ana" );
        obj.setAge( 11 );
        instance.write( obj );

        obj = new Person();
        obj.setName( "Jean" );
        obj.setAge( 37 );

        List<Child> chidren = new LinkedList<>();

        Child child = new Child();
        child.setName( "John" );
        child.setAge( 14 );
        chidren.add( child );

        child = new Child();
        child.setName( "Ana" );
        child.setAge( 11 );
        chidren.add( child );

        obj.setChildren( chidren );
        instance.write( obj );

        w.flush();
        w.close();

        FileReader in = new FileReader( file );
        try (BufferedReader reader = new BufferedReader( in )) {
            String line;

            line = reader.readLine();
            Assert.assertEquals( 128, line.length() );
            Assert.assertEquals( "jean                          merelis                       0000372015-03-21432adfd2143758082015-03-2105.999,9001FFFFFFF67890000", line );

            line = reader.readLine();
            Assert.assertEquals( 128, line.length() );
            Assert.assertEquals( "john                                                        00001400000000                            0000000000 FFFFFFF67890000", line );

            line = reader.readLine();
            Assert.assertEquals( 128, line.length() );
            Assert.assertEquals( "ana                                                         00001100000000                            0000000000 FFFFFFF67890000", line );

            line = reader.readLine();
            Assert.assertEquals( 128, line.length() );
            Assert.assertEquals( "jean                                                        00003700000000                            0000000000 FFFFFFF67890002", line );

            line = reader.readLine();
            Assert.assertNull( line );

        }
    }

    public void overrideAnnotations() throws Exception {
        //Load a json or create a RecordModel object directly
        final String json = "{\"fields\" : [\n"
                + "		   {\"name\": \"ID\", \"id\" : true, \"constantValue\":\"ORDER\", \"length\":5},\n"
                + "		   {\"name\": \"id\", \"length\":5, \"align\":\"RIGHT\", \"padding\": \"0\"},\n"
                + "		   {\"name\": \"date\", \"length\": 8, \"params\":[\"ddMMyyyy\"]},\n"
                + "		   {\"name\": \"customer\", \"length\":30},\n"
                + "		   {\"name\": \"items\"}\n"
                + "		]}";
        final String jsonItem = "{\"fields\" : [\n"
                + "		   {\"name\": \"ID\", \"id\" : true, \"constantValue\":\"ITEM\", \"length\":5},\n"
                + "		   {\"name\": \"number\", \"length\":3, \"align\":\"RIGHT\", \"padding\": \"0\"},\n"
                + "		   {\"name\": \"product\", \"length\": 30},\n"
                + "		   {\"name\": \"quantity\", \"length\":8, \"align\":\"RIGHT\", \"padding\": \"0\"}\n"
                + "		]}";

        //use a json framework of your choice
        final Gson gson = new Gson();

        Order order = new Order();
        order.setCustomer( "John B" );
        order.setDate( new Date() );
        order.setId( 123 );
        order.setItems( new ArrayList<Item>() );

        Item item = new Item();
        item.setNumber( 1 );
        item.setProduct( "Product1" );
        item.setQuantity( 10d );
        item.setDetails( new ArrayList<ItemDet>() );
        item.getDetails().add( new ItemDet( "something" ) );
        item.getDetails().add( new ItemDet( "another something" ) );
        order.getItems().add( item );

        item = new Item();
        item.setNumber( 2 );
        item.setProduct( "Product 002" );
        item.setQuantity( 5d );
        item.setDetails( new ArrayList<ItemDet>() );
        item.getDetails().add( new ItemDet( "blue" ) );
        item.getDetails().add( new ItemDet( "yellow" ) );
        order.getItems().add( item );

        item = new Item();
        item.setNumber( 3 );
        item.setProduct( "Product 003" );
        item.setQuantity( 2d );
        item.setDetails( new ArrayList<ItemDet>() );
        item.getDetails().add( new ItemDet( "red" ) );
        item.getDetails().add( new ItemDet( "white" ) );
        order.getItems().add( item );

        File file = new File( "ORDER_Over_fixed.tmp" );
        Writer w = new FileWriter( file );

        FixedLengthWriter instance = new FixedLengthWriter( w );
        instance.setRecordTerminator( "\r\n" );

        instance.setCallback( new Callback<Class, RecordModel>() {
            @Override
            public RecordModel call( Class t ) {
                if (Order.class.equals( t )) {
                    return gson.fromJson( json, RecordModel.class );
                }
                if (Item.class.equals( t )) {
                    return gson.fromJson( jsonItem, RecordModel.class );
                }
                return null;
            }
        } );

        instance.write( order );

        w.flush();
        w.close();
    }

}
