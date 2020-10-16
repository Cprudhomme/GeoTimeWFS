package info.ponciano.lab.geotimewfs.controllers;

import info.ponciano.lab.geotimewfs.controllers.storage.StorageFileNotFoundException;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.semantic.OwlManagement;
import java.io.IOException;
import java.util.stream.Collectors;
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
			RedirectAttributes redirectAttributes) throws IOException {

                // store file
		storageService.store(file);
                
		redirectAttributes.addFlashAttribute("message",
				"You successfully uplift " + file.getOriginalFilename() + "!");
               
                OwlManagement om= new OwlManagement();
                om.uplift("upload-dir/"+file.getOriginalFilename());
                om.saveOntology("upload-dir/metadataOnto.owl");
        //return "redirect:/metadata";
        return "redirect:/metadata/uplift";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
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
