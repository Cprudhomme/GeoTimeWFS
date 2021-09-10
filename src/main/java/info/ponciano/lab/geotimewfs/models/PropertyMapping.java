package info.ponciano.lab.geotimewfs.models;

import java.util.ArrayList;
import java.util.List;

public class PropertyMapping {

	private String classname;
	//private String properties;
	//private List<String> listproperties;
	private List<String> properties;
	
	public PropertyMapping() {
		properties=new ArrayList<String>();
	}

	/*public List<String> getListProperties() {
		return listproperties;
	}
	public String getProperties() {
		return properties;
	}

	public void setProperties(String p) {
		this.properties=p;
		this.listproperties.add(p);
	}*/
	

	public String getClassname() {
		return classname;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	/*public void setProperties(int i, String p) {
		this.properties.add(i, p); 
	}*/
	
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	
}
