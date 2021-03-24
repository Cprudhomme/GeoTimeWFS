package info.ponciano.lab.array_uplift;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import info.ponciano.lab.pisemantic.PiOnt;

public abstract class ArrayUpliftModel {

	//table representing a data to uplift as RDF triples
	private String [][] table;
	//Fixed path of the vocabulary ontology
	private static final String VOCAB_PATH="";
	//Ontology containing the vocabulary/TBox for the ontology representing a table data
	private PiOnt vocab;
	//Hashmap having name in table as key and property URI as corresponding value
	private HashMap<String, String> propertyNames;
	//Hashmap having property URI as key and its corresponding range as value
	private HashMap<String, String> propertyRanges;
	
	public ArrayUpliftModel (String [][] attributes) throws FileNotFoundException {
		this.table=attributes;
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
	
	
}
