/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.bean.wr.mapper;

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
import coffeepot.bean.wr.annotation.Cmd;
import coffeepot.bean.wr.annotation.Field;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class Helpful {

    public static FieldModel toFieldImpl(Field nf) {
        FieldModel f = new FieldModel();
        f.setId(nf.id());
        f.setAccessorType(nf.accessorType());
        f.setAlign(nf.align());
        f.setClassType(nf.classType());
        f.setCollection(false);
        f.setConstantValue(nf.constantValue());
        f.setGetter(nf.getter());
        f.setLength(nf.length());
        f.setMaxLength(nf.maxLength());
        f.setMinLength(nf.minLength());
        f.setName(nf.name());
        f.setPadding(nf.padding());
        f.setPaddingIfNullOrEmpty(nf.paddingIfNullOrEmpty());
        f.setCommands(toCmdModel(nf.commands()));
        f.setSetter(nf.setter());
        f.setTrim(nf.trim());
        f.setTypeHandlerClass(nf.typeHandler());
        if (nf.length() > 0) {
            f.setMinLength(nf.length());
            f.setMaxLength(nf.length());
        }
        f.setMinVersion(nf.minVersion());
        f.setMaxVersion(nf.maxVersion());

        if (nf.conditionForWriteAs().active()) {
            f.setWriteAs(nf.writeAs());
            f.setConditionForWriteAs(FieldConditionModel.builder()
                    .active(true)
                    .always(nf.conditionForWriteAs().always())
                    .minVersion(nf.conditionForWriteAs().minVersion())
                    .maxVersion(nf.conditionForWriteAs().maxVersion())
                    .build());
        }
        if (nf.writeAsNull().active()) {
            f.setWriteAsNull(
                    FieldConditionModel.builder()
                            .active(true)
                            .always(nf.writeAsNull().always())
                            .minVersion(nf.writeAsNull().minVersion())
                            .maxVersion(nf.writeAsNull().maxVersion())
                            .build()
            );
        }

        if (nf.conditionForReadAs().active()) {
            f.setReadAs(nf.readAs());
            f.setConditionForReadAs(FieldConditionModel.builder()
                    .active(true)
                    .always(nf.conditionForReadAs().always())
                    .minVersion(nf.conditionForReadAs().minVersion())
                    .maxVersion(nf.conditionForReadAs().maxVersion())
                    .build());
        }
        if (nf.readAsNull().active()) {
            f.setReadAsNull(
                    FieldConditionModel.builder()
                            .active(true)
                            .always(nf.readAsNull().always())
                            .minVersion(nf.readAsNull().minVersion())
                            .maxVersion(nf.readAsNull().maxVersion())
                            .build()
            );
        }

        return f;
    }

    public static Command[] toCmdModel(Cmd[] cmds) {
        Command[] result = new Command[cmds.length];
        for (int i = 0; i < cmds.length; i++) {
            Cmd cmd = cmds[i];
            Command cm = new Command();
            cm.name = cmd.name();
            cm.args = cmd.args();
            result[i] = cm;
        }
        return result;
    }
}
