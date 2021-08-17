package info.ponciano.lab.geotimewfs.array_uplift;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class  PropertyForm  {

  @NotNull
	@Size(min=2, max=30)
	private String name;

  @NotNull
	@Size(min=2, max=30)
	private String range;

public  PropertyForm(){
  this.name="Property Name";
  this.range="Range";
}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

/**
 * @param range the range to set
 */
public void setRange(String range) {
	this.range = range;
}
/**
 * @return the range
 */
public String getRange() {
	return range;
}
@Override
public String toString() {
  return 	 "Property( Name: " + this.name + ", Range: " + this.range + ")";

}
}
