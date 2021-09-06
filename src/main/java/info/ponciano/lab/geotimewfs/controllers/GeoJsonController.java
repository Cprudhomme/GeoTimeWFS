/*
 * Copyright (C) 2021 jean-jacquesponciano.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.geotimewfs.controllers;

import info.ponciano.lab.geotimewfs.array_uplift.PropertyForm;
import info.ponciano.lab.geotimewfs.array_uplift.PropertyMapping;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import info.ponciano.lab.geotimewfs.models.geojson.GeoJsonRDF;
import info.ponciano.lab.geotimewfs.models.semantic.KB;
import info.ponciano.lab.geotimewfs.models.semantic.OntoManagementException;
import info.ponciano.lab.pisemantic.PiOnt;
import info.ponciano.lab.pisemantic.PiOntologyException;
import info.ponciano.lab.pitools.files.PiFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jean-jacquesponciano
 */
@Controller
@RequestMapping("/geojson")
public class GeoJsonController {

    private final StorageService storageService;

    @Autowired
    public GeoJsonController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/uplift")
    public String uplift(@RequestParam("file") MultipartFile file, Model model) {
        try {
            //RedirectAttributes redirectAttributes) {
            // store file
            storageService.store(file);

            //File reading
            String filename = file.getOriginalFilename();
            String geojsonfilepath = "upload-dir/" + filename;

            //execute the uplift
            GeoJsonRDF.upliftGeoJSON(geojsonfilepath, KB.get().getOnt());

            model.addAttribute("message", "File uplifted");
            return "geoJSON";
        } catch (OntoManagementException | IOException | ParseException | PiOntologyException ex) {
            Logger.getLogger(GeoJsonController.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("message", ex.getMessage());
            return "error";
        }

    }

    @PostMapping("/downlift")
    public String downlift(@Valid GeoJsonForm dataindiv, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", bindingResult.getAllErrors().toString());
            return "geoJSON";
        }
        String uri = dataindiv.getName();

        try {
            //downlift
            String downlift = GeoJsonRDF.downlift(KB.get().getOnt(), uri);
            //save the file
            String out = uri.substring(uri.lastIndexOf('#') + 1, uri.length());
            new PiFile(out).writeTextFile(downlift);

            model.addAttribute("file", "/download/" + out);
            return "view";
        } catch (PiOntologyException | OntoManagementException ex) {
            Logger.getLogger(GeoJsonController.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("message", ex.getMessage());
            return "error";
        }

    }

//    @GetMapping("/files/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//        Resource file = storageService.loadAsResource(filename);
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    // initialize the model attribute "dataindiv"
    @ModelAttribute(name = "dataindiv")
    public GeoJsonForm propmap() {
        return new GeoJsonForm();
    }

}
