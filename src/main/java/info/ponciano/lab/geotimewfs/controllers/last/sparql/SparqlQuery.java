/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.ponciano.lab.geotimewfs.controllers.last.sparql;

/**
 *
 * @author jean-jacquesponciano
 */
public class SparqlQuery {
    String query;

    public SparqlQuery(String query) {
        this.query = query;
    }

    SparqlQuery() {
    
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
}
