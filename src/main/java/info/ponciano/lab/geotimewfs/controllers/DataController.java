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
package info.ponciano.lab.geotimewfs.controllers;

import info.ponciano.lab.geotimewfs.models.Metadata;
import info.ponciano.lab.geotimewfs.models.SemanticWFSRequest;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagement;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author claireprudhomme
 */
@Controller
public class DataController {

    private SemanticWFSRequest swfs;

    @Autowired
    public DataController() {
        swfs = new SemanticWFSRequest();
    }

    //initialize the model attribute
    @ModelAttribute(name = "metadata")
    public Metadata metadata() {
        return new Metadata();
    }

    //generate the metadata2data view
    @GetMapping("/md2data")
    public String getmd2dataView(Model model) {
        String rtn = "metadata2data";
        try {
            //retrieve metadata
            Metadata md = new Metadata();
            List<String[]> info = md.getMetadata();
            //providing the list of info to the model to allow the view to display all available metadata
            model.addAttribute("MDlist", info);
        } catch (OntoManagementException ex) {
            Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        //retrieve data collections from the semantic WFS
        try {
            String jsoncollection = swfs.getJSONCollections();
            @SuppressWarnings("deprecation")
			JSONParser parser = new JSONParser();
            Object json = parser.parse(jsoncollection);
            model.addAttribute("objJSON", json);
        } catch (IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "Collection retrieving from the semantic WFS has failed: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;

    }

    //associate the metadata to a data collection to add them to the catalog web service
    //modeling using the dcat vocabulary: https://www.w3.org/TR/vocab-dcat-2/
    @PostMapping("/addmd2data")
    public String addmd2data(@ModelAttribute("metadata") Metadata metadata, Model model) {
        System.out.println("metadata: " + metadata.getDelivryMd());
        OntModel ont= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        String gtdcat="http://lab.ponciano.info/ontology/2020/geotime/dcat#";
        //retrieve the selected metadata uri
        try {
            List<String[]> info = metadata.getMetadata();
            int i = 0;
            boolean found = false;
            String mduri="";
            while (i < info.size() && !found) {
                if (info.get(i)[2].equals(metadata.getDelivryMd())) {
                    mduri = info.get(i)[0];
                    //add the metadata as a catalog record 
                    OntClass c=ont.createClass("http://www.w3.org/ns/dcat#CatalogRecord");
                    c.createIndividual(OntoManagement.NS + mduri);
                    /*String query = "INSERT DATA { <" + OntoManagement.NS + mduri + "> rdf:type <http://www.w3.org/ns/dcat#CatalogRecord> . "
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    
                    //add the catalog
                    c=ont.createClass("http://www.w3.org/ns/dcat#Catalog");
                    c.createIndividual("http://lab.ponciano.info/ontology/2020/geotime/dcat#gdi_catalog");
                    /*query = "INSERT DATA { "
                            + "gtdcat:gdi_catalog rdf:type <http://www.w3.org/ns/dcat#Catalog>."
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    
                    //add the metadata as a record of the catalog
                    ObjectProperty p=ont.createObjectProperty("http://www.w3.org/ns/dcat#record");
                    ont.add(ont.getResource(gtdcat+"gdi_catalog"),p,ont.getResource(OntoManagement.NS + mduri));
                    /*query = "INSERT DATA { "
                            + "gtdcat:gdi_catalog <http://www.w3.org/ns/dcat#record> <" + OntoManagement.NS + mduri + ">."
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    found = true;
                }
                i++;
            }

            //retrieve the information of the data collection
            String jsoncollection = swfs.getJSONCollections();
            JSONObject jo = new JSONObject(jsoncollection);
            JSONArray jsarray = jo.getJSONArray("collections");
            System.out.println("dataset: " + metadata.getDelivryDS());
            int j = 0;
            boolean foundc = false;
            while (j < jsarray.length() && !foundc) {
                JSONObject collection = (JSONObject) jsarray.get(j);
                if (collection.getString("name").equals(metadata.getDelivryDS())) {
                    System.out.println(collection.getString("name"));
                    //create dataset
                    UUID dsUri = UUID.randomUUID();
                    OntClass c=ont.createClass("http://www.w3.org/ns/dcat#Dataset");
                    c.createIndividual(gtdcat+dsUri);
                    /*
                    String query = "INSERT DATA { gtdcat:" + dsUri + " rdf:type <http://www.w3.org/ns/dcat#Dataset> . "
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    
                    //add the title of the dataset from its collection 
                    DatatypeProperty dp=ont.createDatatypeProperty("http://purl.org/dc/elements/1.1/title");
                    ont.add(ont.getResource(gtdcat+dsUri),dp,collection.getString("name"));
                    /*query = "INSERT DATA { gtdcat:" + dsUri + " <http://purl.org/dc/elements/1.1/title> \"" + collection.getString("name") + "\" . "
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    
                    //link dataset to catalog through dcat:dataset
                    ObjectProperty op=ont.createObjectProperty("http://www.w3.org/ns/dcat#dataset");
                    ont.add(ont.getResource(gtdcat+"gdi_catalog"),op,ont.getResource(gtdcat+dsUri));
                    /*query = "INSERT DATA { "
                            + "gtdcat:gdi_catalog <http://www.w3.org/ns/dcat#dataset> gtdcat:" + dsUri + "."
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    
                    //link dataset to metadata record through foaf:primaryTopic
                    op=ont.createObjectProperty("http://xmlns.com/foaf/0.1/primaryTopic");
                    ont.add(ont.getResource(OntoManagement.NS + mduri),op,ont.getResource(gtdcat+dsUri));
                    /*query = "INSERT DATA { "
                            + "<" + OntoManagement.NS + mduri + "> <http://xmlns.com/foaf/0.1/primaryTopic> gtdcat:" + dsUri + "."
                            + " }";
                    System.out.println(query);
                    KB.get().update(query);*/
                    
                    //retrieve all links of the dataset to model its distributions
                    JSONArray links = collection.getJSONArray("links");
                    //for each links
                    for (int k = 0; k < links.length(); k++) {
                        //retrieve the current link object
                        JSONObject link = (JSONObject) links.get(k);
                        String rel=link.getString("rel");
                        if(rel.equals("item")){
                            //create a distribution
                            UUID distUri = UUID.randomUUID();
                            c=ont.createClass("http://www.w3.org/ns/dcat#Distribution");
                            c.createIndividual(gtdcat+distUri);
                            /*query = "INSERT DATA { gtdcat:" + distUri + " rdf:type <http://www.w3.org/ns/dcat#Distribution> . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                            
                            //associate the distribution to the current dataset
                            op=ont.createObjectProperty("http://www.w3.org/ns/dcat#distribution");
                            ont.add(ont.getResource(gtdcat + dsUri),op,ont.getResource(gtdcat+distUri));
                            /*query = "INSERT DATA { gtdcat:" + dsUri + " <http://www.w3.org/ns/dcat#distribution> gtdcat:"+distUri+" . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                            //create a dataservice 
                            UUID dservUri = UUID.randomUUID();
                            c=ont.createClass("http://www.w3.org/ns/dcat#DataService");
                            c.createIndividual(gtdcat+dservUri);
                            /*query = "INSERT DATA { gtdcat:" + dservUri + " rdf:type <http://www.w3.org/ns/dcat#DataService> . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/

                            //create the href link as a resource
                            String href=link.getString("href");
                            ont.createResource(href);
                            /*query = "INSERT DATA { <" + href + "> rdf:type rdfs:Resource . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                            
                            // associate it the url to access it
                            op=ont.createObjectProperty("http://www.w3.org/ns/dcat#endpointURL");
                            ont.add(ont.getResource(gtdcat + dservUri),op,ont.getResource(href));
                            /*query = "INSERT DATA { gtdcat:" + dservUri + " <http://www.w3.org/ns/dcat#endpointURL> <"+href+"> . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                            
                            //associate the dataservice to the distribution
                            op=ont.createObjectProperty("http://www.w3.org/ns/dcat#accessService");
                            ont.add(ont.getResource(gtdcat + distUri),op,ont.getResource(gtdcat+dservUri));
                            /*query = "INSERT DATA { gtdcat:" + distUri + " <http://www.w3.org/ns/dcat#accessService> gtdcat:"+dservUri+" . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                            
                            //add the title to the distribution
                            String title=link.getString("title");
                            ont.add(ont.getResource(gtdcat+distUri),dp,title);
                            /*query = "INSERT DATA { gtdcat:" + distUri + " <http://purl.org/dc/elements/1.1/title> \"" + title + "\" . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                            
                            //add the type to the distribution
                            /*String type=link.getString("type");
                            query = "INSERT DATA { gtdcat:" + distUri + " <http://www.w3.org/ns/dcat#mediaType> gtdcat:" + type + " . "
                                    + " }";
                            System.out.println(query);
                            KB.get().update(query);*/
                        }
                    }
                    KB.get().getOnt().add(ont);
                    KB.get().save();
                    foundc = true;
                }
                j++;
            }

        } catch (IOException | InterruptedException | OntoManagementException ex) {
            Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/home";
    }
    /*
    @GetMapping("/data/uplift")
    public String getUpliftView( Model model) {
       GMLImporter importer=new GMLImporter();
        System.out.println("data uplift");
       importer.processFile("", "gn-lu2.gml", false, false, "gn-lu-onto.ttl", "", "", "");
       System.out.println("data uplift done");
       return null;
    }
     */

}
