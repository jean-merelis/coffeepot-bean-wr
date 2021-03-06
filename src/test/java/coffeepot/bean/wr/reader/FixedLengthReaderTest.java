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
import coffeepot.bean.wr.model.file.AFile;
import coffeepot.bean.wr.model.file.Detail;
import coffeepot.bean.wr.typeHandler.DefaultDoubleHandler;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
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
    public void parse_noVersionInReader_shouldNotFillValueFieldWithMinVersion5() throws Exception {
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
        String s = w.toString();

        StringReader r = new StringReader( s );

        FixedLengthReader reader = new FixedLengthReader( r );

        Item i = reader.parse( Item.class );

        assertNotNull( i );
        assertEquals( 1, i.getNumber() );
        assertEquals( "Product", i.getProduct() );
        assertTrue( i.getQuantity().compareTo( 5d ) == 0 );
        assertNull( i.getValue() );

    }

    @Test
    public void parse_readerWithVersion1_shouldNotFillValueFieldWithMaxVersion5() throws Exception {
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
        String s = w.toString();

        StringReader r = new StringReader( s );

        FixedLengthReader reader = new FixedLengthReader( r );

        reader.setVersion( 1 );

        Item i = reader.parse( Item.class );

        assertNotNull( i );
        assertEquals( 1, i.getNumber() );
        assertEquals( "Product", i.getProduct() );
        assertTrue( i.getQuantity().compareTo( 5d ) == 0 );
        assertNull( i.getValue() );
    }

    @Test
    public void parse_readerWithVersion6_shouldNotFillValueFieldWithMaxVersion5() throws Exception {
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
        String s = w.toString();

        StringReader r = new StringReader( s );

        FixedLengthReader reader = new FixedLengthReader( r );
        reader.setVersion( 6 );

        Item i = reader.parse( Item.class );

        assertNotNull( i );
        assertEquals( 1, i.getNumber() );
        assertEquals( "Product", i.getProduct() );
        assertTrue( i.getQuantity().compareTo( 5d ) == 0 );
        assertNull( i.getValue() );
    }

    @Test
    public void parse_readerWithVersion2_shouldFillValueFieldWithMaxVersion5() throws Exception {
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
        String s = w.toString();

        StringReader r = new StringReader( s );

        FixedLengthReader reader = new FixedLengthReader( r );
        reader.setVersion( 2 );

        Item i = reader.parse( Item.class );

        assertNotNull( i );
        assertEquals( 1, i.getNumber() );
        assertEquals( "Product", i.getProduct() );
        assertTrue( i.getQuantity().compareTo( 5d ) == 0 );
        assertNotNull( i.getValue() );
        assertTrue( i.getValue().compareTo( 50d ) == 0 );

    }

    @Test
    public void parse_readerWithVersion5_shouldFillValueFieldWithMaxVersion5() throws Exception {
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
        String s = w.toString();

        StringReader r = new StringReader( s );

        FixedLengthReader reader = new FixedLengthReader( r );
        reader.setVersion( 5 );

        Item i = reader.parse( Item.class );

        assertNotNull( i );
        assertEquals( 1, i.getNumber() );
        assertEquals( "Product", i.getProduct() );
        assertTrue( i.getQuantity().compareTo( 5d ) == 0 );
        assertNotNull( i.getValue() );
        assertTrue( i.getValue().compareTo( 50d ) == 0 );

    }

