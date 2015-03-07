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
import coffeepot.bean.wr.annotation.Field;
import coffeepot.bean.wr.annotation.Record;
import coffeepot.bean.wr.annotation.Records;
import coffeepot.bean.wr.types.Align;
import coffeepot.bean.wr.types.FormatType;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Records({
    @Record(fields = {
        @Field(name = "ID", id = true, constantValue = "ITEM"),
        @Field(name = "number"),
        @Field(name = "product"),
        @Field(name = "quantity"),
        @Field(name = "details")
    }),
    @Record(forFormat = FormatType.FIXED_LENGTH,
            fields = {
                @Field(name = "ID", id = true, constantValue = "ITEM", length = 5),
                @Field(name = "number", length = 3, align = Align.RIGHT, padding = '0'),
                @Field(name = "product", length = 20),
                @Field(name = "quantity", length = 5, align = Align.RIGHT, padding = '0'),
                @Field(name = "details")
            })
})
public class Item {

    private int number;
    private String product;
    private double quantity;
    private List<ItemDet> details;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public List<ItemDet> getDetails() {
        return details;
    }

    public void setDetails(List<ItemDet> details) {
        this.details = details;
    }

}
