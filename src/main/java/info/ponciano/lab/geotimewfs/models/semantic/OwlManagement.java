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
import java.io.File;  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import org.w3c.dom.Document;  
import org.w3c.dom.NamedNodeMap;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;
/**
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
public class OwlManagement extends OntoManagement {

    @Override
    public boolean uplift(String xmlPathfile) {
        try {
            File file = new File(xmlPathfile);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            System.out.println("Root element: " + document.getDocumentElement().getNodeName());
            if (document.hasChildNodes()) {
                printNodeList(document.getChildNodes());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static void printNodeList(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
// get node name and value  
                System.out.println("\nNode Name =" + elemNode.getNodeName() + " [OPEN]");
                System.out.println("Node Content =" + elemNode.getTextContent());
                if (elemNode.hasAttributes()) {
                    NamedNodeMap nodeMap = elemNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                    }
                }
                if (elemNode.hasChildNodes()) {
//recursive call if the node has child nodes  
                    printNodeList(elemNode.getChildNodes());
                }
                System.out.println("Node Name =" + elemNode.getNodeName() + " [CLOSE]");
            }
        }
    }

    @Override
    public boolean change(String... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSPARQL(String... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
