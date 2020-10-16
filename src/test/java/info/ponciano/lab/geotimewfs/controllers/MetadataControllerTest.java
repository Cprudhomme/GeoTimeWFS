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

import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author claireprudhomme
 */
public class MetadataControllerTest {
    
    public MetadataControllerTest() {
    }

    /**
     * Test of getUpliftView method, of class MetadataController.
     */
    @Test
    public void testGetUpliftView() {
        System.out.println("getUpliftView");
        String name = "";
        Model model = null;
        MetadataController instance = null;
        String expResult = "";
        String result = instance.getUpliftView(name, model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serveFile method, of class MetadataController.
     */
    @Test
    public void testServeFile() {
        System.out.println("serveFile");
        String filename = "";
        MetadataController instance = null;
        ResponseEntity<Resource> expResult = null;
        ResponseEntity<Resource> result = instance.serveFile(filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postUpliftAction method, of class MetadataController.
     */
    @Test
    public void testPostUpliftAction() {
        try {
            System.out.println("postUpliftAction");
            MultipartFile file = null;
            RedirectAttributes redirectAttributes = null;
            MetadataController instance = null;
            String expResult = "";
            String result = instance.postUpliftAction(file, redirectAttributes);
            assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
           
        } catch (IOException ex) {
            Logger.getLogger(MetadataControllerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Test of handleStorageFileNotFound method, of class MetadataController.
     */
    @Test
    public void testHandleStorageFileNotFound() {
        System.out.println("handleStorageFileNotFound");
        StorageFileNotFoundException exc = null;
        MetadataController instance = null;
        ResponseEntity expResult = null;
        ResponseEntity result = instance.handleStorageFileNotFound(exc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMetadata method, of class MetadataController.
     */
    @Test
    public void testGetMetadata() {
        System.out.println("getMetadata");
        String name = "";
        MetadataController instance = null;
        String expResult = "";
        String result = instance.getMetadata(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedMd method, of class MetadataController.
     */
    @Test
    public void testGetSelectedMd() {
        System.out.println("getSelectedMd");
        String name = "";
        MetadataController instance = null;
        String expResult = "";
        String result = instance.getSelectedMd(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMdChangeView method, of class MetadataController.
     */
    @Test
    public void testGetMdChangeView() {
        System.out.println("getMdChangeView");
        String name = "";
        MetadataController instance = null;
        String expResult = "";
        String result = instance.getMdChangeView(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
