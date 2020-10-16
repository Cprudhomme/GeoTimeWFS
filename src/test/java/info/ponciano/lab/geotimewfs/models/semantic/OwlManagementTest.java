/*
 * Copyright (C) 2020 Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info.
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
package info.ponciano.lab.geotimewfs.models.semantic;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.ontology.Individual;
import org.apache.jena.util.iterator.ExtendedIterator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Node;

@SpringBootTest

/**
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
public class OwlManagementTest {

    private String xmlPathfile = "src/main/resources/metadata/metadata.xml";
    private String metadataSavedowl = "metadataSaved.owl";

    ;

    public OwlManagementTest() {
    }

    /**
     * Test of uplift method, of class OwlManagement.
     */
    @Test
    public void testUplift() {
        try {
            System.out.println("uplift");
            OwlManagement instance = new OwlManagement();
            boolean expResult = true;
            boolean result = instance.uplift(xmlPathfile);
            assertEquals(expResult, result);
            List<Individual> listIndividuals = instance.listsMetadataIndividuals().toList();
            assertEquals(1, listIndividuals.size());

            instance.saveOntology(metadataSavedowl);
        } catch (IOException | OntoManagementException ex) {
            Logger.getLogger(OwlManagementTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Test of change method, of class OwlManagement.
     */
    @Test
    public void testChange() {
        System.out.println("change");
//        String[] param = null;
//        OwlManagement instance = new OwlManagement();
//        boolean expResult = false;
//        boolean result = instance.change(param);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        System.out.println("The test case is a prototype.");
    }

    /**
     * Test of getSPARQL method, of class OwlManagement.
     */
    @Test
    public void testGetSPARQL() {
        System.out.println("getSPARQL");
//        String[] param = null;
//        OwlManagement instance = new OwlManagement();
//        String expResult = "";
//        String result = instance.getSPARQL(param);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        System.out.println("The test case is a prototype.");
    }

    /**
     * Test of saveOntology method, of class OwlManagement.
     */
  @Test
    public void testSaveOntology() throws Exception {
        System.out.println("saveOntology");
    }

    /**
     * Test of getNodeName method, of class OwlManagement.
     */
    //@Test
    public void testGetNodeName() {
        System.out.println("getNodeName");
        Node elemNode = null;
        String expResult = "";
        String result = OwlManagement.getNodeName(elemNode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downlift method, of class OwlManagement.
     */
   @Test
    public void testDownlift() {
        try {
            System.out.println("downlift");
            OwlManagement instance = new OwlManagement();

            assertTrue(instance.uplift(xmlPathfile));
            instance.saveOntology(metadataSavedowl);

            String expResult = "TODO: Define the expected results.";
            //Lists the Metadata individuals
            List<Individual> listIndividuals = instance.listsMetadataIndividuals().toList();
            assertEquals(1, listIndividuals.size());
            String result = instance.downlift(listIndividuals.get(0).getURI());
            System.out.println(result);
            assertEquals(expResult, result);

        } catch (OntoManagementException | IOException ex) {
            Logger.getLogger(OwlManagementTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

}
