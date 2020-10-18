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

import org.apache.jena.query.ResultSet;

/**
 * Interface to manage knowledge.
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
public interface KnowledgeBaseInterface {

    /**
     * Add prefix for every spaql query
     *
     * @param key kew use as prefix
     * @param namespace name space to be added.      <pre>
     * Example for  xsd:type \n"
     * addPrefix(xsd,http://www.w3.org/2001/XMLSchema#)
     * </pre>
     */
    public abstract void addPrefix(String key, String namespace);

    /**
     * Uplift Metadata from an XML file to the ontology
     *
     * @param xml the XML file path on the server.
     * @return true if the elevation succeeded, false otherwise.
     */
    public abstract boolean uplift(String xml);

    /**
     * Downlifts ontology metadata information as a string formatted in XML.
     *
     * @param metadataURI URI of the metadata in the ontology
     * @return the XML String in iso-119115 format.
     */
    public abstract String downlift(String metadataURI) throws OntoManagementException;

    public abstract boolean change(String... param);

    //**************************************************************************
    // ---------------------------- SPARQL -----------------------------------
    //**************************************************************************
    /**
     * Executes SPARQL select function and format the results in an String
     *
     * @param query select query
     * @return query's results in String format.
     */
    public abstract String getSPARQL(String query);

    /**
     * Execute the SPARQL construct query.
     *
     * @param queryString query to process.
     * @return true if the execution was successful.
     * @throws OntoManagementException
     */
    public abstract boolean construct(String queryString);

    /**
     * Executes SPARQL select query.
     * <p>
     * Example of use:
     * <p>
     * <
     * pre><code>
     *   ResultSet select = this.select(query);
     * List<Resource> gts = new ArrayList<>(); while (select.hasNext()) {
     * Resource resource = select.next().getResource(vcode); gts.add(resource);
     * }
     * </code></pre>
     *
     * @param queryString SPARQL query string.
     * @return ResultSet obtained from the query selection.
     */
    public abstract ResultSet select(String queryString);

    /**
     * Execute a update query on the data set
     *
     * @param query query to be executed
     * @throws info.ponciano.lab.pisemantic.OntoManagementException if something
     * bad happens
     */
    public abstract void update(String query) throws OntoManagementException;

}
