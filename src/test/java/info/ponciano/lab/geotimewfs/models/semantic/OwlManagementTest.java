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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

/**
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
public class OwlManagementTest {
    
    public OwlManagementTest() {
    }

    /**
     * Test of uplift method, of class OwlManagement.
     */
    @Test
    public void testUplift() {
        System.out.println("uplift");
        String xmlPathfile = "src/main/resources/metadata/metadata.xml";
        OwlManagement instance = new OwlManagement();
        boolean expResult = false;
        boolean result = instance.uplift(xmlPathfile);
        assertEquals(expResult, result);
    }

    /**
     * Test of change method, of class OwlManagement.
     */
    @Test
    public void testChange() {
        System.out.println("change");
        String[] param = null;
        OwlManagement instance = new OwlManagement();
        boolean expResult = false;
        boolean result = instance.change(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSPARQL method, of class OwlManagement.
     */
    @Test
    public void testGetSPARQL() {
        System.out.println("getSPARQL");
        String[] param = null;
        OwlManagement instance = new OwlManagement();
        String expResult = "";
        String result = instance.getSPARQL(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
