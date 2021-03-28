package info.ponciano.lab.geotimewfs.array_uplift;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntProperty;
import org.junit.jupiter.api.Test;

import info.ponciano.lab.pisemantic.PiOnt;
import info.ponciano.lab.pitools.files.PiFile;

class ArrayUpliftTest {

	ArrayUpliftModelImp instance;

	public ArrayUpliftTest() {
		super();
		PiFile pf = new PiFile("testdata/vg250gem.csv");
		String[][] attribute;
		try {
			attribute = pf.readCSV(";");
			this.instance = new ArrayUpliftModelImp(attribute, "testdata/ontoCSVtest.ttl", "testdata/vocabtest.owl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Exception from ArrayUpliftModelImp constructor: " + e.getMessage());
			;
		}
	}

	@Test
	final void initpropertyNamesTest() {
		String expectedProp = "http://lab.ponciano.info/ontology/2020/geotime/data#hasArea";
		assertEquals(expectedProp, instance.propertyNames.get("AREA,N,20,5"));

		String expectedProp2 = "http://lab.ponciano.info/ontology/2020/geotime/data#hasPerimeter";
		assertEquals(expectedProp2, instance.propertyNames.get("PERIMETER,N,20,5"));

	}

	@Test
	final void initpropertyRangesTest() {
		String expectedRange = "http://www.w3.org/2001/XMLSchema#double";
		assertEquals(expectedRange,
				instance.propertyRanges.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasArea"));

		String expectedRange2 = "http://www.w3.org/2001/XMLSchema#string";
		assertEquals(expectedRange2,
				instance.propertyRanges.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasDescription"));
		
		String expectedRange3 = "http://lab.ponciano.info/ontology/2020/geotime/data#Municipality";
		assertEquals(expectedRange3,
				instance.propertyRanges.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasMunicipality"));

	}

	@Test
	final void getPropertiesTest() {
		int expectedSize = instance.getProperties().size();
		assertEquals(expectedSize, 10);
	}

	@Test
	final void getObjectPropertiesTest() {
		if (instance.getObjectProperties() != null) {
			int expectedSize = instance.getObjectProperties().size();
			assertEquals(expectedSize, 1);
		} else {
			fail("Not yet implemented");
		}
	}

	@Test
	final void getDataPropertiesTest() {
		if (instance.getDataProperties() != null) {
			int expectedSize = instance.getDataProperties().size();
			assertEquals(expectedSize, 9);
		} else {
			fail("Not yet implemented");
		}
	}

	@Test
	final void geFirstRowsTest() {
		List<String[]> expectedlist = new ArrayList<String[]>();
		String[] row1 = new String[10];
		row1[0] = "AREA,N,20,5";
		row1[1] = "PERIMETER,N,20,5";
		row1[2] = "GEM_,N,11,0";
		row1[3] = "GEM_ID,N,11,0";
		row1[4] = "SHN,C,12";
		row1[5] = "RAU,C,12";
		row1[6] = "USE,N,2,0";
		row1[7] = "KEY,C,8";
		row1[8] = "GEN,C,50";
		row1[9] = "DES,C,50";
		expectedlist.add(row1);

		assertEquals(expectedlist.size(), instance.geFirstRows(1).size());
		assertEquals(expectedlist.get(0)[1], instance.geFirstRows(1).get(0)[1]);
		assertEquals(expectedlist.get(0)[5], instance.geFirstRows(1).get(0)[5]);
		assertEquals(expectedlist.get(0)[9], instance.geFirstRows(1).get(0)[9]);
	}

	@Test
	final void addPropertyTest() {
		instance.addProperty("hasWheelchairAccess", "xsd:boolean");
		String expectedRange1 = "http://www.w3.org/2001/XMLSchema#boolean";
		assertEquals(expectedRange1,
				instance.propertyRanges.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasWheelchairAccess"));
		
		instance.addProperty("hasCity", "City");
		String expectedRange2 = "http://lab.ponciano.info/ontology/2020/geotime/data#City";
		assertEquals(expectedRange2,
				instance.propertyRanges.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasCity"));
	}

	@Test
	final void addPropertyMappingTest() {
		instance.addProperty("hasWheelchairAccess", "xsd:boolean");
		boolean b=instance.addPropertyMapping("hasWheelchairAccess", "wheelchair");
		assertTrue(b);
		
		OntProperty p= instance.vocab.getOntProperty("http://lab.ponciano.info/ontology/2020/geotime/data#hasWheelchairAccess");
		String exp="wheelchair";
		assertEquals(exp,p.getLabel(null));

		String expectedProperty = "http://lab.ponciano.info/ontology/2020/geotime/data#hasWheelchairAccess";
		assertEquals(expectedProperty,instance.propertyNames.get("wheelchair"));
	}
	
	final void createOntologyTest() {
		// TODO Auto-generated method stub
		
	}

}
