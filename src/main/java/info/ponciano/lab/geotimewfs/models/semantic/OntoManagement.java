package info.ponciano.lab.geotimewfs.models.semantic;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

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
    
    public  boolean checkOntology(){
        List<String> localname=new ArrayList<>();
        //get all resources of the ontology
        ExtendedIterator<OntProperty> listOntProperties = this.ont.listOntProperties();
        ExtendedIterator<Individual> listIndividuals = this.ont.listIndividuals();
        ExtendedIterator<OntClass> listClasses = this.ont.listClasses();
        boolean isOK=true;
        while (isOK&&listClasses.hasNext()) {
            OntClass next = listClasses.next();
            isOK = !localname.contains(next.getLocalName());
            localname.add(next.getLocalName()); 
        }
        return isOK;
    }
}