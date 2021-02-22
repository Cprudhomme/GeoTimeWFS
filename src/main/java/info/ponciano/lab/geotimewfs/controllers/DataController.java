/*
 * Copyright (C) 2020 claireprudhomme.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.geotimewfs.controllers;

import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;

import info.ponciano.lab.geotimewfs.models.Metadata;
import info.ponciano.lab.geotimewfs.models.SHPdata;
import info.ponciano.lab.geotimewfs.models.SemanticWFSRequest;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagement;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author claireprudhomme
 */
@Controller
public class DataController {

	private SemanticWFSRequest swfs;
	private final StorageService storageService;

	@Autowired
	public DataController(StorageService storageService) {
		this.storageService = storageService;
		swfs = new SemanticWFSRequest();
	}

	// =============================== Linking Metadata to Data
	// =======================

	// initialize the model attribute "metadata"
	@ModelAttribute(name = "metadata")
	public Metadata metadata() {
		return new Metadata();
	}

	// generate the metadata2data view
	@GetMapping("/md2data")
	public String getmd2dataView(Model model) {
		String rtn = "metadata2data";
		try {
			// retrieve metadata
			Metadata md = new Metadata();
			List<String[]> info = md.getMetadata();
			// providing the list of info to the model to allow the view to display all
			// available metadata
			model.addAttribute("MDlist", info);
		} catch (OntoManagementException ex) {
			Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
			final String message = "The connexion to the ontology fails: " + ex.getMessage();
			rtn = "redirect:/data/error?name=" + message;
		}
		// retrieve data collections from the semantic WFS
		try {
			String jsoncollection = swfs.getJSONCollections();
			@SuppressWarnings("deprecation")
			JSONParser parser = new JSONParser();
			Object json = parser.parse(jsoncollection);
			model.addAttribute("objJSON", json);
		} catch (IOException | InterruptedException | ParseException ex) {
			Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
			final String message = "Collection retrieving from the semantic WFS has failed: " + ex.getMessage();
			rtn = "redirect:/data/error?name=" + message;
		}
		return rtn;

	}

