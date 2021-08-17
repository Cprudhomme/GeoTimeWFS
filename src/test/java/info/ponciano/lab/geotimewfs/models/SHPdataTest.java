/**
 * 
 */
package info.ponciano.lab.geotimewfs.models;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;


import info.ponciano.lab.pitools.files.PiFile;

/**
 * @author claireprudhomme
 *
 */
class SHPdataTest {

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#SHPdata()}.
	 */
	//@Test
	final void testSHPdata() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#SHPdata(java.lang.String, java.lang.String)}.
	 */
	//@Test
	final void testSHPdataStringString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#getMetadata()}.
	 */
	//@Test
	final void testGetMetadata() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#setMetadata(java.lang.String)}.
	 */
	//@Test
	final void testSetMetadata() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#getTitle()}.
	 */
	//@Test
	final void testGetTitle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#setTitle(java.lang.String)}.
	 */
	//@Test
	final void testSetTitle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#getVersion()}.
	 */
	//@Test
	final void testGetVersion() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#setVersion(java.lang.String)}.
	 */
	//@Test
	final void testSetVersion() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#getVersionNote()}.
	 */
	//@Test
	final void testGetVersionNote() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#setVersionNote(java.lang.String)}.
	 */
	//@Test
	final void testSetVersionNote() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#getPrevAsset()}.
	 */
	//@Test
	final void testGetPrevAsset() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#setPrevAsset(java.lang.String)}.
	 */
	//@Test
	final void testSetPrevAsset() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#representationRDF(java.lang.String, java.lang.String)}.
	 */
	//@Test
	final void testRepresentationRDF() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#shpUpliftProcess(java.lang.String, java.lang.String)}.
	 */
	// //@Test Passed but skipped due to the dependencies downloading that is not
	// compatible to CI in GitHub/lab.
	final void testShpUpliftProcess() {
		// SHPdata.main(null);
		String URI = "http://i3mainz.de/";
		String shapeFilePAth = "src/main/resources/datatest/vg250krs.shp";
		String[] results;
		try {
			results = SHPdata.shpUpliftProcess(shapeFilePAth, URI);
			if (results.length == 2 && results[0] != null && results[1] != null) {
				assertTrue(new PiFile(results[0]).exists());
				assertTrue(new PiFile(results[1]).exists());
			} else
				fail("Something wrong in shpUpliftProcess(" + shapeFilePAth + "," + URI + ")");
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link info.ponciano.lab.geotimewfs.models.SHPdata#getDataSet()}.
	 */
	//@Test
	final void testGetDataSet() {
		fail("Not yet implemented"); // TODO
	}

}
