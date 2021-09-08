/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.ponciano.lab.geotimewfs.controllers.last.sparql;

import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.pisemantic.PiSparql;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jean-jacquesponciano
 */
@Controller
@RequestMapping(value = "/sparqlend")
public class SparqlController {

    private final StorageService storageService;

    @Autowired
    public SparqlController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("queryresult", "");
        return "sparql/sparqlEP";
    }

    @ModelAttribute(name = "squery")
    public SparqlQuery sarqlquery() {
        return new SparqlQuery();
    }

    @PostMapping("/query")
    public String query(@ModelAttribute("squery") SparqlQuery sq, Model model) throws Exception {
        String r;
        try {
            r = KB.get().getOnt().selectAsText(sq.getQuery());
        } catch (Exception e) {
            r = e.getMessage();
        }

        model.addAttribute("queryresult", r);
        return "sparql/sparqlEP";
    }

}
