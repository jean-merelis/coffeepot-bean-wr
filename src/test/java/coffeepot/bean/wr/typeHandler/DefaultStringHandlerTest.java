/*
 * Copyright 2018 Jeandeson O. Merelis.
 *
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
 */
package coffeepot.bean.wr.typeHandler;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 - 2018 Jeandeson O. Merelis
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
import coffeepot.bean.wr.mapper.Command;
import coffeepot.bean.wr.mapper.Metadata;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class DefaultStringHandlerTest {

    @Test
    public void parse_withFilterNumbersOnly_shouldReturnOnlyNumbers() throws Exception {
        System.out.println("parse");
        String text = "Test1235 ok";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_FILTER).args(new String[]{DefaultStringHandler.FILTER_NUMBER_ONLY}).build()
        });
        String expResult = "1235";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void parse_withFilterNumbersAndLettersOnly_shouldReturnOnlyNumbersAndLetters() throws Exception {
        System.out.println("parse");
        String text = " abcd()!@#$%¨&*()12345xyz, . : ;/?ABC ";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_FILTER).args(new String[]{DefaultStringHandler.FILTER_NUMBER_LETTERS_ONLY}).build()
        });

        String expResult = "abcd12345xyzABC";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void parse_withParamCharCaseUPPER_shouldReturnAllLettersAsCapitalLetters() throws Exception {
        System.out.println("parse");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_CHARCASE).args(new String[]{DefaultStringHandler.CHARCASE_UPPER}).build()
        });

        String expResult = "TEST ABCD A";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void parse_withParamReplace_shouldReturnProperly() throws Exception {
        System.out.println("parse");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba"}).build()
        });

        String expResult = "test dcba A";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void parse_withParamReplaceOnlyOnRead_shouldReturnProperly() throws Exception {
        System.out.println("parse");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba", DefaultStringHandler.OPT_ONLY_ON_READ}).build()
        });

        String expResult = "test dcba A";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void parse_withParamReplaceOnlyOnWrite_shouldNotChange() throws Exception {
        System.out.println("parse");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba", DefaultStringHandler.OPT_ONLY_ON_WRITE}).build()
        });

        String expResult = "test abcd A";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void parse_withSeveralReplace_shouldReturnProperly() throws Exception {
        System.out.println("parse");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"dcba", "ok"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"test", "this"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"this", "this test is"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{" A", ""}).build()
        });
        String expResult = "this test is ok";
        String result = instance.parse(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withFilterNumbersOnly_shouldReturnOnlyNumbers() throws Exception {
        System.out.println("toString");
        String text = "Test1235 ok";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_FILTER).args(new String[]{DefaultStringHandler.FILTER_NUMBER_ONLY}).build()
        });
        String expResult = "1235";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withFilterNumbersAndLettersOnly_shouldReturnOnlyNumbersAndLetters() throws Exception {
        System.out.println("toString");
        String text = " abcd()!@#$%¨&*()12345xyz, . : ;/?ABC ";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_FILTER).args(new String[]{DefaultStringHandler.FILTER_NUMBER_LETTERS_ONLY}).build()
        });

        String expResult = "abcd12345xyzABC";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withParamCharCaseUPPER_shouldReturnAllLettersAsCapitalLetters() throws Exception {
        System.out.println("toString");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_CHARCASE).args(new String[]{DefaultStringHandler.CHARCASE_UPPER}).build()
        });

        String expResult = "TEST ABCD A";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withParamReplace_shouldReturnProperly() throws Exception {
        System.out.println("toString");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba"}).build()
        });

        String expResult = "test dcba A";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withParamReplaceOnlyOnWrite_shouldReturnProperly() throws Exception {
        System.out.println("toString");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba", DefaultStringHandler.OPT_ONLY_ON_WRITE}).build()
        });

        String expResult = "test dcba A";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withParamReplaceOnlyOnRead_shouldNotChange() throws Exception {
        System.out.println("toString");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba", DefaultStringHandler.OPT_ONLY_ON_READ}).build()
        });


        String expResult = "test abcd A";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withSeveralReplace_shouldReturnProperly() throws Exception {
        System.out.println("toString");
        String text = "test abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();
        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"abcd", "dcba"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"dcba", "ok"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"test", "this"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{"this", "this test is"}).build(),
            Command.builder().name(DefaultStringHandler.CMD_REPLACE).args(new String[]{" A", ""}).build()
        });
        String expResult = "this test is ok";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withReplaceAll_shouldReturnProperly() throws Exception {
        System.out.println("toString");
        String text = "test Abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE_ALL).args(new String[]{"\\s+", ""}).build()
        });

        String expResult = "testAbcdA";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withReplaceAll_shouldReturnProperly2() throws Exception {
        System.out.println("toString");
        String text = "test Abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE_ALL).args(new String[]{"\\s+", "_"}).build()
        });

        String expResult = "test_Abcd_A";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }

    @Test
    public void toString_withReplaceFirst_shouldReturnProperly() throws Exception {
        System.out.println("toString");
        String text = "test  Abcd A";
        Metadata metadata = null;
        DefaultStringHandler instance = new DefaultStringHandler();

        instance.config(new Command[]{
            Command.builder().name(DefaultStringHandler.CMD_REPLACE_FIRST).args(new String[]{"\\s+", "_"}).build()
        });

        String expResult = "test_Abcd A";
        String result = instance.toString(text, metadata);
        assertEquals(expResult, result);
    }
}
