/*
 * Copyright (C)  2021 Dr Claire Prudhomme <claire@prudhomme.info).
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
package info.ponciano.lab.geotimewfs.controllers.examples.semanticwfs;

import info.ponciano.lab.geotimewfs.Conf;
import static info.ponciano.lab.geotimewfs.controllers.examples.semanticwfs.WebService2.onpenapiJSON2;
import info.ponciano.lab.geotimewfs.models.StringRest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dr Jean-Jacques Ponciano (Contact: jean-jacques@ponciano.info)
 */
@RestController
public class ResController {

    @GetMapping("/openapiJSON")
    public StringRest openapiJSON() {
        String result = "";
        try {
            result = onpenapiJSON2(Conf.get().getWfsconf().get("baseurl").toString());
        } catch (ParseException | IOException ex) {
            Logger.getLogger(ResController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(result);
        return new StringRest(result);
    }

}
