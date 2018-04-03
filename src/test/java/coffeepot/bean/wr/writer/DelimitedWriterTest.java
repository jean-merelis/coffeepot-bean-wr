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
import coffeepot.bean.wr.model.Item2;
import coffeepot.bean.wr.model.ItemDet;
import coffeepot.bean.wr.model.Job;
import coffeepot.bean.wr.model.Order;
import coffeepot.bean.wr.model.Parent;
import coffeepot.bean.wr.model.Person;
import coffeepot.bean.wr.model.UnannotatedClass;
import coffeepot.bean.wr.typeHandler.DefaultDoubleHandler;
import coffeepot.bean.wr.typeHandler.TypeHandlerFactory;
import coffeepot.bean.wr.writer.customHandler.LowStringHandler;
import coffeepot.bean.wr.writer.customHandler.DateTimeHandler;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
    public void write_noVersionInWriter_shouldNotWriteValueFieldWithMinVersion2() throws Exception {
        Item item = Item.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();
        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM|1|Product|5|\n", result );
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

        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.setVersion( 1 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM|1|Product|5|\n", result );
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

        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.setVersion( 6 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM|1|Product|5|\n", result );
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


        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.setVersion( 2 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM|1|Product|50|5|\n", result );

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

        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.setVersion( 5 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
     Assert.assertEquals( "ITEM|1|Product|50|5|\n", result );

    }
    
    @Test
    public void write_writerWithVersion1_shouldWriteNormalValueFromConditionalFieldsWithMinVersion5() throws Exception {
        Item2 item = Item2.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();

        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.setVersion( 1 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM2|1|Product|50|5|\n", result );
    }    
    
    @Test
    public void write_writerWithVersion5_shouldWriteConditionalValueFromConditionalFieldsWithMinVersion5() throws Exception {
        Item2 item = Item2.builder()
                .number( 1 )
                .product( "Product" )
                .quantity( 5d )
                .value( 50d )
                .build();

        StringWriter w = new StringWriter();

        DelimitedWriter writer = new DelimitedWriter( w );
        writer.setDelimiter( '|' );
        writer.setEscape( '\\' );
        writer.setRecordTerminator( "|\n" );

        writer.setVersion( 5 );

        writer.write( item );
        writer.flush();
        String result = w.toString();
        Assert.assertEquals( "ITEM2||New product name|50|5|\n", result );
    }    
    

    //TODO: MORE TESTS
    @Test
    public void testWrite() throws Exception {

        File file = new File( "TESTE_.tmp" );
        Writer w = new FileWriter( file );

        DelimitedWriter instance = new DelimitedWriter( w );
        instance.setDelimiter( '|' );
        instance.setEscape( '\\' );
        instance.setRecordInitializator( "" );
        instance.setRecordTerminator( "|\r\n" );

        //set new custom TypeHandler as default for a class
        TypeHandlerFactory handlerFactory = instance.getObjectMapperFactory().getHandlerFactory();
        handlerFactory.registerTypeHandler( DateTime.class, DateTimeHandler.class );

        //set new custom TypeHandler as default for the class String
        handlerFactory.registerTypeHandler( String.class, LowStringHandler.class );

        //set new custom TypeHandler as default for Enum
        handlerFactory.registerTypeHandler( Enum.class, Person.EncodedEnumHandler.class );

        DefaultDoubleHandler.setDecimalSeparatorDefault( ',' );
        DefaultDoubleHandler.setGroupingSeparatorDefault( '.' );

        //instance.createParser(Person.class);
        Person obj = new Person();
        obj.setName( "Jean" );
        obj.setAge( 37 );
        obj.setLongNumber( Long.MIN_VALUE );
//        obj.setBirthday(new Date());
        obj.setJodaDateTime( DateTime.now() );
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

        List<Job> jobs = new LinkedList<>();

        jobs.add( new Job( "initial\\First Job", "11", "22", "33" ) );
        jobs.add( new Job( "Second job|escape test", "44", "55", "66" ) );
        jobs.add( new Job( "3th job", "77", "88", "99" ) );
        jobs.add( new Job( "4th job", "00", "aa", "bb" ) );

        obj.setJobs( jobs );
        instance.write( obj );

//        instance.clearParsers();
//        instance.write(obj, "testGroupRecord");
        w.flush();
        w.close();

        FileReader in = new FileReader( file );
        try (BufferedReader reader = new BufferedReader( in )) {
            String line;

            line = reader.readLine();
            Assert.assertEquals( "PERSON|jean|5||0|", line );

            line = reader.readLine();
            Assert.assertEquals( "PERSON|john|5||0|", line );

            line = reader.readLine();
            Assert.assertEquals( "PERSON|ana|5||0|", line );

            line = reader.readLine();
            Assert.assertEquals( "PERSON|jean|5||2|", line );

            line = reader.readLine();
            Assert.assertEquals( "initial\\\\first job|33|", line );

            line = reader.readLine();
            Assert.assertEquals( "second job\\|escape test|66|", line );

            line = reader.readLine();
            Assert.assertEquals( "3th job|99|", line );

            line = reader.readLine();
            Assert.assertEquals( "4th job|bb|", line );

            line = reader.readLine();
            Assert.assertNull( line );

        }

    }

    //@Test
    public void writePersonWithParentField() throws Exception {

        File file = new File( "TESTE_.tmp" );
        Writer w = new FileWriter( file );

        DelimitedWriter instance = new DelimitedWriter( w );
        instance.setDelimiter( '|' );
        instance.setEscape( '\\' );
        instance.setRecordInitializator( "" );
        instance.setRecordTerminator( "|\r\n" );

        //set new custom TypeHandler as default for a class
        TypeHandlerFactory handlerFactory = instance.getObjectMapperFactory().getHandlerFactory();
        handlerFactory.registerTypeHandler( DateTime.class, DateTimeHandler.class );

        //set new custom TypeHandler as default for the class String
        handlerFactory.registerTypeHandler( String.class, LowStringHandler.class );

        //set new custom TypeHandler as default for Enum
        handlerFactory.registerTypeHandler( Enum.class, Person.EncodedEnumHandler.class );

        //instance.createParser(Person.class);
        Person obj = new Person();
        obj.setName( "Jean" );
        obj.setAge( 37 );
        obj.setLongNumber( Long.MIN_VALUE );
//        obj.setBirthday(new Date());
        obj.setJodaDateTime( DateTime.now() );
        obj.setSalary( 5999.9 );
        obj.setGender( Person.Gender.MALE );

        instance.write( obj );

        obj = new Person();
        obj.setName( "John" );
        obj.setAge( 14 );
        obj.setParent( new Parent( "LastName" ) );
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

        List<Job> jobs = new LinkedList<>();

        jobs.add( new Job( "initial\\First Job", "11", "22", "33" ) );
        jobs.add( new Job( "Second job|escape test", "44", "55", "66" ) );
        jobs.add( new Job( "3th job", "77", "88", "99" ) );
        jobs.add( new Job( "4th job", "00", "aa", "bb" ) );

        obj.setJobs( jobs );
        instance.write( obj );

//        instance.clearParsers();
//        instance.write(obj, "testGroupRecord");
        w.flush();
        w.close();

        FileReader in = new FileReader( file );
        try (BufferedReader reader = new BufferedReader( in )) {
            String line;

            line = reader.readLine();
            Assert.assertEquals( "PERSON|jean|5||0|", line );

            line = reader.readLine();
            Assert.assertEquals( "PERSON|john|5||0|", line );
            line = reader.readLine();
            Assert.assertEquals( "lastname|", line );

            line = reader.readLine();
            Assert.assertEquals( "PERSON|ana|5||0|", line );

            line = reader.readLine();
            Assert.assertEquals( "PERSON|jean|5||2|", line );

            line = reader.readLine();
            Assert.assertEquals( "initial\\\\first job|33|", line );

            line = reader.readLine();
            Assert.assertEquals( "second job\\|escape test|66|", line );

            line = reader.readLine();
            Assert.assertEquals( "3th job|99|", line );

            line = reader.readLine();
            Assert.assertEquals( "4th job|bb|", line );

            line = reader.readLine();
            Assert.assertNull( line );

        }

    }

    //@Test
    public void testWrite2() throws Exception {
        Order order = new Order();
        order.setCustomer( "John B" );
        order.setDate( new Date() );
        order.setId( 123 );
        order.setItems( new ArrayList<Item>() );

        Item item = new Item();
        item.setNumber( 1 );
        item.setProduct( "Product1" );
        item.setQuantity( 10d );
        item.setValue( 50d );
        item.setDetails( new ArrayList<ItemDet>() );
        item.getDetails().add( new ItemDet( "something" ) );
        item.getDetails().add( new ItemDet( "another something" ) );
        order.getItems().add( item );

        item = new Item();
        item.setNumber( 2 );
        item.setProduct( "Product 002" );
        item.setQuantity( 5d );
        item.setValue( 10d );
        item.setDetails( new ArrayList<ItemDet>() );
        item.getDetails().add( new ItemDet( "blue" ) );
        item.getDetails().add( new ItemDet( "yellow" ) );
        order.getItems().add( item );

        item = new Item();
        item.setNumber( 3 );
        item.setProduct( "Product 003" );
        item.setQuantity( 2d );
        item.setValue( 50d );
        item.setDetails( new ArrayList<ItemDet>() );
        item.getDetails().add( new ItemDet( "red" ) );
        item.getDetails().add( new ItemDet( "white" ) );
        order.getItems().add( item );

        File file = new File( "ORDER.tmp" );
        Writer w = new FileWriter( file );

        DelimitedWriter instance = new DelimitedWriter( w );
        instance.setDelimiter( '|' );
        instance.setEscape( '\\' );
        instance.setRecordInitializator( "" );
        instance.setRecordTerminator( "|\r\n" );

        //set new custom TypeHandler as default for a class
        TypeHandlerFactory handlerFactory = instance.getObjectMapperFactory().getHandlerFactory();
        handlerFactory.registerTypeHandler( DateTime.class, DateTimeHandler.class );

        //set new custom TypeHandler as default for the class String
        handlerFactory.registerTypeHandler( String.class, LowStringHandler.class );

        //set new custom TypeHandler as default for Enum
        handlerFactory.registerTypeHandler( Enum.class, Person.EncodedEnumHandler.class );

        instance.write( order );

        w.flush();
        w.close();
    }

    //@Test
    public void overrideAnnotations() throws Exception {
        //Load a json or create a RecordModel object directly
        final String json = "{\"forFormat\":\"DELIMITED\", \"fields\" : [{\"name\" : \"ID\", \"id\" : true, \"constantValue\" : \"ORDER\"},{\"name\": \"date\"},{\"name\":\"items\"}]}";
        final String jsonItem = "{\"forFormat\":\"DELIMITED\", \"fields\" : [{\"name\" : \"ID\", \"id\" : true, \"constantValue\" : \"IT\"},{\"name\": \"product\"}]}";

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

        File file = new File( "ORDER_override.tmp" );
        Writer w = new FileWriter( file );

        DelimitedWriter instance = new DelimitedWriter( w );
        instance.setDelimiter( '|' );
        instance.setEscape( '\\' );
        instance.setRecordInitializator( "" );
        instance.setRecordTerminator( "|\r\n" );

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

    //@Test
    public void unannotatedClassTest() throws Exception {
        //Load a json or create a RecordModel object directly
        final String json = "{\"fields\" : [{\"name\" : \"doubleField\"},{\"name\": \"field\"}]}";

        //use a json framework of your choice
        final Gson gson = new Gson();

        List<UnannotatedClass> list = new ArrayList<>();

        UnannotatedClass u = new UnannotatedClass();
        u.setIntegerField( 123 );
        u.setField( "it is ok" );
        u.setDoubleField( 3.3 );

        list.add( u );

        u = new UnannotatedClass();
        u.setIntegerField( 2452 );
        u.setField( "it is work" );
        u.setDoubleField( 1.1 );

        list.add( u );

        u = new UnannotatedClass();
        u.setIntegerField( 2015 );
        u.setField( "very nice" );
        u.setDoubleField( 555.0 );

        list.add( u );

        u = new UnannotatedClass();
        u.setIntegerField( 2016 );
        u.setField( "Please! make a decent test" );
        u.setDoubleField( 222.0 );

        list.add( u );

        File file = new File( "unnanotated.tmp" );
        Writer w = new FileWriter( file );

        DelimitedWriter instance = new DelimitedWriter( w );
        instance.setDelimiter( '|' );
        instance.setEscape( '\\' );
        instance.setRecordInitializator( "" );
        instance.setRecordTerminator( "|\r\n" );

        instance.setCallback( new Callback<Class, RecordModel>() {

            @Override
            public RecordModel call( Class t ) {
                if (UnannotatedClass.class.equals( t )) {
                    return gson.fromJson( json, RecordModel.class );
                }
                return null;
            }
        } );

        instance.write( list );

        w.flush();
        w.close();
    }
}
