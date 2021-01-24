package info.ponciano.lab.geotimewfs.models;

import org.springframework.web.multipart.MultipartFile;

public class Schema {

	//Attributes
	private String type;
	private String dist;
	private String distTitle;
	private String distFormat;
	private String username;
	private String psw;
	
	//Getters and setters
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
		String[] w = dist.split("/");
		this.distTitle=w[0];
		this.distFormat=w[1];
	}
	public String getDistTitle() {
		return distTitle;
	}
	public String getDistFormat() {
		return distFormat;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	
	public void display() {
		System.out.println("type: "+this.type);
		System.out.println("dist: "+this.dist);
		System.out.println("title: "+this.distTitle);
		System.out.println("format: "+this.distFormat);
		System.out.println("username: "+this.username);
	}
	
}
