package info.ponciano.lab.geotimewfs.models.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

public abstract class OntoManagement {

    protected OntModel ont;
    public static final String NS = "http://lab.ponciano.info/ontology/2020/geotime/iso-19115#";

    public OntoManagement() {
        this.ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        this.ont.read("src/main/resources/ontologies/iso-19115.owl");
    }

    /**
     * Uplift Metadata from an XML file to the ontology
     *
     * @param xml the XML file path on the server.
     * @return true if the elevation succeeded, false otherwise.
     */
    public abstract boolean uplift(String xml);

    public abstract boolean change(String... param);

    public abstract String getSPARQL(String... param);

    /**
     * Check if the ontology is well formed.
     * @return True if the ontology is well formed, false otherwise.
     */
    public boolean checkOntology() {
        List<String> localname = new ArrayList<>();
        //get all resources of the ontology
        ExtendedIterator<OntProperty> listOntProperties = this.ont.listOntProperties();
        ExtendedIterator<Individual> listIndividuals = this.ont.listIndividuals();
        ExtendedIterator<OntClass> listClasses = this.ont.listClasses();
        boolean isOK = true;
        while (isOK && listClasses.hasNext()) {
            OntClass next = listClasses.next();
            isOK = !localname.contains(next.getLocalName());
            localname.add(next.getLocalName());
        }
        return isOK;
    }

    /**
     * Generate an URI with the default name space NS.
     *
     * @return the URI generated.
     */
    public static String generateURI() {
        return NS + UUID.randomUUID().toString();
    }

    /**
     * Transpose the name of a node in an ontology resource.
     *
     * @param nodeName name of the node to parse
     * @return returns the {@code OntResource} corresponding to the resource
     * that has the same local name as the {@code nodeName} or returns null if
     * the resource does not exist in the ontology.
     */
    public OntResource asOntResource(String nodeName) {
        if (nodeName.equals("RS_Identifier")) {
            System.out.println("here");
        }
        List<String> possibleNS = List.of(NS,
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19112#");
        for (String ns : possibleNS) {
            Resource resource = this.ont.getResource(ns + nodeName);
            if (this.ont.containsResource(resource)) {
                return this.ont.getOntResource(ns + nodeName);
            }
        }
        return null;
    }
}
