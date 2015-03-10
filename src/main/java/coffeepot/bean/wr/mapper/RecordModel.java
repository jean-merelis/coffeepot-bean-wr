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
package coffeepot.bean.wr.mapper;

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

import coffeepot.bean.wr.types.AccessorType;
import coffeepot.bean.wr.types.FormatType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class RecordModel {

    private FormatType forFormat = FormatType.ANY;
    private AccessorType accessorType = AccessorType.DEFAULT;
    private List<FieldModel> fields = new LinkedList<>();
    private String groupId = "";

    public FormatType getForFormat() {
        return forFormat;
    }

    public void setForFormat(FormatType forFormat) {
        this.forFormat = forFormat;
    }

    public AccessorType getAccessorType() {
        return accessorType;
    }

    public void setAccessorType(AccessorType accessorType) {
        this.accessorType = accessorType;
    }

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
