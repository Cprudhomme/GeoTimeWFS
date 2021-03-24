package info.ponciano.lab.array_uplift;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public abstract class ArrayUpliftController {
	
	private ArrayUpliftModel am;

	//view to load CSV file
	@GetMapping("/csv_loading")
    public abstract String getCsvLoadingView(Model model);
	
	//view to load DBF file
	@GetMapping("/dbf_loading")
    public abstract String getDbfLoadingView(Model model);
	
	//View with name of uploaded file and view of the array to define the properties of the ontology  
	@GetMapping("/array_uplift")
    public abstract String getArrayUpliftView(Model model);
	
	//adding of a new Property from its local name, range and type
	//require to update 
	@PostMapping("/property_adding")
    public abstract String addNewProperty();
	
	@PostMapping("/uplift_validation")
    public abstract String ontologyPopulation();
	
	
	
}
