package info.ponciano.lab.geotimewfs.models.importer;


import info.ponciano.lab.geotimewfs.models.parser.ConfigParser;
import java.io.IOException;
import java.util.List;
import java.util.Map;



public abstract class FormatImporter {

	public static Map<String, List<String>> formatToOntology;

	public static void readConfig() throws IOException {
		ConfigParser parser = new ConfigParser("config2.csv");
		formatToOntology = parser.maps;
	}
	
}
