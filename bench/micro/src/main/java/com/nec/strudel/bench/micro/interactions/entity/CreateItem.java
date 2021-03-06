/*******************************************************************************
 * Copyright 2015, 2016 Junichi Tatemura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.nec.strudel.bench.micro.interactions.entity;

import com.nec.strudel.bench.micro.entity.Item;
import com.nec.strudel.bench.micro.interactions.base.AbstractCreateItem;
import com.nec.strudel.bench.micro.params.SessionParam;
import com.nec.strudel.entity.EntityDB;
import com.nec.strudel.session.Interaction;
import com.nec.strudel.session.Param;
import com.nec.strudel.session.Result;
import com.nec.strudel.session.ResultBuilder;

/**
 * An interaction that emulate creation of a new private item by a particular
 * user. It requires the following parameters set:
 * <ul>
 * <li>USER_ID
 * <li>CONTENT_LENGTH
 * </ul>
 * It does not change any transitional session parameters.
 * 
 * @author tatemura
 *
 */
public class CreateItem extends AbstractCreateItem<EntityDB>
        implements Interaction<EntityDB> {
    @Override
    public Result execute(Param param, EntityDB db, ResultBuilder res) {
        final int userId = param.getInt(SessionParam.USER_ID);
        final String content = param.get(InParam.CONTENT);
        Item item = new Item(userId);
        item.setContent(content);
        db.create(item);
        return res.success();
    }

}
