/**
 * 
 */
package info.ponciano.lab.geotimewfs.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Claire Prudhomme
 *
 * 
 */
@Controller
public class SpatioTemporalController {

	@GetMapping("/SpatioTemporal/{asset}/{version}")
	public String spatioTemporal(@PathVariable(name = "asset", required = true) String asset,
			@PathVariable(name = "version", required = true) String version, Model model) {
		SpatioTemporalCModel stm = new SpatioTemporalCModel(asset, version);
		List<String> versions = stm.getVersion();
		var geodata = stm.getGeoData();
		List<String[]> po = stm.getPropertiesOP();
		List<String[]> pd = stm.getPropertiesDP();
		model.addAttribute("pd", pd);
		model.addAttribute("po", po);
		model.addAttribute("geodata", geodata);
		model.addAttribute("versions", versions);
		return "SpatioTemporalView";
	}

}
