package info.ponciano.lab.geotimewfs.models;

import java.util.List;
import java.util.UUID;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import info.ponciano.lab.geotimewfs.models.semantic.OntoManagement;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;

/**
*
* @author Claire Prudhomme
*/
public class SHPdata {
	
	 private String metadata;
	 private String title;
	 private String version;
	 private String versionNote;
	 private String prevAsset;
	 
	 public SHPdata(){
			
	 }
	 
	 public SHPdata(String version, String versionNote) {
		this.version = version;
		this.versionNote = versionNote;
		this.prevAsset = null;
	}



	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionNote() {
		return versionNote;
	}

	public void setVersionNote(String versionNote) {
		this.versionNote = versionNote;
	}

	public String getPrevAsset() {
		return prevAsset;
	}

	public void setPrevAsset(String prevAsset) {
		this.prevAsset = prevAsset;
	}

	public OntModel representationRDF(String rdfdata, String shpdata) throws OntoManagementException {
		 
		 OntModel ont= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	     String gtdcat="http://lab.ponciano.info/ontology/2020/geotime/dcat#";
	     //retrieve the selected metadata uri
	    	 Metadata metadata = new Metadata();
	         List<String[]> info = metadata.getMetadata();
	         int i = 0;
	         boolean found = false;
	         String mduri="";
	         while (i < info.size() && !found) {
	             if (info.get(i)[2].equals(this.metadata)) {
	                 mduri = info.get(i)[0];
	                 //create the metadata individual with catalog record type
	                 OntClass c=ont.createClass("http://www.w3.org/ns/dcat#CatalogRecord");
	                 c.createIndividual(OntoManagement.NS + mduri);
	                 
	                 //add the GDI catalog
	                 c=ont.createClass("http://www.w3.org/ns/dcat#Catalog");
	                 c.createIndividual("http://lab.ponciano.info/ontology/2020/geotime/dcat#gdi_catalog");
	                 
	                 //add the metadata as a record of the catalog
	                 ObjectProperty p=ont.createObjectProperty("http://www.w3.org/ns/dcat#record");
	                 ont.add(ont.getResource(gtdcat+"gdi_catalog"),p,ont.getResource(OntoManagement.NS + mduri));
	                 found = true;
	             }
	             i++;
	         }

	                 //create a new dataset version = an Asset
	                 UUID dsUri = UUID.randomUUID();
	                 //OntClass c=ont.createClass("http://www.w3.org/ns/dcat#Dataset");
	                 OntClass c=ont.createClass("http://www.w3.org/ns/adms#Asset");
	                 c.createIndividual(gtdcat+dsUri);
	                 
	                 //add the title of the dataset 
	                 DatatypeProperty dp=ont.createDatatypeProperty("http://purl.org/dc/elements/1.1/title");
	                 ont.add(ont.getResource(gtdcat+dsUri),dp,this.title);
	                 
	                 //if the data is an update (that means it has a previous asset), 
	                 //the version is computed according to its previous version number
	                 if(this.prevAsset!=null) this.computeVersion();
	                 //add the version of the dataset through owl:versionInfo
	                 dp=ont.createDatatypeProperty("http://www.w3.org/2002/07/owl#versionInfo");
	                 ont.add(ont.getResource(gtdcat+dsUri),dp, this.version);

	                 //add the version note of the dataset through adms:versionNotes
	                 dp=ont.createDatatypeProperty("http://www.w3.org/ns/adms#versionNotes");
	                 ont.add(ont.getResource(gtdcat+dsUri),dp,this.versionNote);
	                 
	                 //add the default status of the dataset, which is unverified
	                 String statusURI= "http://lab.ponciano.info/ontology/2020/geotime/data-status#unverified";
	                 c=ont.createClass("http://www.w3.org/2004/02/skos/core#Concept");
	                 c.createIndividual(statusURI);
	                 ObjectProperty op=ont.createObjectProperty("http://www.w3.org/ns/adms#status");
	                 ont.add(ont.getResource(gtdcat+dsUri),op,ont.getResource(statusURI));
	                 
	                 //if the data is an update (that means it has a previous asset), 
	                 //update for all its previous versions the adms:last with the new created asset, and
	                 //add the links adms:next and adms:prev between it and its previous asset
	                 if(this.prevAsset!=null) {
	                	 //TODO version links
	                 }
	                 
	                 //link dataset to catalog through dcat:dataset
	                 op=ont.createObjectProperty("http://www.w3.org/ns/dcat#dataset");
	                 ont.add(ont.getResource(gtdcat+"gdi_catalog"),op,ont.getResource(gtdcat+dsUri));
	                 
	                 //link dataset to metadata record through foaf:primaryTopic
	                 op=ont.createObjectProperty("http://xmlns.com/foaf/0.1/primaryTopic");
	                 ont.add(ont.getResource(OntoManagement.NS + mduri),op,ont.getResource(gtdcat+dsUri));

	                 //create the RDF distribution
	                         UUID distRdfUri = UUID.randomUUID();
	                         c=ont.createClass("http://www.w3.org/ns/dcat#Distribution");
	                         c.createIndividual(gtdcat+distRdfUri);
	                 //create the SHP distribution
	                         UUID distShpUri = UUID.randomUUID();
	                         c.createIndividual(gtdcat+distShpUri);
	                         
	                 //associate the distributions to the newly created dataset
	                         op=ont.createObjectProperty("http://www.w3.org/ns/dcat#distribution");
	                         ont.add(ont.getResource(gtdcat + dsUri),op,ont.getResource(gtdcat+distRdfUri));
	                         ont.add(ont.getResource(gtdcat + dsUri),op,ont.getResource(gtdcat+distShpUri));
	                        
	                 //create a dataservice 
	                         UUID dservUriRDF = UUID.randomUUID();
	                         c=ont.createClass("http://www.w3.org/ns/dcat#DataService");
	                         c.createIndividual(gtdcat+dservUriRDF);
	                         UUID dservUriSHP = UUID.randomUUID();
	                         c.createIndividual(gtdcat+dservUriSHP);

	                 //create the local access as a resource
	                         String hrefRDF="https://local"+rdfdata;
	                         ont.createResource(hrefRDF);
	                         String hrefSHP="https://local/shapefile-data/"+shpdata;
	                         ont.createResource(hrefSHP);
	                         
	                 // associate it the url to access it
	                         op=ont.createObjectProperty("http://www.w3.org/ns/dcat#endpointURL");
	                         ont.add(ont.getResource(gtdcat + dservUriRDF),op,ont.getResource(hrefRDF));
	                         ont.add(ont.getResource(gtdcat + dservUriSHP),op,ont.getResource(hrefSHP));
	                         
	                 //associate the dataservice to the distribution
	                         op=ont.createObjectProperty("http://www.w3.org/ns/dcat#accessService");
	                         ont.add(ont.getResource(gtdcat + distRdfUri),op,ont.getResource(gtdcat+dservUriRDF));
	                         ont.add(ont.getResource(gtdcat + distShpUri),op,ont.getResource(gtdcat+dservUriSHP));
	                         
	                 //add the title to the distribution
	                         String ttle=this.title+ " (RDF data)";
	                         ont.add(ont.getResource(gtdcat+distRdfUri),dp,ttle);
	                         ttle=this.title+ " (Shapefile)";
	                         ont.add(ont.getResource(gtdcat+dservUriSHP),dp,ttle);
	              return ont;
	 }
	
	 /**
	  * Compute the version number of the current data according to the version number of its previous version 
	  */
	 private void computeVersion() {
		 //TODO computeVersion
	 }
     
	 public String ShpUpliftProcess(String path) {
			// TODO 1.0 @JJPONCIANO
			// 1- execution of script to transform Shapefile into RDF file (NB: storage of
			// mapping file into the directory "r2rml-mapping" for now, later on git)
			// 2- save the resulting RDF file into a directory "rdf-data" and return the RDF
			// file path (NB: empty string "", if no file)
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}
}
