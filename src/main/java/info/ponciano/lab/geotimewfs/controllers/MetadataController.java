package info.ponciano.lab.geotimewfs.controllers;

import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.Metadata;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagement;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MetadataController {

    private final StorageService storageService;

    @Autowired
    public MetadataController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 
     * @param model represents the thymeleaf model accesible through the view
     * @return the web interface to choose a metadata file to uplift
     */
    @GetMapping("/metadata/uplift")
    public String getUpliftView( Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(MetadataController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "upliftView";
    }

    @GetMapping("/metadata/uplift/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * 
     * @param file represents the  XML file to uplift into RDF triples
     * @param redirectAttributes attributes provided to the view
     * @return the same view with a message informing about the successful uplift and the link to the provided XML file
     */
    @PostMapping("/metadata/uplift")
    public String postUpliftAction(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        String rtn = "";
        try {
            // store file
            storageService.store(file);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uplift " + file.getOriginalFilename() + "!");

            boolean upliftOk = KB.get().uplift("upload-dir/" + file.getOriginalFilename());
            if (upliftOk) {
                int index = file.getOriginalFilename().indexOf(".");
                String fn = file.getOriginalFilename().substring(0, index);
                KB.get().save();
                //return "redirect:/metadata";
                rtn = "redirect:/metadata/uplift";
            } else {
                throw new ControllerException("file format was incorrect");
            }
        } catch (OntoManagementException | ControllerException | IOException ex) {
        	//TODO remove file from upload-dir
            final String message = "The uplift fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/error")
    public String errorManagement(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("message", name);
        return "errorView";

    }

    /**
     * 
     * 
     * @param model represents the thymeleaf model accesible through the view
     * @return the view of all instances of MD_metadata contained into the knowledge base with their associated organization name and title
     */
    @GetMapping("/metadata")
    public String getMetadata(Model model) {
        //default view to return except case of error
        String rtn = "selectMetadata";

        //initialize the list of info to display in the view
        //List<String[]> info = new ArrayList<String[]>();
        Metadata md=new Metadata();
        try {
            
            List<String[]> info = md.getMetadata();
            //providing the list of info to the model to allow the view to display all available metadata
            model.addAttribute("MDlist", info);
            /* Function now created in Metadata model: getMetadata()
            //initialize the query to retrieve all instances of metadata and their associated organization and title
            String query = "SELECT ?m ?o ?t "
                    + "WHERE{"
                    + "?m rdf:type iso115:MD_Metadata. "
                    + "?m iso115:contact ?co. "
                    + "?co iso115:organisationName ?o. "
                    + "?m iso115:identificationInfo ?i. "
                    + "?i iso115:citation ?ci. "
                    + "?ci iso115:title ?t. "
                    + "}";
            System.out.println(KB.get().getSPARQL(query));
            //create the table of variables
            String[] var = {"m", "o", "t"};
            //query the ontology
            info = KB.get().queryAsArray(query, var, false, true);
            /*ResultSet rs = om.select(query);
            //adding of the query result into the list of info
            while (rs.hasNext()) {
                QuerySolution solu = rs.next();
                String get1 = solu.get("m").asResource().getURI();
                int index = get1.indexOf("#");
                get1 = get1.substring(index + 1);
                String get2 = solu.getLiteral("o").getString();
                String get3 = solu.getLiteral("t").toString();
                String[] ls = {get1, get2, get3};
                info.add(ls);
            }*/
        } catch (OntoManagementException ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }

    /**
     * 
     * @param md represents the local name of an instance of MD_Metadata
     * @param indSon represents the local name of an instance associated to a metadata instance (by an object property or a succession of object property)
     * @param model represents the thymeleaf model accesible through the view
     * @return the view of the selected metadata 
     */
    @PostMapping("/metadata/selected")
    public String getPostSelectedMd(@RequestParam(name = "md", required = true) String md, @RequestParam(name = "prefixindSon", required = false, defaultValue = "nopIndSON") String prefixindSon, @RequestParam(name = "indSon", required = false, defaultValue = "noIndSON") String indSon, Model model) {
        //default view to return except case of error
        String rtn = "metadataView";
        //initialize the list of info about Object properties to display in the view
        List<String[]> infoOP = new ArrayList<String[]>();
        //initialize the list of info about Data properties to display in the view
        List<String[]> infoDP = new ArrayList<String[]>();
        
        try {
            //variable containing the query to retrieve the Object properties
            String queryOP;
            //initialize the query to retrieve all properties and property values of an instance
            if (indSon.equals("noIndSON")) {
                queryOP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "iso115:" + md + " ?p ?o. "
                        + "?p rdf:type owl:ObjectProperty. "
                        + "}";
            } else {
                queryOP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "<" + prefixindSon+"#"+indSon + "> ?p ?o. "
                        + "?p rdf:type owl:ObjectProperty. "
                        + "}";
            }
            System.out.println(KB.get().getSPARQL(queryOP));
            //variable containing the query to retrieve the Data properties
            String queryDP;
            //initialize the query to retrieve all properties and property values of an instance
            if (indSon.equals("noIndSON")) {
                queryDP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "iso115:" + md + " ?p ?o. "
                        + "?p rdf:type owl:DatatypeProperty. "
                        + "}";
            } else {
                queryDP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "<" + prefixindSon+"#"+ indSon + "> ?p ?o. "
                        + "?p rdf:type owl:DatatypeProperty. "
                        + "}";
            }
            System.out.println(KB.get().getSPARQL(queryDP));
            //create the table of variables
            String[] var = {"p", "o"};
            //query the ontology
            infoOP = KB.get().queryAsArray(queryOP, var, true, false);
            infoDP = KB.get().queryAsArray(queryDP, var, true, false);
            List<String[]> infoOPcomp = new ArrayList<String[]>();
            for(int i=0; i<infoOP.size(); i++){
                String[] op=new String[3];
                String[] obj=infoOP.get(i)[1].split("#");
                op[0]=infoOP.get(i)[0];
                op[1]=obj[0];
                op[2]=obj[1];
                infoOPcomp.add(op);
            }
            /*for (int j = 0; j < info.size(); j++) {
                for (int k = 0; k < info.get(j).length; k++) {
                    System.out.println(info.get(j)[k]);
                }
            }*/
            //providing the list of info to the model to allow the view to display all properties according to their type
            model.addAttribute("md", md);
            model.addAttribute("indSon", indSon);
            model.addAttribute("OPlist", infoOPcomp);
            model.addAttribute("DPlist", infoDP);

        } catch (OntoManagementException ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }

    /**
     * 
     * @param md represents the local name of an instance of MD_Metadata
     * @param indSon represents the local name of an instance associated to a metadata instance (by an object property or a succession of object property)
     * @param model represents the thymeleaf model accesible through the view
     * @return the view of the selected metadata 
     */
    @GetMapping("/metadata/selected")
    public String getSelectedMd(@RequestParam(name = "md", required = true) String md, @RequestParam(name = "indSon", required = false, defaultValue = "noIndSON") String indSon, Model model) {
        //default view to return except case of error
        String rtn = "metadataView";
        //initialize the list of info about Object properties to display in the view
        List<String[]> infoOP = new ArrayList<String[]>();
        //initialize the list of info about Data properties to display in the view
        List<String[]> infoDP = new ArrayList<String[]>();
        
        try {
            //variable containing the query to retrieve the Object properties
            String queryOP;
            //initialize the query to retrieve all properties and property values of an instance
            if (indSon.equals("noIndSON")) {
                queryOP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "iso115:" + md + " ?p ?o. "
                        + "?p rdf:type owl:ObjectProperty. "
                        + "}";
            } else {
                queryOP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "iso115:" + indSon + " ?p ?o. "
                        + "?p rdf:type owl:ObjectProperty. "
                        + "}";
            }
            System.out.println(KB.get().getSPARQL(queryOP));
            //variable containing the query to retrieve the Data properties
            String queryDP;
            //initialize the query to retrieve all properties and property values of an instance
            if (indSon.equals("noIndSON")) {
                queryDP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "iso115:" + md + " ?p ?o. "
                        + "?p rdf:type owl:DatatypeProperty. "
                        + "}";
            } else {
                queryDP = "SELECT ?p ?o "
                        + "WHERE{"
                        + "iso115:" + indSon + " ?p ?o. "
                        + "?p rdf:type owl:DatatypeProperty. "
                        + "}";
            }
            System.out.println(KB.get().getSPARQL(queryDP));
            //create the table of variables
            String[] var = {"p", "o"};
            //query the ontology
            infoOP = KB.get().queryAsArray(queryOP, var, false, true);
            infoDP = KB.get().queryAsArray(queryDP, var, false, true);
            /*for (int j = 0; j < info.size(); j++) {
                for (int k = 0; k < info.get(j).length; k++) {
                    System.out.println(info.get(j)[k]);
                }
            }*/
            //providing the list of info to the model to allow the view to display all properties according to their type
            model.addAttribute("md", md);
            model.addAttribute("indSon", indSon);
            model.addAttribute("OPlist", infoOP);
            model.addAttribute("DPlist", infoDP);

        } catch (OntoManagementException ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }
    
    /**
     * 
     * @param md represents the local name of an instance of MD_Metadata
     * @param ind represents the local name of an instance associated to a metadata instance (by an object property or a succession of object property)
     * @param property represents the local name of a datatype property associated to "md" or "ind"
     * @param value represents the value associated to "md" or "ind" by "property"
     * @return stay on the current page
     */
    @GetMapping("/metadata/update")
    public String getMdChangeView(@RequestParam(name = "md", required = true) String md, @RequestParam(name = "ind", required = false, defaultValue = "noIndSON") String ind, @RequestParam(name = "property", required = true) String property, @RequestParam(name = "value", required = true) String value) {
        String rtn="redirect:/";
        String ns= OntoManagement.NS;
        try {
            if(ind.equals("noIndSON"))
                KB.get().change(ns+md, ns+property, value);
            else
                KB.get().change(ns+ind, ns+property, value);
                //save the change of the knowledge base
                KB.get().save();
        } catch (OntoManagementException|IOException ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/error?name=" + message;
        }
        return rtn;
    }

}
