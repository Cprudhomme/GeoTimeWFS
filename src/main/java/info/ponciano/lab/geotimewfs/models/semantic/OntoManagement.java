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

    public OntoManagement() throws OntoManagementException {
        this.ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        this.ont.read("src/main/resources/ontologies/iso-19115.owl");
        String checkOntology = this.checkOntology();
        if (!checkOntology.isEmpty()) {
            throw new OntoManagementException("Ontology mal-formed:\n" + checkOntology);
        }
    }

    /**
     * Uplift Metadata from an XML file to the ontology
     *
     * @param xml the XML file path on the server.
     * @return true if the elevation succeeded, false otherwise.
     */
    public abstract boolean uplift(String xml);

    /**
     * Downlifts ontology metadata information as a string formatted in XML.
     *
     * @param metadataURI URI of the metadata in the ontology
     * @return the XML String in iso-119115 format.
     */
    public abstract String downlift(String metadataURI);

    public abstract boolean change(String... param);

    public abstract String getSPARQL(String... param);

    /**
     * Check if the ontology is well formed.
     *
     * @return Empty String if the ontology is well formed, mal-formed
     * information otherwise.
     */
    private String checkOntology() {
        List<String> localname = new ArrayList<>();
        //get all resources of the ontology
        ExtendedIterator<OntProperty> listOntProperties = this.ont.listOntProperties();
        ExtendedIterator<Individual> listIndividuals = this.ont.listIndividuals();
        ExtendedIterator<OntClass> listClasses = this.ont.listClasses();
        String error = "";
        while (listClasses.hasNext()) {
            OntClass next = listClasses.next();
            String localName = next.getLocalName();
            if (localname.contains(next.getLocalName()) && localName != null) {
                error += "\n" + next.getLocalName();
            } else {
                localname.add(next.getLocalName());
            }
        }
        while (listOntProperties.hasNext()) {
            OntProperty next = listOntProperties.next();
            String localName = next.getLocalName();
            if (localname.contains(localName) && localName != null) {
                error += "\n" + next.getLocalName();
            } else {
                localname.add(next.getLocalName());
            }
        }
        while (listIndividuals.hasNext()) {
            Individual next = listIndividuals.next();
            String localName = next.getLocalName();
            if (localname.contains(next.getLocalName()) && localName != null) {
                error += "\n" + next.getLocalName();
            } else {
                localname.add(next.getLocalName());
            }
        }
        return error;
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
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19112#",
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19103#",
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19109#",
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19107#",
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19106#",
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19108#",
                "http://lab.ponciano.info/ontology/2020/geotime/iso-19111#"
        );
        for (String ns : possibleNS) {
            Resource resource = this.ont.getResource(ns + nodeName);
            if (this.ont.containsResource(resource)) {
                return this.ont.getOntResource(ns + nodeName);
            }
        }
        return null;
    }

    /**
     * Gets this OntModel .
     *
     * @return this ontModel.
     */
    public OntModel getOnt() {
        return ont;
    }

    /**
     * Lists all metadata individuals.
     *
     * @return Extended Iterator of individual that are instances of MD_Metadata
     * class.
     */
    public ExtendedIterator<Individual> listsMetadataIndividuals() {
        //Lists the Metadata individuals
        return this.ont.listIndividuals(this.ont.getOntClass(OntoManagement.NS + "MD_Metadata"));
    }

}