//    @Test
    public void testRead() throws Exception {
        DefaultDoubleHandler.setDecimalSeparatorDefault( ',' );
        DefaultDoubleHandler.setGroupingSeparatorDefault( '.' );

        Order order = new Order();
        order.setCustomer( "John B" );
        order.setDate( new Date() );
        order.setId( 123 );
        order.setItems( new ArrayList<Item>() );

        Item item = new Item();
        item.setNumber( 1 );
        item.setProduct( "Product 1" );
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

        File file = new File( "ORDER_fixed_to_reader.tmp" );
        try (Writer w = new FileWriter( file )) {

            FixedLengthWriter writer = new FixedLengthWriter( w );
            writer.setRecordTerminator( "\r\n" );

            writer.write( order );

            w.flush();
            w.close();
        }
        FixedLengthReader reader = new FixedLengthReader( new FileReader( file ) );

        Order o = reader.parse( Order.class );

        assertNotNull( o );
        //TODO: check field values

    }

    // @Test
    public void singleClassTest() throws Exception {
        DefaultDoubleHandler.setDecimalSeparatorDefault( ',' );
        DefaultDoubleHandler.setGroupingSeparatorDefault( '.' );

        List<SingleClass> list = new ArrayList<>();

        SingleClass s;

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "22222299999999999999999999999999" );
        list.add( s );

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "222222" );
        list.add( s );

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "222222" );
        list.add( s );

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "222222" );
        list.add( s );

        File file = new File( "single.tmp" );
        try (Writer w = new FileWriter( file )) {

            FixedLengthWriter writer = new FixedLengthWriter( w );
            writer.setRecordTerminator( "\r\n" );

            for (SingleClass sc : list) {
                writer.write( sc );
            }

            w.flush();
            w.close();
        }

        FixedLengthReader reader = new FixedLengthReader( new FileReader( file ) );

        DelimitedReaderTest.SingleClassList o = reader.parse( DelimitedReaderTest.SingleClassList.class );

        reader = new FixedLengthReader( new FileReader( file ) );
        //Vai ler somente a primeira linha
        //It will read only the first line
        SingleClass sc = reader.parse( SingleClass.class );

        Assert.assertNotNull( sc );
        //TODO: check field values
    }

    @Test
    public void shoudlReadAsListOf() throws Exception {

        List<SingleClass> list = new ArrayList<>();

        SingleClass s;

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "22222299999999999999999999999999" );
        list.add( s );

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "222222" );
        list.add( s );

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "222222" );
        list.add( s );

        s = new SingleClass();
        s.setField1( "111111" );
        s.setField2( "222222" );
        list.add( s );

        File file = new File( "single.tmp" );
        try (Writer w = new FileWriter( file )) {

            FixedLengthWriter writer = new FixedLengthWriter( w );
            writer.setRecordTerminator( "\r\n" );

            for (SingleClass sc : list) {
                writer.write( sc );
            }

            w.flush();
            w.close();
        }

        FixedLengthReader reader = new FixedLengthReader( new FileReader( file ) );

        List<SingleClass> records = reader.parseAsListOf( SingleClass.class );

        Assert.assertNotNull( records );
        Assert.assertEquals( 4, records.size() );

        //TODO: check field values
    }

    @Test
    public void readFileWithObjectWrapper() throws Exception {
        File file = new File( "file-to-test-reader.txt" );

        try (FileReader fr = new FileReader( file )) {
            FixedLengthReader reader = new FixedLengthReader( fr );

            AFile aFile = reader.parse( AFile.class );

            assertNotNull( aFile );

            assertNotNull( aFile.getHeader() );
            assertEquals( "HEADER OF FILE", aFile.getHeader().getValue() );

            assertNotNull( aFile.getTrailer() );
            assertEquals( "TRAILER", aFile.getTrailer().getValue() );

            assertNotNull( aFile.getDetails() );
            assertEquals( 4, aFile.getDetails().size() );

            int i = 0;
            for (Detail d : aFile.getDetails()) {
                i++;
                assertNotNull( d.getReg01() );
                assertEquals( "REG 01-" + i, d.getReg01().getValue() );

                if (i == 1) {
                    // in first detail reg02 should be null
                    assertNull( d.getReg02() );
                } else {
                    assertNotNull( d.getReg02() );
                    assertEquals( "REG 02-" + i, d.getReg02().getValue() );
                }
            }
        }
    }

}
