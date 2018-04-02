/*
 * Copyright 2015 Jeandeson O. Merelis.
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
package coffeepot.bean.wr.model;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 - 2015 Jeandeson O. Merelis
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
import coffeepot.bean.wr.annotation.Cmd;
import coffeepot.bean.wr.annotation.Field;
import coffeepot.bean.wr.annotation.Record;
import coffeepot.bean.wr.annotation.Records;
import coffeepot.bean.wr.typeHandler.DefaultDateHandler;
import coffeepot.bean.wr.types.Align;
import coffeepot.bean.wr.types.FormatType;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Records({
    @Record(fields = {
        @Field(name = "ID", id = true, constantValue = "ORDER"),
        @Field(name = "id"),
        @Field(name = "date"),
        @Field(name = "customer"),
        @Field(name = "items")
    }),
    @Record(forFormat = FormatType.FIXED_LENGTH,
            fields = {
                @Field(name = "ID", id = true, constantValue = "ORDER", length = 5),
                @Field(name = "id", length = 5, align = Align.RIGHT, padding = '0'),
                @Field(name = "date", length = 8,
                        commands = {
                            @Cmd(name = DefaultDateHandler.CMD_SET_PATTERN, args = {"ddMMyyyy"})}
                ),
                @Field(name = "customer", length = 30),
                @Field(name = "items")
            })
})
public class Order {

    private Integer id;
    private Date date;
    private String customer;
    private List<Item> items;
    private double total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
