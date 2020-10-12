package info.ponciano.lab.geotimewfs.models;

public interface CswModel {
    /**
     * allows CSW clients to retrieve service metadata from a server
     *
     * @return
     */
    public String getCapabilities(); 

    /**
     * allows a client to discover elements of the information model supported
     * by the target catalogue service. The operation allows some or all of the
     * information model to be described
     *
     * @return
     */
    public String describeRecord(); //To change body of generated methods, choose Tools | Templates.
    

    /**
     * search for records, returning record IDs
     *
     * @return
     */
    public String getRecords(); 

    /**
     * retrieves the default representation of catalogue records using their
     * identifier
     *
     *
     * @return
     */
    public String getRecordById() ; 

    /**
     * used to obtain runtime information about the range of values of a
     * metadata record element or request parameter
     *
     *
     * @return
     */
    public String getDomain(); 

    /**
     * create/update metadata by asking the server to 'pull' metadata from
     * somewhere
     *
     * @return
     */
    public String harvest(); 

    /**
     * create/edit metadata by 'pushing' the metadata to the server
     *
     * @return
     */
    public String transaction(); 
}