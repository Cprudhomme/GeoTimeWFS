package info.ponciano.lab.geotimewfs.models;

public class SchemaValidation {

		//Attributes
		private String dist;
		private String distTitle;
		private String distFormat;
		private String distSchema;
		
		//Getters and setters
		public String getDist() {
			return dist;
		}
		public void setDist(String dist) {
			this.dist = dist;
			String[] w = dist.split(", ");
			this.distTitle=w[0];
			this.distFormat=w[1];
			this.distSchema=w[2];
		}
		public String getDistTitle() {
			return distTitle;
		}
		public String getDistFormat() {
			return distFormat;
		}
		public String getDistSchema() {
			return distSchema;
		}
		public void display() {
			System.out.println("dist: "+this.dist);
			System.out.println("title: "+this.distTitle);
			System.out.println("format: "+this.distFormat);
			System.out.println("schema: "+this.distSchema);
		}
}
