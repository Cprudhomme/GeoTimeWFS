package de.hsmainz.cs.semgis.wfs.resultformatter;


public abstract class VectorResultFormatter extends ResultFormatter{
	
	String featureType="";
	
	public static final String WKTLiteral="http://www.opengis.net/ont/geosparql#wktLiteral";
	
	@Override
	public String parseCoverageLiteral(String literalValue, String literalType, String epsg, String srsName) {
		return null;
	}
	

}
