package de.hsmainz.cs.semgis.wfs.resultformatter.vector;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.locationtech.jts.geom.Geometry;

import de.hsmainz.cs.semgis.wfs.resultformatter.VectorResultFormatter;
import de.hsmainz.cs.semgis.wfs.resultstyleformatter.StyleObject;

/**
 * Formats a query result to GeoURI. Only geometries will be serialized,
 * attributes will be ignored.
 *
 */
public class GeoURIFormatter extends VectorResultFormatter {

	public GeoURIFormatter() {
		this.mimeType = "text/plain";
		this.exposedType = "text/geouri";
		this.urlformat = "geouri";
		this.label = "GeoURI";
		this.fileextension = "txt";
		this.definition = "https://tools.ietf.org/html/rfc5870";
	}

	@Override
	public String formatter(ResultSet results, String startingElement, String featuretype, String propertytype,
			String typeColumn, Boolean onlyproperty, Boolean onlyhits, String srsName, String indvar, String epsg,
			List<String> eligiblenamespaces, List<String> noteligiblenamespaces, StyleObject mapstyle,
			Boolean alternativeFormat, Boolean invertXY,Boolean coverage,Writer out) throws IOException {
		List<QuerySolution> test = ResultSetFormatter.toList(results);
		String lastInd = "";
		for (QuerySolution solu : test) {
			Iterator<String> varnames = solu.varNames();
			while (varnames.hasNext()) {
				String name = varnames.next();
				String curfeaturetype = "";
				if (solu.get(indvar) != null) {
					curfeaturetype = solu.get(indvar).toString();
					if (curfeaturetype.contains("http") && curfeaturetype.contains("#")) {
						curfeaturetype = curfeaturetype.substring(curfeaturetype.lastIndexOf('#') + 1);
					}
					if (!solu.get(indvar).toString().equals(lastInd) || lastInd.isEmpty()) {
						lastQueriedElemCount++;
					}
				}
				if (name.endsWith("_geom")) {
					Geometry geom = this.parseVectorLiteral(
							solu.get(name).toString().substring(0, solu.get(name).toString().indexOf("^^")),
							solu.get(name).toString().substring(solu.get(name).toString().indexOf("^^") + 2), epsg,
							srsName);
					if (geom != null) {
						String uricode = "";
						if (!srsName.isEmpty()) {
							if (srsName.startsWith("http")) {
								uricode = "EPSG:" + srsName.substring(srsName.lastIndexOf("/") + 1);
							} else {
								uricode = srsName;
							}
						} else {
							uricode = epsg;
						}
						if ("POINT".equalsIgnoreCase(geom.getGeometryType())) {
							out.write("geo:" + geom.getCoordinate().x + "," + geom.getCoordinate().y + ";crs="
									+ uricode + System.lineSeparator());
						} else {
							out.write("geo:" + geom.getCentroid().getCoordinate().x + ","
									+ geom.getCentroid().getCoordinate().y + ";crs=" + uricode
									+ System.lineSeparator());
						}
					}
				}
			}
		}
		return "";
	}

}
