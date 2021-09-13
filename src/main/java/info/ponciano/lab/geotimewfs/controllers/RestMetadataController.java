/*
 * Copyright (C) 2021 Dr. Jean-Jacques Ponciano.
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
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class RestMetadataController {
    

    /* 
    return not yet defined 
     */
    @PostMapping("/metadata/downlift")
    public String postMdChangeAction(@RequestParam(name = "md", required = true) String md) {
        String rtn;
        try {
            rtn= KB.get().downlift("http://lab.ponciano.info/ontology/2020/geotime/iso-19115#"+md);
        } catch (OntoManagementException ex) {
            Logger.getLogger(RestMetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/errror?name=" + message;
        }
        return rtn;
    }

}