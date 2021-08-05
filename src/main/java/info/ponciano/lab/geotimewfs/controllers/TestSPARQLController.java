package info.ponciano.lab.geotimewfs.controllers;


import org.apache.jena.query.*;

import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import info.ponciano.lab.pisemantic.PiSparql;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import info.ponciano.lab.geotimewfs.models.JGit;
import info.ponciano.lab.geotimewfs.models.Schema;
import info.ponciano.lab.geotimewfs.models.SchemaValidation;
import info.ponciano.lab.geotimewfs.models.SparqlQuery;

@Controller
public class TestSPARQLController {
	
	private final StorageService storageService;
	
	@Autowired
	public TestSPARQLController(StorageService storageService) {
		this.storageService = storageService;
	}
	
	/**
	 * 
	 * @param model represents the thymeleaf model accessible through the view
	 * @return the web interface of the result of a specific SPARQL request
	 * @throws OntoManagementException 
	 */
	
	//the url we want to associate is "https://localhost:8080/test/SPARQL"
	@GetMapping("/test/SPARQL")
	public String getSPARQLRequest(Model model) throws OntoManagementException  {
		//the name of the view associated is "testSPARQL.html"
		String rtn="testSPARQL";
	
		//prefixes for SPARQL query
		String prefixes = "PREFIX schema: <http://schema.org/>"+
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				"PREFIX hist: <http://wikiba.se/history/ontology#>"+
				"PREFIX wd: <http://www.wikidata.org/entity/>"+
				"PREFIX wdt: <http://www.wikidata.org/prop/direct/>"+
				"PREFIX wikibase: <http://wikiba.se/ontology#>"+
				"PREFIX dct: <http://purl.org/dc/terms/>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX bd: <http://www.bigdata.com/rdf#>"+
				"PREFIX wds: <http://www.wikidata.org/entity/statement/>\r\n" + 
				"PREFIX wdv: <http://www.wikidata.org/value/>"+
				"PREFIX p: <http://www.wikidata.org/prop/>\r\n" + 
				"PREFIX ps: <http://www.wikidata.org/prop/statement/>\r\n" + 
				"PREFIX pq: <http://www.wikidata.org/prop/qualifier/>";
		//content of the query
		String queryContent = "SELECT ?city ?cityLabel ?population WHERE {\r\n" + 
				"  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\r\n" + 
				"  VALUES ?town_or_city {\r\n" + 
				"    wd:Q515\r\n" + 
				"  }\r\n" + 
				"  ?city (wdt:P31/(wdt:P279*)) ?town_or_city;\r\n" + 
				"    wdt:P17 wd:Q183.\r\n" + 
				"  OPTIONAL { ?city wdt:P1082 ?population. }\r\n" + 
				"}\r\n" + 
				"LIMIT 10";
		//build of the query with prefixes and the content of the query
		String queryString = prefixes + queryContent;
		//Create a query type with the String 'queryString'
		Query query = QueryFactory.create(queryString);
		//Print the query in console to ensure that it works
		System.out.println(queryString);
		//execute the query with the endpoint of Wikidata		
		QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
		//store results in ResultSet format
		ResultSet results = qexec.execSelect();
		//gives the column names of the query
		List<String> columnNames = results.getResultVars();
		System.out.println("Column Names : "+ columnNames);	
		
		//return the number of column of the results
		List<Integer> numberOfColumns = new ArrayList<Integer>();
		for (int i=0; i<columnNames.size(); i++) {
			numberOfColumns.add(i);
		}
		System.out.println(numberOfColumns);
		//the empty list that will contain all the results
		List<String[]> resultList = new ArrayList<String[]>();
		
		//for all the QuerySolution in the ResultSet file
		while (results.hasNext()) {
			//the QuerySolution is find on the ResultSet file
			QuerySolution solu = results.next();
			//empty array that have the size of the number of columns
			String[]ls=new String[columnNames.size()];
			//for all the column in the results
			for (int i=0; i<columnNames.size(); i++) {
				//the name of the column
				String columnName = columnNames.get(i);
				//the node is the result corresponding to the current column in the current QuerySolution
				RDFNode node = solu.get(columnName);
				String a = null;

                //test if the node is a resource type
                if(node.isResource()){
                	//a is equal to the Local Name (String)
                	a =node.asResource().getLocalName();
                }
            	//test if literal
                if(node.isLiteral()) {
                	//a is in String type
                    a = node.asLiteral().toString();
                } 
                //remove characters contains in the latitude and longitude fields
                if(a.contains("^^http://www.w3.org/2001/XMLSchema#decimal")) {
                    a = a.replace("^^http://www.w3.org/2001/XMLSchema#decimal", "");
                }
                //remove characters contains in the itemLabal field
                if(a.contains("@en")) {
                    a = a.replace("@en", "");
                }
                //add the node to the list
                ls[i]=a;
			}
			//force a String[] content and add into resultList
			Arrays.deepToString(ls);
			//add the list corresponding to a QuerySolution in the list of results
			resultList.add(ls);
			}
		
			//add into the model attribute
        	
			model.addAttribute("nc", numberOfColumns);
			model.addAttribute("cl", columnNames);
			model.addAttribute("MDlist", resultList);
			qexec.close();
			return rtn;

	}
	
	@GetMapping("/semantic_WFS/home")
	public String semanticWFSHome(Model model) {
		return "home2";
	}
	
	@GetMapping("/documentation")
	public String documentationMenu(Model model) {
		return "docu";
	}
	
	@GetMapping("/contact")
	public String contactMenu(Model model) {
		return "ctct";
	}
	
}