package info.ponciano.lab.geotimewfs.models;

import java.util.ArrayList;
import java.util.List;

import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;

public abstract class LocalFCManager implements FeatureCollectionManager{
	
	
	
	public List<String[]> getVersion() throws OntoManagementException {
		List<String[]> info = new ArrayList<String[]>();

		// initialize the query to retrieve all instances of previous asset
		/*String query = "SELECT ?a "
				+ "WHERE{ ?a rdf:type <http://www.w3.org/ns/adms#Asset>. ?a <http://www.w3.org/ns/adms#last> <"
				+ this.prevAsset + ">. " + "}";
		System.out.println(query);
		System.out.println(KB.get().getSPARQL(query));
		// create the table of variables
		String[] var = { "a" };
		// query the ontology
		info = KB.get().queryAsArray(query, var, true, false);*/
		return info;
	}

}
