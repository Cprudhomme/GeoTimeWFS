package info.ponciano.lab.geotimewfs.array_uplift;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.util.iterator.ExtendedIterator;

import info.ponciano.lab.pisemantic.PiSparql;

public class ArrayUpliftModelImp extends ArrayUpliftModel {

	public ArrayUpliftModelImp(String[][] attributesArray, String ontpath) throws FileNotFoundException {
		super(attributesArray, ontpath);
	}

	public ArrayUpliftModelImp(String[][] attributesArray, String ontpath, String vocabpath)
			throws FileNotFoundException {
		super(attributesArray, ontpath, vocabpath);
	}

	@Override
	protected void initpropertyNames() {
		//init hashmap propertyNames
		this.propertyNames=new HashMap<String,String>();
		//retrieve all properties
		List<String> prop=this.getProperties();
		//for each property 
		for(int i=0; i<prop.size();i++) {
			//retrieve its label
			OntProperty p=this.vocab.getOntProperty(prop.get(i));
			ExtendedIterator<RDFNode> labels=p.listLabels(null);
			while(labels.hasNext()) {
				RDFNode l=labels.next();
				//add the label with the property to the hashmap
				this.propertyNames.put(l.asLiteral().toString(), prop.get(i));
			}
		}
		
	}

	@Override
	protected void initpropertyRanges() {
		// init hashmap propertyRanges
		this.propertyRanges = new HashMap<String, String>();
		// retrieve all properties
		List<String> prop=this.getProperties();
		//for each property 
		for(int i=0; i<prop.size();i++) {
			// for each property retrieve its range
			OntProperty p=this.vocab.getOntProperty(prop.get(i));
			OntResource range=p.getRange();
			// add the range with the property to the hashmap
			if(range!=null) {
				this.propertyRanges.put(prop.get(i), range.getURI());
				//System.out.println(prop.get(i));
				//System.out.println(range.getURI());
			}
		}
	}

	@Override
	public List<String> getProperties() {
		List<String> properties = this.getObjectProperties();
		List<String> dp = this.getDataProperties();
		if (dp != null)
			for (int i = 0; i < dp.size(); i++) {
				properties.add(dp.get(i));
			}
		return properties;
	}

	@Override
	public List<String> getObjectProperties() {
		ExtendedIterator<ObjectProperty> l=this.vocab.getOnt().listObjectProperties();
		List<String> oprop=new ArrayList<String>();
		while(l.hasNext()) {
			ObjectProperty op=l.next();
			oprop.add(op.getURI());
		}
		return oprop;
	}

	@Override
	public List<String> getDataProperties() {
		ExtendedIterator<DatatypeProperty> l=this.vocab.getOnt().listDatatypeProperties();
		List<String> dprop=new ArrayList<String>();
		while(l.hasNext()) {
			DatatypeProperty dp=l.next();
			dprop.add(dp.getURI());
		}
		return dprop;
	}

	@Override
	public List<String[]> geFirstRows(int nbrows) {
		List<String[]> firstrows = new ArrayList<String[]>();
		for (int i = 0; i < nbrows; i++) {
			firstrows.add(List.of(attributes).get(i));
		}
		return firstrows;
	}

	@Override
	public boolean addProperty(String localname, String range) {
		boolean datatypeproperty=range.contains("xsd:");
		//case of Datatypeproperty
		if(datatypeproperty) {
			//adding to vocab ontology
			DatatypeProperty dp=this.vocab.createDatatypeProperty("http://lab.ponciano.info/ontology/2020/geotime/data#"+localname);
			Resource r=new ResourceImpl("http://www.w3.org/2001/XMLSchema#"+range.substring(4));
			dp.addRange(r);
			//adding to propertyRanges
			this.propertyRanges.put(dp.getURI(), dp.getRange().getURI());
		}
		//case of ObjectProperties
		else {
			//adding to vocab ontology
			ObjectProperty op=this.vocab.createObjectProperty("http://lab.ponciano.info/ontology/2020/geotime/data#"+localname);
			OntClass c=this.vocab.createClass("http://lab.ponciano.info/ontology/2020/geotime/data#"+range);
			op.addRange(c);
			//adding to propertyRanges
			this.propertyRanges.put(op.getURI(), op.getRange().getURI());
		}
		return true;
	}

	@Override
	public boolean createOntology() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PiSparql getOntology() {
		return this.ontology;
	}

	@Override
	public boolean addPropertyMapping(String localname, String label) {
		OntProperty p=this.vocab.getOntProperty("http://lab.ponciano.info/ontology/2020/geotime/data#"+localname);
		if(p==null)
			return false;
		else {
			p.addLabel(label,null);
			this.propertyNames.put(label, p.getURI());
			return true;
		}
	}

}
