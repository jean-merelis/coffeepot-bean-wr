/*
 * Copyright 2017 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.model;

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

import coffeepot.bean.wr.annotation.Field;
import coffeepot.bean.wr.annotation.FieldCondition;
import coffeepot.bean.wr.annotation.Record;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jeandeson O. Merelis
 */
@Record(fields = {
    @Field(name = "ID", id = true, constantValue = "ITEM2"),
    @Field(name = "number", writeAsNull = @FieldCondition(active = true, minVersion = 5, maxVersion = 6)),
    @Field(name = "product", 
            writeAs = "New product name", 
            conditionForWriteAs = @FieldCondition(active = true, minVersion = 5, maxVersion = 6),
            readAs = "Another name from readAs",
            conditionForReadAs = @FieldCondition(active = true, minVersion = 5, maxVersion = 6)),
    @Field(name = "value"),
    @Field(name = "quantity"),
    @Field(name = "details")    
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item2 {

    private int number;
    private String product;
    private Double value;
    private Double quantity;
    private List<ItemDet> details;
}
