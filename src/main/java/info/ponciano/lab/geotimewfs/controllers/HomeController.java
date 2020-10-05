/*
 * Copyright (C) 2020 Dr Jean-Jacques Ponciano <jean-jacques@ponciano.info>.
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

import de.hsmainz.cs.semgis.wfs.webservice.WebService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    /**
     * Example : http://localhost:8080/?servicetitle=BKG%20WFS%20Service&servicedescription=Test%20web%20services%20with%20a%20Triple%20Store%20backend
     * @param sd
     * @param st
     * @param model
     * @return 
     */
    @GetMapping("/")
    public String greeting(@RequestParam(name = "servicedescription",
            required = false,
            defaultValue = "C'est un beau service qui dit non non non on on o")
            String sd,
            @RequestParam(name = "servicetitle",
            required = false,
            defaultValue = "Un titre super !!!!")
            String st, Model model) {
        model.addAttribute("servicedescription", sd);
         model.addAttribute("servicetitle", "Landing page: "+st);
        return "home";
    }

}
