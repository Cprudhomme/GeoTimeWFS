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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
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
     */
    public static void upliftGeoJSON(String pathGeoJson, PiOnt ont) throws FileNotFoundException, IOException, ParseException {
        /*
        {
	"type" : "FeatureCollection",
	"name" : "HS",
	"features" : [
		{
			"type" : "Feature",
			"geometry" : {
				"type" : "Point",
				"coordinates" : [ 6.0779364934, 50.7776408005 ]
			},
			"properties" : {
				"HS_Nr" : "1",
				"Name" : "Rheinisch-Westfälische Technische Hochschule Aachen",
				"Kurzname" : "Aachen TH",
				"Strasse" : "Templergraben",
				"Hn" : "55",
				"PLZ" : "52062",
				"Ort" : "Aachen",
				"Telefon" : "0241/80-1",
				"Telefax" : "0241/80-92312",
				"Homepage" : "www.rwth-aachen.de",
				"HS_Typ" : "Universitäten",
				"Traegersch" : "öffentlich-rechtlich",
				"Anzahl_Stu" : 45945,
				"Gruendungs" : 1870,
				"Promotion" : "Ja",
				"Habilitati" : "Ja",
				"PLZ_Postfa" : "52056",
				"Ort_Postfa" : "Aachen",
				"Mitglied_H" : 1,
				"Quelle" : "HRK",
				"RS" : "053340002002",
				"Bundesland" : "Nordrhein-Westfalen",
				"Regierungs" : "Köln",
				"Kreis" : "Städteregion Aachen",
				"Verwaltung" : "Aachen",
				"Gemeinde" : "Aachen"
			}
		},
         */
        JSONParser parser = new JSONParser();//creates an instance  of  JSONParser object
        Object object = parser
                .parse(new FileReader(pathGeoJson));

        //convert Object to JSONObject
        JSONObject jsonObject = (JSONObject) object;

        //Reading the collection
        String nameCollection = (String) jsonObject.get("name");
        String typeCollection = (String) jsonObject.get("type");
        

        //if the type is "FeatureCollection"
        if (typeCollection.equals("FeatureCollection")) {

            JSONArray features = (JSONArray) jsonObject.get("features");

            for (Iterator it = features.iterator(); it.hasNext();) {
                JSONObject feature = (JSONObject) it.next();
                String type = (String) feature.get("type");
                JSONObject geometry = (JSONObject) feature.get("geometry");
                JSONObject properties = (JSONObject) feature.get("properties");

            }
        }
        //Reading the array

        //Printing all the values
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Countries:");
        for (Object country : countries) {
            System.out.println("\t" + country.toString());
        }

    }

    public static void downlift(String outputOut, PiOnt ont) {

    }
}
