package info.ponciano.lab.geotimewfs.controllers;

import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
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

    /* 
    parameter not yet defined 
     */
    @GetMapping("/metadata/uplift")
    public String getUpliftView(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
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

    /* 
    parameter not yet defined 
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

            KB om = KB.get();
            boolean upliftOk = om.uplift("upload-dir/" + file.getOriginalFilename());
            if (upliftOk) {
                int index = file.getOriginalFilename().indexOf(".");
                String fn = file.getOriginalFilename().substring(0, index);
                om.save();
                //return "redirect:/metadata";
                rtn = "redirect:/metadata/uplift";
            } else {
                throw new ControllerException("file format was incorrect");
            }
        } catch (OntoManagementException | ControllerException | IOException ex) {
            //7Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The uplift fails: " + ex.getMessage();
            // redirectAttributes.addFlashAttribute("message", nessage);
            rtn = "redirect:/errror?name=" + message;
        }
        return rtn;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/errror")
    public String errorManagement(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("message", name);
        return "errorView";

    }

    /* 
    parameter not yet defined 
     */
    @GetMapping("/metadata")
    public String getMetadata(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        //default view to return except case of error
        String rtn = "selectMetadata";
        //retrieve all stored files
        Stream<Path> files = storageService.loadAll();
        List<Path> lf = new ArrayList<>();
        //filter those corresponding to ontologies and add them to a list
        files.filter(s -> s.toString().contains("Onto.owl")).forEach(f -> lf.add(f));
        System.out.println(lf.size());

        //initialize the list of info to display in the view
        List<String[]> info = new ArrayList<String[]>();
        OwlManagement om;
        // browse the list of ontology files
        for (int i = 0; i < lf.size(); i++) {
            try {
                //initialize a new ontology from the path file
                om = new OwlManagement("upload-dir/" + lf.get(i).toString());
                om.addPrefix("iso115", "http://lab.ponciano.info/ontology/2020/geotime/iso-19115#");
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
                System.out.println(om.getSPARQL(query));
                //query the ontology
                ResultSet rs = om.select(query);
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
                }

            } catch (OntoManagementException ex) {
                Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
                final String message = "The connexion to the ontology fails: " + ex.getMessage();
                rtn = "redirect:/errror?name=" + message;
            }
        }
        //providing the list of info to the model to allow the view to display all available metadata
        model.addAttribute("MDlist", info);
        return rtn;
    }

    /* 
    parameter not yet defined 
     */
    @PostMapping("/metadata/selected")
    public String getSelectedMd(@RequestParam(name = "md", required = true, defaultValue = "World") String md, @RequestParam(name = "indSon", required = false, defaultValue = "World")String indSon, Model model) {
        //default view to return except case of error
        String rtn = "metadataView";
        //retrieve all stored files
        Stream<Path> files = storageService.loadAll();
        List<Path> lf = new ArrayList<>();
        //filter those corresponding to ontologies and add them to a list
        files.filter(s -> s.toString().contains("Onto.owl")).forEach(f -> lf.add(f));
        System.out.println(lf.size());

        //initialize the list of info to display in the view
        List<String[]> info = new ArrayList<String[]>();
        OwlManagement om;
        // browse the list of ontology files
        for (int i = 0; i < lf.size(); i++) {
            try {
                //initialize a new ontology from the path file
                om = new OwlManagement("upload-dir/" + lf.get(i).toString());
                if (!om.containsNS("iso115"))
                    om.addPrefix("iso115", "http://lab.ponciano.info/ontology/2020/geotime/iso-19115#");
                //search the metadata individual
                //boolean foundInd = om.listsMetadataIndividuals().toList().contains("http://lab.ponciano.info/ontology/2020/geotime/iso-19115#" + md);
                //System.out.println(foundInd);
                //if (foundInd) {
                    //end the research in files
                    i = lf.size();
                    //initialize the query to retrieve all properties and property values of an instance
                    String query = "SELECT ?p ?o "
                            + "WHERE{"
                            + "iso115:"+md+" ?p ?o. "
                            + "}";
                    System.out.println(om.getSPARQL(query));
                    //create the table of variables
                    String[] var={"p","o"};
                   //query the ontology
                    info=om.queryAsArray(query, var);
                    for(int j=0; j<info.size();j++){
                        for(int k=0; k<info.get(j).length;k++){
                            System.out.println(info.get(j)[k]);
                    }
                   // }
                      
                }

            } catch (OntoManagementException ex) {
                Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
                final String message = "The connexion to the ontology fails: " + ex.getMessage();
                rtn = "redirect:/errror?name=" + message;
            }
        }
        //providing the list of info to the model to allow the view to display all available metadata
        model.addAttribute("MDlist", info);
        return rtn;
    }

    /* 
    parameter not yet defined 
     */
    @PostMapping("/metadata/update")
    public String getMdChangeView(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "redirect:/";
    }

}
