package info.ponciano.lab.geotimewfs.controllers;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class RestMetadataController {
    

    /* 
    return not yet defined 
     */
    @PostMapping("/metadata/downlift")
    public String postMdChangeAction(@RequestParam(name = "md", required = true) String md) {
        String rtn;
        try {
            rtn= KB.get().downlift("http://lab.ponciano.info/ontology/2020/geotime/iso-19115#"+md);
        } catch (OntoManagementException ex) {
            Logger.getLogger(RestMetadataController.class.getName()).log(Level.SEVERE, null, ex);
            final String message = "The connexion to the ontology fails: " + ex.getMessage();
            rtn = "redirect:/errror?name=" + message;
        }
        return rtn;
    }

}