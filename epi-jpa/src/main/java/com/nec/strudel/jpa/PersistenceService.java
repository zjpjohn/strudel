/*******************************************************************************
 * Copyright 2015 Junichi Tatemura
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

package com.nec.strudel.jpa;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.nec.strudel.target.TargetConfig;
import com.nec.strudel.target.TargetCreator;
import com.nec.strudel.target.TargetLifecycle;

public class PersistenceService implements TargetCreator<EntityManager> {

    @Override
    public PersistentStore create(TargetConfig dbConfig) {
        String unitName = dbConfig.getName();
        Properties props = dbConfig.getProperties();
        /**
         * NOTE proprietary feature of EclpseLink:
         */
        props.put(PersistenceUnitProperties.CLASSLOADER,
                dbConfig.targetClassLoader());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                unitName, props);
        return new PersistentStore(emf);
    }

    @Override
    public TargetLifecycle createLifecycle(TargetConfig dbConfig) {
        return new JpaDatabaseCreator(dbConfig);
    }

    @Override
    public Class<?> instrumentedClass(TargetConfig config) {
        return PersistentStore.class;
    }
}
