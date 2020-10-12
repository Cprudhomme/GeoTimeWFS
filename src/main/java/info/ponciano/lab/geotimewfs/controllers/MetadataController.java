package info.ponciano.lab.geotimewfs.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MetadataController {
    /* 
    parameter not yet defined 
     */
    @GetMapping("/metadata/uplift")
    public String getUpliftView(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "upliftView";
    }

    /* 
    parameter not yet defined 
     */
    @PostMapping("/metadata/uplift")
    public String postUpliftAction(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "redirect:/";
    }

    /* 
    parameter not yet defined 
     */
    @GetMapping("/metadata")
    public String getMetadata(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "selectMetadata";
    }

    /* 
    parameter not yet defined 
     */
    @PostMapping("/metadata/selected")
    public String getSelectedMd(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "metadataView";
    }

    /* 
    parameter not yet defined 
     */
    @PostMapping("/metadata/update")
    public String getMdChangeView(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return "redirect:/";
    }

}