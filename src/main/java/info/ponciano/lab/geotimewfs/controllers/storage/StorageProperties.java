package info.ponciano.lab.geotimewfs.controllers.storage;

import info.ponciano.lab.pitools.files.PiFile;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        new PiFile(this.location).mkdir();

    }

    public StorageProperties() {
        new PiFile(this.location).mkdir();
    }


}
