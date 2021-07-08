package info.ponciano.lab.geotimewfs.models;

import java.util.List;

import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;

public interface FeatureCollectionManager {
	
	public List<String[]> getVersion() throws OntoManagementException;
}
