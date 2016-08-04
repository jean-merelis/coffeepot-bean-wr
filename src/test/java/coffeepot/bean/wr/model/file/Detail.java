package coffeepot.bean.wr.model.file;

/*
 * #%L
 * coffeepot-bean-wr
 * %%
 * Copyright (C) 2013 - 2016 Jeandeson O. Merelis
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

/**
 *
 * @author Jeandeson O. Merelis
 */

@Record(fields = {
    @Field(name="reg01"),
    @Field(name="reg02")
})

public class Detail {
    private Reg01 reg01;
    private Reg02 reg02;

    public Reg01 getReg01() {
        return reg01;
    }

    public void setReg01(Reg01 reg01) {
        this.reg01 = reg01;
    }

    public Reg02 getReg02() {
        return reg02;
    }

    public void setReg02(Reg02 reg02) {
        this.reg02 = reg02;
    }


}
