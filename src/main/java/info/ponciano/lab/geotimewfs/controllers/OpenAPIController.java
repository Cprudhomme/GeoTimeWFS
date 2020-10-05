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
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Dr Jean-Jacques Ponciano <jean-jacques@ponciano.info>
 */
@Controller

public class OpenAPIController {

    @GetMapping("/openapi")
    public String greeting(@RequestParam(name = "name",
            required = false,
            defaultValue = "name") String sd,
            Model model) {
        model.addAttribute("name", sd);
        try {
            System.out.println( onpenapiJSON2("/openapi.json"));
        } catch (ParseException ex) {
            Logger.getLogger(OpenAPIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OpenAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/semanticwfs/queryinterface";
    }
    
       public static String onpenapiJSON2(String openapi) throws ParseException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(openapi);
        CloseableHttpResponse response;
        response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        response.close();
        httpClient.close();
        return result;
    }
}
