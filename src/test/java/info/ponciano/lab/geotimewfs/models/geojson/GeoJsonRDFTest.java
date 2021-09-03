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
import info.ponciano.lab.pitools.files.PiFile;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jean-jacquesponciano
 */
public class GeoJsonRDFTest {

    private String geoExample = "{\n"
            + "	\"type\" : \"FeatureCollection\",\n"
            + "	\"name\" : \"HS\",\n"
            + "	\"features\" : [\n"
            + "		{\n"
            + "			\"type\" : \"Feature\",\n"
            + "			\"geometry\" : {\n"
            + "				\"type\" : \"Point\",\n"
            + "				\"coordinates\" : [ 6.0779364934, 50.7776408005 ]\n"
            + "			},\n"
            + "			\"properties\" : {\n"
            + "				\"HS_Nr\" : \"1\",\n"
            + "				\"Name\" : \"Rheinisch-Westfälische Technische Hochschule Aachen\",\n"
            + "				\"Kurzname\" : \"Aachen TH\",\n"
            + "				\"Strasse\" : \"Templergraben\",\n"
            + "				\"Hn\" : \"55\",\n"
            + "				\"PLZ\" : \"52062\",\n"
            + "				\"Ort\" : \"Aachen\",\n"
            + "				\"Telefon\" : \"0241/80-1\",\n"
            + "				\"Telefax\" : \"0241/80-92312\",\n"
            + "				\"Homepage\" : \"www.rwth-aachen.de\",\n"
            + "				\"HS_Typ\" : \"Universitäten\",\n"
            + "				\"Traegersch\" : \"öffentlich-rechtlich\",\n"
            + "				\"Anzahl_Stu\" : 45945,\n"
            + "				\"Gruendungs\" : 1870,\n"
            + "				\"Promotion\" : \"Ja\",\n"
            + "				\"Habilitati\" : \"Ja\",\n"
            + "				\"PLZ_Postfa\" : \"52056\",\n"
            + "				\"Ort_Postfa\" : \"Aachen\",\n"
            + "				\"Mitglied_H\" : 1,\n"
            + "				\"Quelle\" : \"HRK\",\n"
            + "				\"RS\" : \"053340002002\",\n"
            + "				\"Bundesland\" : \"Nordrhein-Westfalen\",\n"
            + "				\"Regierungs\" : \"Köln\",\n"
            + "				\"Kreis\" : \"Städteregion Aachen\",\n"
            + "				\"Verwaltung\" : \"Aachen\",\n"
            + "				\"Gemeinde\" : \"Aachen\"\n"
            + "			}\n"
            + "		},{\n"
            + "			\"type\" : \"Feature\",\n"
            + "			\"geometry\" : {\n"
            + "				\"type\" : \"Point\",\n"
            + "				\"coordinates\" : [ 7.6435043046, 51.8989428278 ]\n"
            + "			},\n"
            + "			\"properties\" : {\n"
            + "				\"HS_Nr\" : \"X910\",\n"
            + "				\"Name\" : \"Deutsche Hochschule der Polizei, Münster (U)\",\n"
            + "				\"Kurzname\" : \"Münster U\",\n"
            + "				\"Strasse\" : \"Zum Roten Berge\",\n"
            + "				\"Hn\" : \"18-24\",\n"
            + "				\"PLZ\" : \"48165\",\n"
            + "				\"Ort\" : \"Münster\",\n"
            + "				\"HS_Typ\" : \"Universitäten\",\n"
            + "				\"Traegersch\" : \"öffentlich-rechtlich\",\n"
            + "				\"Anzahl_Stu\" : 421,\n"
            + "				\"Gruendungs\" : 0,\n"
            + "				\"Mitglied_H\" : 0,\n"
            + "				\"Quelle\" : \"StaBA\",\n"
            + "				\"RS\" : \"055150000000\",\n"
            + "				\"Bundesland\" : \"Nordrhein-Westfalen\",\n"
            + "				\"Regierungs\" : \"Münster\",\n"
            + "				\"Kreis\" : \"Münster\",\n"
            + "				\"Verwaltung\" : \"Münster\",\n"
            + "				\"Gemeinde\" : \"Münster\"\n"
            + "			}\n"
            + "		}\n"
            + "	]\n"
            + "}";

    public GeoJsonRDFTest() {
    }

    /**
     * Test of upliftGeoJSON method, of class GeoJsonRDF.
     */
    @Test
    public void testUpliftGeoJSON() throws Exception {
        System.out.println("upliftGeoJSON");
     String pathGeoJson = "geotest.json";
        new PiFile(pathGeoJson).writeTextFile(geoExample);
        PiOnt ont = new PiOnt("src/main/resources/ontologies/geosparql.owl");
        GeoJsonRDF.upliftGeoJSON(pathGeoJson, ont);
        List<Individual> individuals = ont.getIndividuals(ont.getOntClass(GeoJsonRDF.DCAT_DATASET));
        assertFalse(individuals.isEmpty());
        assertEquals(1, individuals.size());
    }

    /**
     * Test of downlift method, of class GeoJsonRDF.
     */
    @Test
    public void testDownlift() {
        System.out.println("downlift");
        String outputOut = "";
        PiOnt ont = null;
        GeoJsonRDF.downlift(outputOut, ont);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
