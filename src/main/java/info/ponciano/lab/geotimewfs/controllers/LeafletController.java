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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;

@Controller
public class LeafletController {

    private final StorageService storageService;
    
    @Autowired
    public LeafletController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    /**
     * 
     * @param model represents the thymeleaf model accessible through the view
     * @return the web interface of the result of a specific SPARQL request
     * @throws OntoManagementException 
     */
    
    // Link to Thematic Maps home
    @GetMapping("/thematicMaps")
    public String displayThematicMapsHome(Model model) throws OntoManagementException  {
        String rtn="thematicMaps";
        return rtn;
    }

    // Link to Creation of Thematic Maps
    @GetMapping("/mapCreation")
    public String displayMapCreation(Model model) throws OntoManagementException  {
        String rtn="mapCreation";
        return rtn;
    }

    // Link to list of Thematic Maps
    @GetMapping("/mapList")
    public String displayMapList(Model model) throws OntoManagementException  {
        String rtn="mapList";
        return rtn;
    }

    // Link to Thematic Map Example
    @GetMapping("/mapExample")
    public String displayMapExample(Model model) throws OntoManagementException  {
        String rtn="mapExample";
        return rtn;
    }

    // Data are not stored in the triple store yet
    // The following links lead to new maps
    
    @GetMapping("/mapCreation/schools")
    public String displaySchools(Model model) throws OntoManagementException  {
        String rtn="schools";
        return rtn;
    }

    @GetMapping("/mapCreation/universities")
    public String displayUniversities(Model model) throws OntoManagementException  {
        String rtn="universities";
        return rtn;
    }
    
    @GetMapping("/mapCreation/hospitals")
    public String displayHospitals(Model model) throws OntoManagementException  {
        String rtn="hospitals";
        return rtn;
    }

    @GetMapping("/mapCreation/townhalls")
    public String displayTownHalls(Model model) throws OntoManagementException  {
        String rtn="townhalls";
        return rtn;
    }

}