	// associate the metadata to a data collection to add them to the catalog web
	// service
	// modeling using the dcat vocabulary: https://www.w3.org/TR/vocab-dcat-2/
	@PostMapping("/addmd2data")
	public String addmd2data(@ModelAttribute("metadata") Metadata metadata, Model model) {
		System.out.println("metadata: " + metadata.getDelivryMd());
		OntModel ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		String gtdcat = "http://lab.ponciano.info/ontology/2020/geotime/dcat#";
		// retrieve the selected metadata uri
		try {
			List<String[]> info = metadata.getMetadata();
			int i = 0;
			boolean found = false;
			String mduri = "";
			while (i < info.size() && !found) {
				if (info.get(i)[2].equals(metadata.getDelivryMd())) {
					mduri = info.get(i)[0];
					// add the metadata as a catalog record
					OntClass c = ont.createClass("http://www.w3.org/ns/dcat#CatalogRecord");
					c.createIndividual(OntoManagement.NS + mduri);
					/*
					 * String query = "INSERT DATA { <" + OntoManagement.NS + mduri +
					 * "> rdf:type <http://www.w3.org/ns/dcat#CatalogRecord> . " + " }";
					 * System.out.println(query); KB.get().update(query);
					 */

					// add the catalog
					c = ont.createClass("http://www.w3.org/ns/dcat#Catalog");
					c.createIndividual("http://lab.ponciano.info/ontology/2020/geotime/dcat#gdi_catalog");
					/*
					 * query = "INSERT DATA { " +
					 * "gtdcat:gdi_catalog rdf:type <http://www.w3.org/ns/dcat#Catalog>." + " }";
					 * System.out.println(query); KB.get().update(query);
					 */

					// add the metadata as a record of the catalog
					ObjectProperty p = ont.createObjectProperty("http://www.w3.org/ns/dcat#record");
					ont.add(ont.getResource(gtdcat + "gdi_catalog"), p, ont.getResource(OntoManagement.NS + mduri));
					/*
					 * query = "INSERT DATA { " +
					 * "gtdcat:gdi_catalog <http://www.w3.org/ns/dcat#record> <" + OntoManagement.NS
					 * + mduri + ">." + " }"; System.out.println(query); KB.get().update(query);
					 */
					found = true;
				}
				i++;
			}

			// retrieve the information of the data collection
			String jsoncollection = swfs.getJSONCollections();
			JSONObject jo = new JSONObject(jsoncollection);
			JSONArray jsarray = jo.getJSONArray("collections");
			System.out.println("dataset: " + metadata.getDelivryDS());
			int j = 0;
			boolean foundc = false;
			while (j < jsarray.length() && !foundc) {
				JSONObject collection = (JSONObject) jsarray.get(j);
				if (collection.getString("name").equals(metadata.getDelivryDS())) {
					System.out.println(collection.getString("name"));
					// create dataset
					UUID dsUri = UUID.randomUUID();
					OntClass c = ont.createClass("http://www.w3.org/ns/dcat#Dataset");
					c.createIndividual(gtdcat + dsUri);
					/*
					 * String query = "INSERT DATA { gtdcat:" + dsUri +
					 * " rdf:type <http://www.w3.org/ns/dcat#Dataset> . " + " }";
					 * System.out.println(query); KB.get().update(query);
					 */

					// add the title of the dataset from its collection
					DatatypeProperty dp = ont.createDatatypeProperty("http://purl.org/dc/elements/1.1/title");
					ont.add(ont.getResource(gtdcat + dsUri), dp, collection.getString("name"));
					/*
					 * query = "INSERT DATA { gtdcat:" + dsUri +
					 * " <http://purl.org/dc/elements/1.1/title> \"" + collection.getString("name")
					 * + "\" . " + " }"; System.out.println(query); KB.get().update(query);
					 */

					// link dataset to catalog through dcat:dataset
					ObjectProperty op = ont.createObjectProperty("http://www.w3.org/ns/dcat#dataset");
					ont.add(ont.getResource(gtdcat + "gdi_catalog"), op, ont.getResource(gtdcat + dsUri));
					/*
					 * query = "INSERT DATA { " +
					 * "gtdcat:gdi_catalog <http://www.w3.org/ns/dcat#dataset> gtdcat:" + dsUri +
					 * "." + " }"; System.out.println(query); KB.get().update(query);
					 */

					// link dataset to metadata record through foaf:primaryTopic
					op = ont.createObjectProperty("http://xmlns.com/foaf/0.1/primaryTopic");
					ont.add(ont.getResource(OntoManagement.NS + mduri), op, ont.getResource(gtdcat + dsUri));
					/*
					 * query = "INSERT DATA { " + "<" + OntoManagement.NS + mduri +
					 * "> <http://xmlns.com/foaf/0.1/primaryTopic> gtdcat:" + dsUri + "." + " }";
					 * System.out.println(query); KB.get().update(query);
					 */

					// retrieve all links of the dataset to model its distributions
					JSONArray links = collection.getJSONArray("links");
					// for each links
					for (int k = 0; k < links.length(); k++) {
						// retrieve the current link object
						JSONObject link = (JSONObject) links.get(k);
						String rel = link.getString("rel");
						if (rel.equals("item")) {
							// create a distribution
							UUID distUri = UUID.randomUUID();
							c = ont.createClass("http://www.w3.org/ns/dcat#Distribution");
							c.createIndividual(gtdcat + distUri);
							/*
							 * query = "INSERT DATA { gtdcat:" + distUri +
							 * " rdf:type <http://www.w3.org/ns/dcat#Distribution> . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */

							// associate the distribution to the current dataset
							op = ont.createObjectProperty("http://www.w3.org/ns/dcat#distribution");
							ont.add(ont.getResource(gtdcat + dsUri), op, ont.getResource(gtdcat + distUri));
							/*
							 * query = "INSERT DATA { gtdcat:" + dsUri +
							 * " <http://www.w3.org/ns/dcat#distribution> gtdcat:"+distUri+" . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */
							// create a dataservice
							UUID dservUri = UUID.randomUUID();
							c = ont.createClass("http://www.w3.org/ns/dcat#DataService");
							c.createIndividual(gtdcat + dservUri);
							/*
							 * query = "INSERT DATA { gtdcat:" + dservUri +
							 * " rdf:type <http://www.w3.org/ns/dcat#DataService> . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */

							// create the href link as a resource
							String href = link.getString("href");
							ont.createResource(href);
							/*
							 * query = "INSERT DATA { <" + href + "> rdf:type rdfs:Resource . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */

							// associate it the url to access it
							op = ont.createObjectProperty("http://www.w3.org/ns/dcat#endpointURL");
							ont.add(ont.getResource(gtdcat + dservUri), op, ont.getResource(href));
							/*
							 * query = "INSERT DATA { gtdcat:" + dservUri +
							 * " <http://www.w3.org/ns/dcat#endpointURL> <"+href+"> . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */

							// associate the dataservice to the distribution
							op = ont.createObjectProperty("http://www.w3.org/ns/dcat#accessService");
							ont.add(ont.getResource(gtdcat + distUri), op, ont.getResource(gtdcat + dservUri));
							/*
							 * query = "INSERT DATA { gtdcat:" + distUri +
							 * " <http://www.w3.org/ns/dcat#accessService> gtdcat:"+dservUri+" . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */

							// add the title to the distribution
							String title = link.getString("title");
							ont.add(ont.getResource(gtdcat + distUri), dp, title);
							/*
							 * query = "INSERT DATA { gtdcat:" + distUri +
							 * " <http://purl.org/dc/elements/1.1/title> \"" + title + "\" . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */

							// add the type to the distribution
							/*
							 * String type=link.getString("type"); query = "INSERT DATA { gtdcat:" + distUri
							 * + " <http://www.w3.org/ns/dcat#mediaType> gtdcat:" + type + " . " + " }";
							 * System.out.println(query); KB.get().update(query);
							 */
						}
					}
					KB.get().getOnt().add(ont);
					KB.get().save();
					foundc = true;
				}
				j++;
			}

		} catch (IOException | InterruptedException | OntoManagementException ex) {
			Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "redirect:/home";
	}

//=============================== Data management =======================

	// ========== Data Uplift management ==========
	/*
	 * @GetMapping("/data/uplift") public String getUpliftView( Model model) {
	 * GMLImporter importer=new GMLImporter(); System.out.println("data uplift");
	 * importer.processFile("", "gn-lu2.gml", false, false, "gn-lu-onto.ttl", "",
	 * "", ""); System.out.println("data uplift done"); return null; }
	 */

	/**
	 * 
	 * @param model represents the thymeleaf model accessible through the view
	 * @return the web interface to choose a shapefile to uplift
	 */
	@GetMapping("/data/ShpUplift")
	public String getShpUpliftView(Model model) {
		String rtn = "shpUpliftView";

		try {
			// retrieve metadata
			Metadata md = new Metadata();
			List<String[]> info = md.getMetadata();
			// providing the list of info to the model to allow the view to display all
			// available metadata
			model.addAttribute("MDlist", info);
		} catch (OntoManagementException ex) {
			Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
			final String message = "The connexion to the ontology fails: " + ex.getMessage();
			rtn = "redirect:/data/error?name=" + message;
		}

		model.addAttribute("files",
				storageService.loadAll()
						.map(path -> MvcUriComponentsBuilder
								.fromMethodName(MetadataController.class, "serveFile", path.getFileName().toString())
								.build().toUri().toString())
						.collect(Collectors.toList()));

		return rtn;
	}

	@GetMapping("/data/ShpUplift/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	// initialize the model attribute "data"
		@ModelAttribute(name = "data")
		public SHPdata data() {
			return new SHPdata("V1.0", "Original version of the dataset");
		}

	/**
     * 
     * @param file represents the shapefile to uplift into RDF triples
     * @param redirectAttributes attributes provided to the view
     * @return the same view with a message informing about the successful uplift and the link to the provided file
     */
    @PostMapping("/data/ShpUplift")
    public String postShpUpliftAction(@ModelAttribute("data") SHPdata data, @RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes) {
        String rtn = "";
        try {
            // store file
            storageService.store(file);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uplift the shapefile: " + file.getOriginalFilename() + "!");
            //transform Shapefile into RDF data
            String rdfdata=ShpUpliftProcess();
            if (rdfdata.equals("")) {
            	throw new ControllerException("file format was incorrect");
            } else {
            	try {
            	//retrieve the RDF representation of the data associated to its metadata
            	OntModel ont= data.representationRDF(rdfdata, file.getOriginalFilename());
                KB.get().getOnt().add(ont);
                KB.get().save();

                } catch (IOException | OntoManagementException ex) {
                    Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
                }
                rtn = "redirect:/data/ShpUplift";
            }
        }
        catch(ControllerException ex)
			{
				// TODO remove file from upload-dir
				final String message = "The uplift fails: " + ex.getMessage();
				rtn = "redirect:/data/error?name=" + message;
			}
        return rtn;
	}

	private String ShpUpliftProcess() {
		// TODO 1.0 @JJPONCIANO
		// 1- execution of script to transform Shapefile into RDF file (NB: storage of
		// mapping file into the directory "r2rml-mapping" for now, later on git)
		// 2- save the resulting RDF file into a directory "rdf-data" and return the RDF
		// file path (NB: empty string "", if no file)
		throw new java.lang.UnsupportedOperationException("Not supported yet.");
	}
	
	// initialize the model attribute "updatedData"
	@ModelAttribute(name = "updatedData")
	public SHPdata updatedData() {
		return new SHPdata();
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

	@GetMapping("data/error")
	public String errorManagement(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("message", name);
		return "errorView";

	}

	// ========== Spatio-temporal Data management ==========

}
