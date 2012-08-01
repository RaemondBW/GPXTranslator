
public class singleDataItem {
	private String name;
	private String description;
	private Double altitude;
	private Double latitude;
	private Double longitude;
	private String time;
	private String date;
	private Integer hour;
	private Integer minute;
	private Double second;
	
	
	
	
	
	singleDataItem() {
		this.name = "unknown";
		this.altitude = 0.0;
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.description = "none";
		this.time = "none";
	}
	
	
	
	
	
	
	singleDataItem(String newLocation, String newDescription, Double newAltitude, Double newLatitude, Double newLongitude, String newTime) {
		this.name = newLocation;
		this.altitude = newAltitude;
		this.latitude = newLatitude;
		this.longitude = newLongitude;
		this.description = newDescription;
		this.time = newTime;
		parseTime(newTime);
	}
	
	
	
	
	
	
	
	public void setName (String newName) { this.name = newName;}
	public void setAltitude (Double newAltitude) {this.altitude = newAltitude;}
	public void setLongitude (Double newLongitude) {this.longitude = newLongitude;}
	public void setLatitude (Double newLatitude){this.latitude = newLatitude;}
	public void setDescription (String newDescription) {this.description = newDescription;}
	public void setTime (String newTime) {
		this.time = newTime;
		parseTime(newTime);
	}

	public String getName () {return name;}
	public Double getAltitude () {return altitude;}
	public Double getLongitude() {return longitude;}
	public Double getLatitude() {return latitude;}
	public String getDescription() {return description;}
	public String getTime() {return time;}
	
	
	
	
	
	public void setCoordinants (String Coordinants) {
		String[] parts = Coordinants.split(",");
		
		latitude = Double.parseDouble(parts[0]);
		longitude = Double.parseDouble(parts[1]);
		if (parts.length > 2) {
			this.altitude = Double.parseDouble(parts[2]);
		}
	}
	
	
	
	
	
	public void parseTime(String newTime) {
		String[] dateSplit = newTime.split("T");
		date = dateSplit[0];
		String[] timeSplit = dateSplit[1].split(":");
		hour = Integer.parseInt(timeSplit[0]);
		minute = Integer.parseInt(timeSplit[1]);
		second = Double.parseDouble(timeSplit[2].substring(0,timeSplit[2].length() - 1));
	}
	
	
	
	
	
	public String toString() {
		String output;
		output = "Name: " + name + ", Description: "+ description + ", Location: ("+ latitude + "," + longitude + ","+ altitude + "), time: " + hour + ":" +minute +":" + second + "z";
		return output;
	}
	
	public Double timeToSeconds() {
		return (double) (hour*3600 + minute*60 + second);
	}
}

