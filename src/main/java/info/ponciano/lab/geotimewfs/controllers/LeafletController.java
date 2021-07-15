package info.ponciano.lab.geotimewfs.controllers;

import java.util.ArrayList;
import java.util.List;

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
public class LeafletController {

    private final StorageService storageService;
    
    @Autowired
    public LeafletController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    /**
     * 
     * @param model represents the thymeleaf model accessible through the view
     * @return the web interface of the result of a specific SPARQL request
     * @throws OntoManagementException 
     */
    
    @GetMapping("/thematicMaps")
    public String displayThematicMapsHome(Model model) throws OntoManagementException  {
        String rtn="thematicMaps";
        return rtn;
    }

    @GetMapping("/mapCreation")
    public String displayMapCreation(Model model) throws OntoManagementException  {
        String rtn="mapCreation";
        return rtn;
    }

    @GetMapping("/mapList")
    public String displayMapList(Model model) throws OntoManagementException  {
        String rtn="mapList";
        return rtn;
    }

    @GetMapping("/mapExample")
    public String displayMapExample(Model model) throws OntoManagementException  {
        String rtn="mapExample";
        return rtn;
    }

    @GetMapping("/mapCreation/schools")
    public String displaySchools(Model model) throws OntoManagementException  {
        String rtn="schools";
        return rtn;
    }

    @GetMapping("/mapCreation/universities")
    public String displayUniversities(Model model) throws OntoManagementException  {
        String rtn="universities";
        return rtn;
    }
    
    @GetMapping("/mapCreation/hospitals")
    public String displayHospitals(Model model) throws OntoManagementException  {
        String rtn="hospitals";
        return rtn;
    }

    @GetMapping("/mapCreation/townhalls")
    public String displayTownHalls(Model model) throws OntoManagementException  {
        String rtn="townhalls";
        return rtn;
    }

}
