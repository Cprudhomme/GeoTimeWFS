package info.ponciano.lab.geotimewfs.controllers;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
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

import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import info.ponciano.lab.geotimewfs.models.JGit;
import info.ponciano.lab.geotimewfs.models.Schema;
import info.ponciano.lab.geotimewfs.models.SchemaValidation;

@Controller
public class SchemaController {
	
	private final StorageService storageService;

    @Autowired
    public SchemaController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 
     * @param model represents the thymeleaf model accessible through the view
     * @return the web interface to choose a schema to upload
     */
    @GetMapping("/schema/upload")
    public String getSchemaUploadView( Model model) {
    	String rtn="SchemaUpload";
    	
    	//file management
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(SchemaController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        
        //initialize the list to retrieve all available distributions title and 
        //their associated format
        List<String[]> info = new ArrayList<String[]>();
        try {
            //variable containing the query to retrieve distributions and format
            String query ="SELECT ?d ?f "
                        + "WHERE{"
                        + "?di rdf:type dcat:Distribution. "
                        + "?di <http://purl.org/dc/elements/1.1/title> ?d. "
                        + "?di dcat:accessService ?ds. "
                        + "?ds dcat:endpointURL ?f. "
                        + "}";
            System.out.println(KB.get().getSPARQL(query));
            //query the ontology
            String[] var = {"d", "f"};
            info = KB.get().queryAsArray(query, var, true, false);
            //create the list to give to the model from the distribution and format
            List<String> distList = new ArrayList<String>();
            for(int i=0; i<info.size(); i++){
                String dist=info.get(i)[0];
                String[] finfo = info.get(i)[1].split("=");
                String f=finfo[1];//.substring(0, finfo[1].length()-2);
                dist +="/";
                dist +=f;
                distList.add(dist);
            }
            //add the list to the model
            model.addAttribute("DistList", distList); 
            model.addAttribute("schema", new Schema());
        } catch (OntoManagementException ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }

    @GetMapping("/schema/upload/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * 
     * @param file represents a schema to upload in GitLab repository and whose its representation must be added
     * to a data set distribution into the knowledge base.
     * @param schema fulfilled class returned by the form 
     * @param model represents the thymeleaf model accessible through the view that contains elements required for the view to return
     * @return a view with a message informing about the successful upload of the provided schema
     */
    @PostMapping("/schema/upload")
    public String postUpliftAction(@RequestParam("file") MultipartFile file, @ModelAttribute("schema") Schema schema,
    		Model model) {//RedirectAttributes redirectAttributes) {

        	String rtn = "view";
            // store file
            storageService.store(file);
            //init parameter to add the file to git
            String localRepoName="schema-storage";
            String dirInRepo;
            if(schema.getType().equals("xsd")) {
            	dirInRepo="xsd-geojson_schema/";
            }
            else{
            	dirInRepo="MappingSchema/";
            }
            File f = new File("./upload-dir/"+file.getOriginalFilename());
            System.out.println(f);
            String uri="https://git.gdi-de.org/claire.prudhomme/schema-storage.git";
            //add the uploaded file to git
            JGit.AddFile2Git(localRepoName, dirInRepo, f, schema.getUsername(), schema.getPsw(), uri);
           
            //add the link between the uploaded schema and its related distribution into the knowledge base
            //creation of the ontmodel
            OntModel ont= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            String gt="http://lab.ponciano.info/ontology/2020/geotime#";
            //retrieve the selected distribution
            try {
            	//initialize the list to retrieve the selected distribution
                List<String[]> info = new ArrayList<String[]>();
                    //variable containing the query to retrieve distributions and format
                    String query ="SELECT ?di ?f "
                                + "WHERE{"
                                + "?di rdf:type dcat:Distribution. "
                                + "?di <http://purl.org/dc/elements/1.1/title> \""+schema.getDistTitle()+"\". "
                                + "?di dcat:accessService ?ds. "
                                + "?ds dcat:endpointURL ?f. "
                                + "}";
                    System.out.println(query);
                    System.out.println(KB.get().getSPARQL(query));
                    //query the ontology
                    String[] var = {"di", "f"};
                    info = KB.get().queryAsArray(query, var, true, false);
                    //create the list to give to the model from the distribution and format
                    for(int i=0; i<info.size(); i++){
                        String[] finfo = info.get(i)[1].split("=");
                        String format=finfo[1];
                        if(format.equals(schema.getDistFormat())) {
                        	//create an instance of schema related to the distribution 
                            OntClass c=ont.createClass(gt+"Schema");
                            UUID uuid = UUID.randomUUID();
                            c.createIndividual(gt+uuid);
                            ObjectProperty p=ont.createObjectProperty(gt+"hasSchema");
                            ont.add(ont.getResource(info.get(i)[0]),p,ont.getResource(gt+uuid));
                            //create an instance of a data service to retrieve the schema
                            OntClass c2=ont.createClass("http://www.w3.org/ns/dcat#DataService");
                            UUID uuid2 = UUID.randomUUID();
                            c2.createIndividual(gt+uuid2);
                            ObjectProperty p2=ont.createObjectProperty("http://www.w3.org/ns/dcat#accessService");
                            org.apache.jena.rdf.model.Resource resource = ont.getResource(gt+uuid2);
							ont.add(ont.getResource(gt+uuid),p2,resource);
                            //create the endpoint of the schema's data service
                            ObjectProperty p3=ont.createObjectProperty("http://www.w3.org/ns/dcat#endpointURL");
                            String newName = uri+"/"+dirInRepo+file.getOriginalFilename();
							org.apache.jena.rdf.model.Resource newResource = ont.createResource(newName);
                            ont.add(resource,p3,newResource);
                        }
      	
                    }
                    //adding to the knowledge base
                    KB.get().getOnt().add(ont);
                    KB.get().save();
                } catch (IOException | OntoManagementException ex) {
                    Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
                }
                   
            //return the message of successful upload
            String message="You successfully upload " + file.getOriginalFilename() + "!";
            model.addAttribute("message", message);
        return rtn;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 
     * @param model represents the thymeleaf model accessible through the view
     * @return the web interface to validate a downlift through its associated schema
     */
    @GetMapping("/schema/validation")
    public String getSchemaValidationView(Model model) {
    	String rtn="schemaValidation";
        
        //initialize the list to retrieve the title, the format, and the schema end point of a distribution
        List<String[]> info = new ArrayList<String[]>();
        try {
            //variable containing the query to retrieve distributions that have an associated schema
            String query ="SELECT ?d ?f ?s "
                        + "WHERE{"
                        + "?di rdf:type dcat:Distribution. "
                        + "?di <http://purl.org/dc/elements/1.1/title> ?d. "
                        + "?di dcat:accessService ?ds1. "
                        + "?ds1 dcat:endpointURL ?f. "
                        + "?di <http://lab.ponciano.info/ontology/2020/geotime#hasSchema> ?sc. "
                        + "?sc dcat:accessService ?ds2. "
                        + "?ds2 dcat:endpointURL ?s. "
                        + "}";
            System.out.println(KB.get().getSPARQL(query));
            //query the ontology
            String[] var = {"d", "f", "s"};
            info = KB.get().queryAsArray(query, var, true, false);
            //create the list to give to the model from the distribution, format, and schema
            List<String> distList = new ArrayList<String>();
            for(int i=0; i<info.size(); i++){
                String dist=info.get(i)[0];
                String[] finfo = info.get(i)[1].split("=");
                String f=finfo[1];//.substring(0, finfo[1].length()-2);
                dist +=", ";
                dist +=f;
                dist +=", ";
                dist +=info.get(i)[1];
                distList.add(dist);
            }
            //add the list to the model
            model.addAttribute("DistList", distList); 
            model.addAttribute("schemaval", new SchemaValidation());
        } catch (OntoManagementException ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }

    @PostMapping("/schema/validation")
    public String postSchemaValidation(@ModelAttribute("schemaval") SchemaValidation schemaval,
    		Model model) {
    	String rtn = "view";
    	//return the message of validation result
    	boolean res=false;
    	String message;
    	if(res)
    		message="The downlift has been validated !";
    	else
    		message="The downlift has not been validated ! Please check the downlift fonction.";
        model.addAttribute("message", message);
    	return rtn;
    }
}
