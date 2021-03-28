package info.ponciano.lab.geotimewfs.array_uplift;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.ponciano.lab.pisemantic.PiOnt;
import info.ponciano.lab.pitools.files.PiFile;

class ArrayUpliftTest {

	ArrayUpliftModelImp instance;
	
	
	public ArrayUpliftTest() {
		super();
		PiFile pf= new PiFile("testdata/vg250gem.csv");
		String[][] attribute;
		try {
			attribute = pf.readCSV(";");
			this.instance = new ArrayUpliftModelImp(attribute, "testdata/ontoCSVtest.ttl", "testdata/vocabtest.owl");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Exception from ArrayUpliftModelImp constructor: "+e.getMessage());;
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
		assertEquals(expectedRange, instance.propertyRanges.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasArea"));
		
		String expectedRange2 = "http://www.w3.org/2001/XMLSchema#string";
		assertEquals(expectedRange2, instance.propertyNames.get("http://lab.ponciano.info/ontology/2020/geotime/data#hasDescription"));
		
	}

	@Test
	final void getPropertiesTest() {
		int expectedSize=instance.getProperties().size();
		assertEquals(expectedSize,10);
	}

	@Test
	final void getObjectPropertiesTest() {
		int expectedSize=instance.getObjectProperties().size();
		assertEquals(expectedSize,1);
	}

	@Test
	final void getDataPropertiesTest() {
		int expectedSize=instance.getDataProperties().size();
		assertEquals(expectedSize,9);
	}

	@Test
	final void geFirstRowsTest(int nbrows) {
		List<String[]> expectedlist=new ArrayList<String[]>();
		String[] row1=new String[10];
		row1[0]="AREA,N,20,5";
		row1[1]="PERIMETER,N,20,5";
		row1[2]="GEM_,N,11,0";
		row1[3]="GEM_ID,N,11,0";
		row1[4]="SHN,C,12";
		row1[5]="RAU,C,12";
		row1[6]="USE,N,2,0";
		row1[7]="KEY,C,8";
		row1[8]="GEN,C,50";
		row1[9]="DES,C,50";
		expectedlist.add(row1);
		
		assertEquals(expectedlist,instance.geFirstRows(1));
	}

	final boolean addPropertyTest(String localname, String range) {
		// TODO Auto-generated method stub
		return false;
	}

	final boolean createOntologyTest() {
		// TODO Auto-generated method stub
		return false;
	}


}
