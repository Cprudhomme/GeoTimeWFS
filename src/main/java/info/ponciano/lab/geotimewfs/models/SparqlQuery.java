package info.ponciano.lab.geotimewfs.models;

public class SparqlQuery {
	String query;

    public SparqlQuery(String query) {
        this.query = query;
    }

    public SparqlQuery() {
    
    }

    public String getResults() {
        return query;
    }

    public void setResults(String query) {
        this.query = query;
    }
}