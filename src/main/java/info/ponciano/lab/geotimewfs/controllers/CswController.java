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
package info.ponciano.lab.geotimewfs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
@RestController
public class CswController {

    @GetMapping("/csw")
    public String csw(@RequestParam(name = "request", required = true) String request) {
        String resultXML;
        switch (request) {
            case "GetCapabilities":
                resultXML = this.getCapabilities();
                break;
            case "DescribeRecord":
                resultXML = this.describeRecord();
                break;
            case "GetRecords":
                resultXML = this.getRecords();
                break;
            case "GetRecordById":
                resultXML = this.getRecordById();
                break;
            case "GetDomain":
                resultXML = this.getDomain();
                break;
            case "Harvest":
                resultXML = this.harvest();
                break;
            case "Transaction":
                resultXML = this.transaction();
                break;
                default:resultXML ="";
        }
        return resultXML;
    }

    /**
     * allows CSW clients to retrieve service metadata from a server
     *
     * @return
     */
    public String getCapabilities() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * allows a client to discover elements of the information model supported
     * by the target catalogue service. The operation allows some or all of the
     * information model to be described
     *
     * @return
     */
    public String describeRecord() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * search for records, returning record IDs
     *
     * @return
     */
    public String getRecords() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * retrieves the default representation of catalogue records using their
     * identifier
     *
     *
     * @return
     */
    public String getRecordById() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * used to obtain runtime information about the range of values of a
     * metadata record element or request parameter
     *
     *
     * @return
     */
    public String getDomain() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * create/update metadata by asking the server to 'pull' metadata from
     * somewhere
     *
     * @return
     */
    public String harvest() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * create/edit metadata by 'pushing' the metadata to the server
     *
     * @return
     */
    public String transaction() {
        return("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
