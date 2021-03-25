package info.ponciano.lab.array_uplift;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import info.ponciano.lab.pisemantic.PiOnt;
import info.ponciano.lab.pisemantic.PiSparql;

public abstract class ArrayUpliftModel {

	/**
	 * table representing a data to uplift as RDF triples
	 */
	protected String [][] attributes;
	/**
	 * path of the ontology to create from attributes
	 */
	protected String ontopath;
	//Fixed path of the vocabulary ontology
	protected static final String VOCAB_PATH="";
	//Ontology containing the vocabulary/TBox for the ontology representing a table data
	protected PiOnt vocab;
	//Ontology to create from attributes
	protected PiSparql ontology;
	//Hashmap having name in table as key and property URI as corresponding value
	protected HashMap<String, String> propertyNames;
	//Hashmap having property URI as key and its corresponding range as value
	protected HashMap<String, String> propertyRanges;
	
	
	public ArrayUpliftModel (String [][] attributesArray, String ontpath) throws FileNotFoundException {
		this.attributes=attributesArray;
		this.ontopath=ontpath;
		this.ontology= new PiSparql(this.ontopath);
		//can make a PiFile to verify if file exists, and create it if not.
		this.vocab= new PiOnt(VOCAB_PATH);
		this.initpropertyNames();
		this.initpropertyRanges();
	}

	protected abstract void initpropertyNames();
	protected abstract void initpropertyRanges();
	
	public abstract List<String> getProperties();
	public abstract List<String> getObjectProperties();
	public abstract List<String> getDataProperties();
	
	/**
	 * adding of a new Property from its local name, range and type that update hashmap and list of properties
	 * @param localname of the property to add
	 * @param range of the property to add, which allows also to determine if it is an object property or a data property according to if the range contains "xsd" at the beginning (to manage in web interface)
	 * @return validation of the property adding
	 */
	public abstract boolean addProperty(String localname, String range);
	
	
	public abstract boolean createOntology();
	/**
	 * 
	 * @return the created ontology from attributes
	 */
	public abstract PiSparql getOntology(); 
}
