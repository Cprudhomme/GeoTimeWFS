/**
 * 
 */
package info.ponciano.lab.geotimewfs.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;

/**
 * @author Claire Prudhomme
 *
 */
public class SpatioTemporalCModel {

	public SpatioTemporalCModel(String asset, String version) {
		// TODO Auto-generated constructor stub
	} // TODO @Claire

	public List<String> getVersions(String asset) throws OntoManagementException {
		//Retrieve the metadata associated to the asset provided as parameter
		//Retrieve all assets associated to the metadata and their associated version information
		List<String[]> info = new ArrayList<String[]>();
		//crete the query that retrieve all versions associated to the same metadata of the asset given as parameter
		String query = "SELECT ?v "
				+ "WHERE{ ?m <http://xmlns.com/foaf/0.1/primaryTopic> <http://lab.ponciano.info/ontology/2020/geotime/dcat#"+asset+">. "
				+"?m <http://xmlns.com/foaf/0.1/primaryTopic> ?a. "
				+ "?a <http://www.w3.org/2002/07/owl#versionInfo> ?v. " + "}";
		System.out.println(query);
		System.out.println(KB.get().getSPARQL(query));
		// create the table with the variable to retrieve
		String[] var = { "v" };
		// query the ontology
		info = KB.get().queryAsArray(query, var, true, false);
		List<String> versions=orderVersion(info);
		return versions;
	}
	
	private List<String> orderVersion(List<String[]> info) {
		HashMap <Double, String> versionsmap=new HashMap<Double, String>();
		for(int i=0; i< info.size();i++)
		{
			String v=info.get(i)[0];
			Double d=Double.parseDouble(v.substring(1));
			System.out.println(d);
			versionsmap.put(d, v);
		}
		//adding of keys into a list to sort the double values
		List<Double> keys= new ArrayList<Double>();
		keys.addAll(versionsmap.keySet());
		Collections.sort(keys);
		return null;
	}

	
	public String getAsset(String asset, String version) throws OntoManagementException {
		List<String[]> info = new ArrayList<String[]>();
		//create the query that retrieve an asset having the same metadata that those given as parameter and that have the version given as parameter
		String query = "SELECT ?a "
				+ "WHERE{ ?m <http://xmlns.com/foaf/0.1/primaryTopic> <http://lab.ponciano.info/ontology/2020/geotime/dcat#"+asset+">. "
				+"?m <http://xmlns.com/foaf/0.1/primaryTopic> ?a. "
				+ "?a <http://www.w3.org/2002/07/owl#versionInfo> \""+version+"\". " + "}";
		System.out.println(query);
		System.out.println(KB.get().getSPARQL(query));
		// create the table with the variable to retrieve
		String[] var = { "a" };
		// query the ontology
		info = KB.get().queryAsArray(query, var, true, false);
		//TODO @Claire secure the string retrieving with exception in case of error
		String res=info.get(0)[0];
		return res;
	}

	public Object getGeoData() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String[]> getPropertiesOP() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String[]> getPropertiesDP() {
		// TODO Auto-generated method stub
		return null;
	}

}
