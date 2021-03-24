/**
 * 
 */
package info.ponciano.lab.geotimewfs.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;

/**
 * @author claireprudhomme
 *
 */
class FeatureCollectionTest {
/**
 * Text of the functuion initPaths
 * @throws OntoManagementException 
 */
	@Test 
	final void testInitPaths() throws OntoManagementException {
		System.out.println("testInitPaths");
		FeatureCollection instance= new FeatureCollection("2c04f566-ec08-4c6d-8beb-becc89a0418c");

		String expectedShp = "shp-data/vg250krs_2000.shp";
		assertEquals(expectedShp, instance.geopath);
		
		String expectedRDF = "rdf-data/776cc9ba-a5c2-46e3-abbd-427696bd4b36.ttl";
		assertEquals(expectedRDF, instance.rdfpath);
	}

}
