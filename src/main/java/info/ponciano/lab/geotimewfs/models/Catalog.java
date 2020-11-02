/*
 * Copyright (C) 2020 claireprudhomme.
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
package info.ponciano.lab.geotimewfs.models;

import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author claireprudhomme
 */
public class Catalog {
    private JSONObject jo;
    private String catalogId;

    public Catalog(){
    }
    
    public Catalog(String catalogId) throws OntoManagementException {
        this.catalogId=catalogId;
        this.jo=new JSONObject();
        this.jo.put("recordid",catalogId);
        this.jo.put("type","catalogue");
        List<String[]> info = initCatalogInfo();
        String title=info.get(0)[0];
        if(title.contains("@")){
            String[] tsplit=title.split("@");
            title=tsplit[0];
        }    
        System.out.println(title);
        this.jo.put("title",title);
        String description=info.get(0)[1];
        if(description.contains("@")){
            String[] tsplit=description.split("@");
            description=tsplit[0];
        } 
        System.out.println(description);
        this.jo.put("description",description);
        info = initCatalogDistributionInfo();
        JSONArray jodists=new JSONArray();
        for (int i=0; i<info.size(); i++)
        {
            //create a JSON object for each distribution of the catalog
            JSONObject jodist=new JSONObject();
            jodist.put("href", info.get(i)[0]);
            jodist.put("rel", "collection");
            title=info.get(i)[1];
            if(title.contains("@")){
                String[] tsplit=title.split("@");
                title=tsplit[0];
            }
            System.out.println(title);
            jodist.put("title", title);
            //add its description to an array
            jodists.put(jodist);
        }
        //add the array containing all the distribution collection to the JSON object representing the catalog
        this.jo.put("links", jodists);
    }
    
    private List<String[]> initCatalogInfo() throws OntoManagementException{
        List<String[]> info = new ArrayList<String[]>();
        //initialize the query to retrieve the title and the description of the catalog
            String query = "SELECT ?t ?d "
                    + "WHERE{"
                    +"gtdcat:"+this.catalogId+" <http://purl.org/dc/elements/1.1/title> ?t."
                    +"gtdcat:"+this.catalogId+" <http://purl.org/dc/elements/1.1/description> ?d. "
                    + "}";
            System.out.println(KB.get().getSPARQL(query));
            //create the table of variables
            String[] var = {"t", "d"};
            //query the ontology
            info = KB.get().queryAsArray(query, var, false, false);
            return info;
    }
    
    private List<String[]> initCatalogDistributionInfo() throws OntoManagementException{
        List<String[]> info = new ArrayList<String[]>();
        //initialize the query to retrieve the link and the title of each catalog distribution
            String query = "SELECT ?l ?t "
                    + "WHERE{"
                    +"gtdcat:"+this.catalogId+" dcat:distribution ?d. "
                    +"?d dcat:accessService ?s. "
                    +"?s dcat:endpointURL ?l. "
                    +"?s <http://purl.org/dc/elements/1.1/title> ?t. "
                    + "}";
            System.out.println(KB.get().getSPARQL(query));
            //create the table of variables
            String[] var = {"l", "t"};
            //query the ontology
            info = KB.get().queryAsArray(query, var, true, false);
            return info;
    }

    public JSONObject getJo() {
        return this.jo;
    }

    public void setJo(JSONObject jo) {
        this.jo = jo;
    }
    
    
    
}
