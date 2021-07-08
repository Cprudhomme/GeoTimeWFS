/**
 * 
 */
package info.ponciano.lab.geotimewfs.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import info.ponciano.lab.geotimewfs.models.FeatureCollection;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import info.ponciano.lab.pitools.files.PiFile;

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
		
		var geodata = stm.getGeoData();
		List<String[]> po = stm.getPropertiesOP();
		List<String[]> pd = stm.getPropertiesDP();
		model.addAttribute("pd", pd);
		model.addAttribute("po", po);
		
		List<String> versions;
		try {
			versions = stm.getVersions(asset);
			//String curAsset=stm.getAsset(asset, version);
			FeatureCollection fc= new FeatureCollection(asset);//curAsset);
			System.out.println("enter in controller");
			String[] shpArray= { "/data/vg250krs_1998.shp","/data/vg250krs_1999.shp","/data/vg250krs_2000.shp"}; 
			model.addAttribute("shp",shpArray);
	//		model.addAttribute("dbf", "/data/vg250krs.dbf");
			model.addAttribute("versions", versions);
			String csv="attribute-data/vg250gem.csv";
			PiFile pf= new PiFile(csv);
			String [][] attribute=pf.readCSV(";");
			System.out.println("attribute size: "+attribute.length);
			List<String[]> lf=new ArrayList<String []>();
			if(attribute!=null)
				lf= List.of(attribute);

			model.addAttribute("fc", lf);//fc.features);
		} catch (OntoManagementException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return "SpatioTemporalViewSHP.html";
	}



private List<String[]> convertTab2List(String [][] tab)
{
	List<String[]> of = List.of(tab);
	
	List<String[]> list=new ArrayList<>();
	for (int i=0; i<tab.length; i++)
	{
		String [] stab=tab[i];
		list.add(stab);
	}
	return list;
}

}
