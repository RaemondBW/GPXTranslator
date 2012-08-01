
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GPXTranslator extends DefaultHandler{
	
	private singleDataItem SDI;
	private String temp;
	private static ArrayList<singleDataItem> locationDataList = new ArrayList<singleDataItem>();
	private String listName;
	private String listDescription;
	private static ArrayList<Double> deltaTime = new ArrayList<Double>();
	private static ArrayList<Double> deltaDistance = new ArrayList<Double>();
	private static ArrayList<Double> bearing = new ArrayList<Double>();
	
	
	
	
	public static void main(String[] args) {
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream("output.csv"));
			System.setOut(out);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		SAXParserFactory spfac = SAXParserFactory.newInstance();
		SAXParser sp;
		GPXTranslator handler = new GPXTranslator();
		try {
			sp = spfac.newSAXParser();
			sp.parse("Track 011.gpx", handler);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//handler.readList();
		calculateDeltaDistance();
		calculateDeltaTime();
		calculateAllBearings();
		System.out.println("Longitude, Latitude, Elevation, Bearing, Displacement(miles), Elapsed Time(sec), Average Velocity(mph), Time Since Start(sec)");
		System.out.println(locationDataList.get(0).getLongitude() + "," + locationDataList.get(0).getLatitude() + "," +
				locationDataList.get(0).getAltitude() + "," + (locationDataList.get(0).timeToSeconds()-locationDataList.get(0).timeToSeconds()) + "," + 0.0 + "," + 0.0 + "," + 0.0 + "," + 0.0);
		for (int i=0; i<deltaDistance.size(); i++){
			System.out.println(locationDataList.get(i+1).getLongitude() + "," + locationDataList.get(i+1).getLatitude() + "," + locationDataList.get(i+1).getAltitude() + "," + bearing.get(i) + "," +deltaDistance.get(i) + "," + deltaTime.get(i) + "," + ((deltaDistance.get(i)/deltaTime.get(i))*3600) + "," + (locationDataList.get(i+1).timeToSeconds()-locationDataList.get(0).timeToSeconds()));
		}
	}
	

	
	
	
	public void characters(char[] buffer, int start, int length) {
    	temp = new String(buffer, start, length);
    }
   
    
    
	
    
	public void startElement(String uri, String localName, 
			String qName, Attributes attributes) throws SAXException {
		temp = "";
		if (qName.equalsIgnoreCase("trkpt")) {
			SDI = new singleDataItem();
			SDI.setLatitude(Double.parseDouble(attributes.getValue("lat")));
			SDI.setLongitude(Double.parseDouble(attributes.getValue("lon")));
			SDI.setName(listName);
			SDI.setDescription(listDescription);
		}
	}

    
    
    
	
    public void endElement(String uri, String localName, String qName)
                  throws SAXException {
    	
    	if (qName.equalsIgnoreCase("name")){
			listName = temp;
		} else if (qName.equalsIgnoreCase("desc")){
			listDescription = temp;
		}
    	
		if (qName.equalsIgnoreCase("trkpt")) {
			locationDataList.add(SDI);
		} else if (qName.equalsIgnoreCase("Name")) {
			if (SDI == null) {
				SDI = new singleDataItem();
				SDI.setName(listName);
				SDI.setDescription(listDescription);
			}
			SDI.setName(temp);
		} else if (qName.equalsIgnoreCase("ele")) {
			SDI.setAltitude(Double.parseDouble(temp));
		} else if (qName.equalsIgnoreCase("time")) {
			SDI.setTime(temp);
		}
    }

    
    
    
    
    private void readList() {
    	System.out.println("No of locations: " + locationDataList.size()  + ".");
    	Iterator<singleDataItem> it = locationDataList.iterator();
    	while (it.hasNext()) {
    		System.out.println(it.next().toString());
    	}
    }
    
    
    
    
    
    private static void calculateDeltaDistance() {
    	for (int i=0; i<locationDataList.size()-1; i++) {
    		deltaDistance.add(distFrom(locationDataList.get(i).getLatitude(),locationDataList.get(i).getLongitude(),
    				locationDataList.get(i+1).getLatitude(),locationDataList.get(i+1).getLongitude()));
    	}
    }
    
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }
    
    
    
    
    
    private static void calculateDeltaTime() {
    	for (int i=0; i<locationDataList.size()-1; i++) {
    		//Double numberSecondsAfter = locationDataList.get(i).timeToSeconds();
    		deltaTime.add(locationDataList.get(i+1).timeToSeconds()-locationDataList.get(i).timeToSeconds());
    	}
    }
    
    
    
    
    private static void calculateAllBearings() {
    	for (int i=0; i<locationDataList.size()-1; i++) {
    		bearing.add(calculateBearing(locationDataList.get(i).getLatitude(),locationDataList.get(i).getLongitude(),locationDataList.get(i+1).getLatitude(),locationDataList.get(i+1).getLongitude()));
    	}
    }
    
    
    
    
    private static double calculateBearing(double lat1, double lon1, double lat2, double lon2){
    	  double longitude1 = lon1;
    	  double longitude2 = lon2;
    	  double latitude1 = Math.toRadians(lat1);
    	  double latitude2 = Math.toRadians(lat2);
    	  double longDiff= Math.toRadians(longitude2-longitude1);
    	  double y= Math.sin(longDiff)*Math.cos(latitude2);
    	  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

    	  return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    	}
}
