/*
 * Copyright (C) 2020 Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.geotimewfs.models.semantic;

import java.io.IOException;
import org.apache.jena.query.ResultSet;

/**
 * Knowledge base singleton class to manage semantic access.
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
public class KB implements KnowledgeBaseInterface {

    private static KB kb = null;
    private static final String SRC_ONTO = "src/main/resources/ontologies/iso-19115.owl";
    private static final String OUT_ONTO = "geotimeOutput.owl";
    private OwlManagement model;

    public static KB get() throws OntoManagementException {
        if (kb == null) {
            kb = new KB();
        }
        return kb;
    }

    private KB() throws OntoManagementException {
        this.model = new OwlManagement(SRC_ONTO);
    }

    /**
     * Save the current ontology in an OWL file.
     *
     * @throws IOException If the file cannot be written.
     */
    public void save() throws IOException {
        this.model.saveOntology(OUT_ONTO);
    }

    @Override
    public void addPrefix(String key, String namespace) {
        this.model.addPrefix(key, namespace);
    }

    @Override
    public boolean uplift(String xml) {
        return this.model.uplift(xml);
    }

    @Override
    public String downlift(String metadataURI) throws OntoManagementException {
        return this.model.downlift(metadataURI);
    }

    @Override
    public boolean change(String... param) {
        return this.model.change(param);
    }

    @Override
    public String getSPARQL(String query) {
        return this.model.getSPARQL(query);
    }

    @Override
    public boolean construct(String queryString) throws OntoManagementException {
        return this.model.construct(queryString);
    }

    @Override
    public ResultSet select(String queryString) {
        return this.model.select(queryString);
    }

    @Override
    public void update(String query) throws OntoManagementException {
         this.model.update(query);
    }

}
