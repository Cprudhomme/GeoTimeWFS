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
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.core.Prologue;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.util.iterator.ExtendedIterator;

public abstract class OntoManagement {

    protected OntModel ont;
    protected String prefix;

    public static final String NS = "http://lab.ponciano.info/ontology/2020/geotime/iso-19115#";
    private static final List<String> possibleNS = List.of(NS,
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19112#",
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19103#",
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19109#",
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19107#",
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19106#",
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19108#",
            "http://lab.ponciano.info/ontology/2020/geotime/iso-19111#"
    );

    /**
     * Creates an instance of OntoManagmenent and load the ontological model
     * given.
     *
     * @param ontologyPath OWL file containing the model to load.
     * @throws OntoManagementException if the model is wrong.
     */
    public OntoManagement(String ontologyPath) throws OntoManagementException {
        this.ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        this.ont.read(ontologyPath);
        String checkOntology = this.checkOntology();
        if (!checkOntology.isEmpty()) {
            throw new OntoManagementException("Ontology mal-formed:\n" + checkOntology);
        }
        prefix = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
        prefix += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";
        prefix += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n";
        prefix += "PREFIX dbr:    <http://dbpedia.org/resource/>\n";
        prefix += "PREFIX dbo:    <http://dbpedia.org/ontology/>\n";
        prefix += "PREFIX dct:    <http://purl.org/dc/terms/>\n";
        prefix += "PREFIX owl:    <http://www.w3.org/2002/07/owl#>\n";
        prefix += "PREFIX prov:   <http://www.w3.org/ns/prov#>\n";
        prefix += "PREFIX qb:     <http://purl.org/linked-data/cube#>\n";
        prefix += "PREFIX qudt:   <http://qudt.org/1.1/schema/qudt#>\n";
        prefix += "PREFIX schema: <http://schema.org/>\n";
        prefix += "PREFIX skos:   <http://www.w3.org/2004/02/skos/core#>\n";
        prefix += "PREFIX unit:   <http://qudt.org/vocab/unit#>\n";
        prefix += "PREFIX sdmx:   <http://purl.org/linked-data/sdmx#>\n";
    }

    /**
     * Creates an instance of OntoManagmenent and load the ontological model by
     * default.
     *
     * @throws OntoManagementException If the model is wrong
     */
    public OntoManagement() throws OntoManagementException {
        this.ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        this.ont.read("src/main/resources/ontologies/iso-19115.owl");
        String checkOntology = this.checkOntology();
        if (!checkOntology.isEmpty()) {
            throw new OntoManagementException("Ontology mal-formed:\n" + checkOntology);
        }
        prefix = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
        prefix += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";
        prefix += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n";
        prefix += "PREFIX dbr:    <http://dbpedia.org/resource/>\n";
        prefix += "PREFIX dbo:    <http://dbpedia.org/ontology/>\n";
        prefix += "PREFIX dct:    <http://purl.org/dc/terms/>\n";
        prefix += "PREFIX owl:    <http://www.w3.org/2002/07/owl#>\n";
        prefix += "PREFIX prov:   <http://www.w3.org/ns/prov#>\n";
        prefix += "PREFIX qb:     <http://purl.org/linked-data/cube#>\n";
        prefix += "PREFIX qudt:   <http://qudt.org/1.1/schema/qudt#>\n";
        prefix += "PREFIX schema: <http://schema.org/>\n";
        prefix += "PREFIX skos:   <http://www.w3.org/2004/02/skos/core#>\n";
        prefix += "PREFIX unit:   <http://qudt.org/vocab/unit#>\n";
        prefix += "PREFIX sdmx:   <http://purl.org/linked-data/sdmx#>\n";
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
    public abstract String downlift(String metadataURI) throws OntoManagementException;

    public abstract boolean change(String... param);

    /**
     * Executes SPARQL select function and format the results in an String
     *
     * @param query select query
     * @return query's results in String format.
     */
    public String getSPARQL(String query) {
        return ResultSetFormatter.asText(this.select(query), new Prologue(ont));

    }

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

    /**
     * Test if the given name space is know.
     *
     * @param nameSpace name space to test.
     * @return true if the name space is know, false otherwise.
     */
    public static boolean containsNS(String nameSpace) {
        return possibleNS.contains(nameSpace);
    }

    /**
     * Add prefix for every spaql query
     *
     * @param key kew use as prefix
     * @param namespace name space to be added.      <pre>
     * Example for  xsd:type \n"
     * addPrefix(xsd,http://www.w3.org/2001/XMLSchema#)
     * </pre>
     */
    public void addPrefix(String key, String namespace) {
        this.ont.setNsPrefix(key, namespace);
        prefix += "PREFIX " + key + ": <" + namespace + ">\n";
    }
    //**************************************************************************
    // ---------------------------- SPARQL -----------------------------------
    //**************************************************************************

    public ResultSet select(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }
        queryString = prefix + queryString;
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.create(query, this.ont);
        return queryExecution.execSelect();
    }

    /**
     * Execute a update query on the dataset
     *
     * @param query query to be executed
     * @throws info.ponciano.lab.pisemantic.PiOntologyException if something bad
     * happens
     */
    public void update(String query) throws OntoManagementException {
        if (query != null && !query.isEmpty()) {
            query = prefix + query;
            String res = removeGraph(query);
            UpdateAction.parseExecute(res, this.ont);
        }
    }

    private String removeGraph(String query) throws OntoManagementException {
        String res;
        if (query.contains("GRAPH")) {
            res = query.replaceAll("\n*\\s*GRAPH\\s<.*?>\\s*[{]", "");
            int lastIndexOf = res.lastIndexOf("}");
            res = res.substring(0, lastIndexOf);
        } else {
            res = query;
        }
        if (res.contains("GRAPH")) {
            throw new OntoManagementException("Querry with graph in owl file: " + res);
        }
        return res;
    }
}
