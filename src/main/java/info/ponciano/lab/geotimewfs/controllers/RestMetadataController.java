package info.ponciano.lab.geotimewfs.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class RestMetadataController {
    

    /* 
    return not yet defined 
     */
    @PostMapping("/metadata/downlift")
    public String postMdChangeAction(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "not yet defined";
    }

}