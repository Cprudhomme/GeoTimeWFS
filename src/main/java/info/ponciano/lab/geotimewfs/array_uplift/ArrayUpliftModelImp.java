package info.ponciano.lab.geotimewfs.array_uplift;

import java.io.FileNotFoundException;
import java.util.List;

import info.ponciano.lab.pisemantic.PiSparql;

public class ArrayUpliftModelImp extends ArrayUpliftModel{

	public ArrayUpliftModelImp(String [][] attributesArray, String ontpath) throws FileNotFoundException{
		super(attributesArray, ontpath);
	}
	
	public ArrayUpliftModelImp(String [][] attributesArray, String ontpath, String vocabpath) throws FileNotFoundException{
		super(attributesArray, ontpath, vocabpath);
	}

	@Override
	protected void initpropertyNames() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initpropertyRanges() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getObjectProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDataProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> geFirstRows(int nbrows) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addProperty(String localname, String range) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createOntology() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PiSparql getOntology() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
