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
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntProperty;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Dr Jean-Jacques Ponciano Contact: jean-jacques@ponciano.info
 */
public class OwlManagement extends OntoManagement {

    /**
     * Loading information from an XML file into an ontology.
     *
     * @param xmlPathfile path of the XML file
     * @return true if the uplift works, false otherwise.
     */
    @Override
    public boolean uplift(String xmlPathfile) {
        try {
            File file = new File(xmlPathfile);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            System.out.println("Root element: " + document.getDocumentElement().getNodeName());
            if (document.hasChildNodes()) {
                NodeList childNodes = document.getChildNodes();
                writeNodeList(childNodes.item(0).getChildNodes(), null, null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void writeNodeList(NodeList nodeList, Individual indiv, OntProperty parentProperty) throws OntoManagementException {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = NS + getNodeName(elemNode);
                //the node represents a property or an individual .
                //if the node represents a class.
                if (this.ont.getOntClass(nodeName) != null) {
                    Individual n = this.getIndividual(elemNode, nodeName);
                    if (parentProperty != null) {
                        indiv.addProperty(parentProperty, n);
                    }
                    if (elemNode.hasChildNodes()) {
                        //recursive call if the node has child nodes  
                        writeNodeList(elemNode.getChildNodes(), n, null);
                    }
                } else { //if the node represents a property.
                    if (!elemNode.hasChildNodes()) {
                        String textContent = elemNode.getTextContent();
                        indiv.addLiteral(parentProperty, textContent);
                    } else {
                        OntProperty ontProperty = this.ont.getOntProperty(nodeName);
                        writeNodeList(elemNode.getChildNodes(), indiv, ontProperty);
                    }
                }
            }
        }
    }

    public void saveOntology(String path) throws IOException {
        this.ont.write(new FileWriter(path));
    }

    private Individual getIndividual(Node elemNode, String nodeName) throws DOMException {
        Individual n = null;
        boolean notCreate = true;
        if (elemNode.hasAttributes()) {
            NamedNodeMap nodeMap = elemNode.getAttributes();
            int i = 0;
            while (notCreate && i < nodeMap.getLength()) {
                Node node = nodeMap.item(i);
                String attrName1 = node.getNodeName();
                String attrValue = node.getNodeValue();
                if (attrName1.equals("codeListValue") || attrName1.equals("codeListElementValue")) {
                    n = this.ont.getIndividual(NS + attrValue);
                    notCreate = false;
                } else if (attrName1.toLowerCase().equals("uuid")) {
                    n = this.ont.createIndividual(NS + attrValue, this.ont.getResource(nodeName));
                    notCreate = false;
                }
                i++;
            }
        }
        if (notCreate) {
            n = this.ont.createIndividual(this.ont.getResource(nodeName));
        }
        return n;
    }

    public static String getNodeName(Node elemNode) {
        return elemNode.getNodeName().split(":")[1];
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
