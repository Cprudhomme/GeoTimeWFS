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
package info.ponciano.lab.geotimewfs.models;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 *
 * @author claireprudhomme
 */
public class SemanticWFSRequest {

    private String url;
    public SemanticWFSRequest() {
        url="https://ld.gdi-de.org/semanticwfs/";
    }
    
    private String getConformance(String type) throws IOException, InterruptedException{
        String res="";
        String url2=url+"conformance?f=";
        try {
           HttpRequest request = HttpRequest.newBuilder(new URI(url2+type)). GET().build();
           HttpResponse<String> response;
           response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
           res=response.body();
           //System.out.println(res);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SemanticWFSRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public String getJSONConformance()throws IOException, InterruptedException{
        return getConformance("json");
    }
    public String getXMLConformance() throws IOException, InterruptedException{
        return getConformance("gml");
    }
    public String getHTMLConformance() throws IOException, InterruptedException{
        return getConformance("html");
    }
    
    
    private String getCollections(String type) throws IOException, InterruptedException{
        String res="";
        String url2=url+"collections?f=";
        try {
           HttpRequest request = HttpRequest.newBuilder(new URI(url2+type)). GET().build();
           HttpResponse<String> response;
           response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
           res=response.body();
           //System.out.println(res);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SemanticWFSRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public String getJSONCollections()throws IOException, InterruptedException{
        return getCollections("json");
    }
    
}
