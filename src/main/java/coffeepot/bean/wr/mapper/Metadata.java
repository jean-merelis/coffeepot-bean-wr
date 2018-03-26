/*
 * Copyright 2017 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.mapper;

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
import lombok.Getter;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class Metadata {

    @Getter private int version;
    @Getter private ImmutableFieldModel fieldModel;

    /**
     * This field is filled with the current raw line on reading process.
     */
    @Getter private String currentRawLine;

    public Metadata(int version) {
        this.version = version;
    }

    public Metadata(int version, ImmutableFieldModel fieldModel) {
        this.version = version;
        this.fieldModel = fieldModel;
    }

    /**
     * For internal use only.
     *
     * @param version
     * @return
     * @deprecated
     */
    public Metadata __setVersion(int version) {
        this.version = version;
        return this;
    }

    /**
     * For internal use only.
     *
     * @param fm
     * @return
     * @deprecated
     */
    @Deprecated
    public Metadata __setFieldModel(FieldModel fm) {
        this.fieldModel = new ImmutableFieldModel(fm);
        return this;
    }

    /**
     * For internal use only.
     *
     * @param s
     * @return
     * @deprecated
     */
    @Deprecated
    public Metadata __setCurrentRawLine(String s) {
        this.currentRawLine = s;
        return this;
    }

}
