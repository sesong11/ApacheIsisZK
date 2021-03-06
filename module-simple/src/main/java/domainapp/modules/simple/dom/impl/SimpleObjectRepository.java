/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.modules.simple.dom.impl;

import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.datanucleus.api.jdo.JDOQuery;
import org.zkoss.zhtml.P;

import javax.inject.Inject;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = SimpleObject.class
)
public class SimpleObjectRepository {

    public List<SimpleObject> listAll() {
        return repositoryService.allInstances(SimpleObject.class);
    }

    public List<SimpleObject> findByName(final String name) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        SimpleObject.class,
                        "searchByName",
                        "name", name));
    }

    public SimpleObject findById(final long id) {
        return repositoryService.firstMatch(
                new QueryDefault<>(
                        SimpleObject.class,
                        "findById",
                        "id", id));
    }

    public void delete(final SimpleObject simpleObject) {
        SimpleObject object = findById(simpleObject.getId());
        object.delete();
    }

    public void update(final SimpleObject simpleObject) {
        SimpleObject object = findById(simpleObject.getId());
        object.setNotes(simpleObject.getNotes());
    }

    public SimpleObject create(final String name) {
        SimpleObject object = SimpleObject.create(name);
        create(object);
        return object;
    }

    public SimpleObject create(final SimpleObject simpleObject) {
        SimpleObject object = repositoryService.persist(simpleObject);
        return object;
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;
}
