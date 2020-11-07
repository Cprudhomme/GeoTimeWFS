package info.ponciano.lab.geotimewfs.models.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * Parses the config file for the input formats
 * @author timo.homburg
 *
 */
public class ConfigParser {

	public Map<String,List<String>> maps=new TreeMap<>();
	
	public ConfigParser(String path) throws IOException {
		BufferedReader reader=new BufferedReader(new FileReader(new File(path)));
		String line;
		while((line=reader.readLine())!=null) {
			String[] linearr=line.split(",");
			List<String> linelist=Arrays.asList(linearr);
			linelist=linelist.subList(1, linelist.size());
			maps.put(linearr[0], linelist);
		}
		reader.close();
	}
	
	
}
