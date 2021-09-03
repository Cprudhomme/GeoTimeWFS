/*
 * Copyright (C) 2021 jean-jacquesponciano.
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
package info.ponciano.lab.geotimewfs.models.geojson;

import info.ponciano.lab.pisemantic.PiOnt;
import info.ponciano.lab.pisemantic.PiOntologyException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Dr. Jean-Jacques Ponciano
 */
public class GeoJsonRDF {

    /**
     * Uplift a geoJSON file in an ontology
     *
     * @param pathGeoJson path of the file to read
     * @param ont ontology model in with the uplift should be done
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws org.json.simple.parser.ParseException if the file cannot be
     * parsed.
     * @throws info.ponciano.lab.pisemantic.PiOntologyException if something wrong or is not yet supported 
     */
    public static void upliftGeoJSON(String pathGeoJson, PiOnt ont) throws FileNotFoundException, IOException, ParseException, PiOntologyException {

        JSONParser parser = new JSONParser();//creates an instance  of  JSONParser object
        Object object = parser
                .parse(new FileReader(pathGeoJson));

        //convert Object to JSONObject
        JSONObject jsonObject = (JSONObject) object;

        //Reading the collection
        String nameCollection = (String) jsonObject.get("name");
        FeatureCollection featureCollection = new FeatureCollection(nameCollection);

        extractFeatures(jsonObject, featureCollection);

        // uplift
        List<Feature> allfeatures = featureCollection.getFeatures();
        String name = featureCollection.getName();
        //create an individual
        OntClass dataset = ont.getOntClass(DCAT_DATASET);
        if (dataset == null) {
            throw new PiOntologyException("the class \"http://www.w3.org/ns/dcat#Dataset\" does not exists but is requiered");
        }

        //create the individual data
        String nameFC = ont.getNs() + name;
        //generate a new name if the name is already known
        if (ont.getIndividual(nameFC) != null) {
            nameFC += "_" + UUID.randomUUID().toString();
        }
        Individual data = dataset.createIndividual(nameFC);

        for (Feature f : allfeatures) {

            //create geometry
            Geometry geometry = f.getGeometry();
            String type = geometry.getType();
            OntClass ontClassGeo = ont.getOntClass("http://www.opengis.net/ont/geosparql#" + type);
            if (ontClassGeo == null) {
                throw new PiOntologyException("the class \"http://www.opengis.net/ont/geosparql#" + type + "\" does not exists but is requiered");
            }
            Individual indGeo = ontClassGeo.createIndividual(ont.getNs() + "_" + UUID.randomUUID().toString());
            var asWKT = ont.getDataProperty(GEOSPARQLAS_WKT);
            if (asWKT == null) {
                throw new PiOntologyException("the property \"http://www.opengis.net/ont/geosparql#asWKT\" does not exists but is requiered");
            }
            String value = geometry.toString();
            indGeo.addLiteral(asWKT, value);

            //creates feature
            OntClass ontClassFeature = ont.getOntClass(GEOSPARQL_FEATURE);
            if (ontClassFeature == null) {
                throw new PiOntologyException("the class \"http://www.opengis.net/ont/geosparql#Feature\" does not exists but is requiered");
            }
            Individual indF = ontClassFeature.createIndividual(ont.getNs() + "_" + UUID.randomUUID().toString());

            //asign a geometry to the feature
            var hasGeometry = ont.getObjectProperty(GEOSPARQLHAS_GEOMETRY);
            if (hasGeometry == null) {
                throw new PiOntologyException("the property \"http://www.opengis.net/ont/geosparql#hasGeometry\" does not exists but is requiered");
            }
            indF.addProperty(hasGeometry, indGeo);

            //asigne properties
            Map<String, String> properties = f.getProperties();
            properties.forEach((k, v) -> {
                DatatypeProperty p = ont.createDatatypeProperty(k);
                indF.addLiteral(p, v);
            });
            //asign the feature to the datasets
            ObjectProperty hasFeature = ont.createObjectProperty("hasFeature");
            data.addProperty(hasFeature, indF);

        }

    }
    public static final String GEOSPARQLHAS_GEOMETRY = "http://www.opengis.net/ont/geosparql#hasGeometry";
    public static final String GEOSPARQLAS_WKT = "http://www.opengis.net/ont/geosparql#asWKT";
    public static final String GEOSPARQL_FEATURE = "http://www.opengis.net/ont/geosparql#Feature";
    public static final String DCAT_DATASET = "http://www.w3.org/ns/dcat#Dataset";

    private static void extractFeatures(JSONObject jsonObject, FeatureCollection featureCollection) throws NumberFormatException, PiOntologyException {
        String typeCollection = (String) jsonObject.get("type");
        //create the object

        //if the type is "FeatureCollection"
        if (typeCollection.equals("FeatureCollection")) {

            JSONArray features = (JSONArray) jsonObject.get("features");//get all feature

            for (Iterator it = features.iterator(); it.hasNext();) {//for each feature
                //extract information
                JSONObject feature = (JSONObject) it.next();
                String type = (String) feature.get("type");
                if (type.equals("Feature")) {
                    Geometry geo = extractGeo(feature);
                    //get the properties
                    JSONObject properties = (JSONObject) feature.get("properties");
                    final Feature f = new Feature(geo);
                    properties.keySet().forEach(k -> {
                        f.addProperty((String) k, (String) properties.get(k));
                    });
                    featureCollection.add(f);

                } else {
                    throw new PiOntologyException("parsing for " + type + " is not yet supported");
                }
            }

        } else {
            throw new PiOntologyException("parsing for " + typeCollection + " is not yet supported");
        }
    }

    private static Geometry extractGeo(JSONObject feature) throws NumberFormatException {
        //get the geometry
        JSONObject geometry = (JSONObject) feature.get("geometry");
        String geotype = (String) geometry.get("type");
        JSONArray coords = (JSONArray) geometry.get("coordinates");//get all coordinates
        double[] coordinates = new double[coords.size()];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = Double.parseDouble((String) coords.get(i));
        }
        Geometry geo = new Geometry(geotype, coordinates);
        return geo;
    }

    public static void downlift(String outputOut, PiOnt ont) {

    }
}
