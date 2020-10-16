package info.ponciano.lab.geotimewfs.models.semantic;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public abstract class OntoManagement {
    
    protected OntModel ont;
    public static final String NS="http://lab.ponciano.info/ontology/2020/geotime/iso-19115#";
    public OntoManagement() {
        this.ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        this.ont.read("src/main/resources/ontologies/iso-19115.owl");
    }
    
    public abstract boolean uplift(String xml);
    
    public abstract boolean change(String... param);

    public abstract String getSPARQL(String ...param);
}