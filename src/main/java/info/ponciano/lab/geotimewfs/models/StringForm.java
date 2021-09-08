package info.ponciano.lab.geotimewfs.models;

public class StringForm {
	String string;

    public StringForm(String query) {
        this.string = query;
    }

    public StringForm() {
    this.string="";
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

}


