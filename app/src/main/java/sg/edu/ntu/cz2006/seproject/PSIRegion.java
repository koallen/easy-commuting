package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "region")
public class PSIRegion {
    @Element(name = "id")
    private String id;

    @Element(name = "latitude")
    private double latitude;

    @Element(name = "longitude")
    private double longitude;

    @Element(name = "record")
    private PSIRecord psiRecord;

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public PSIRecord getPsiRecord() {
        return psiRecord;
    }

    public String toString() {
        return id + " " + latitude + " " + longitude + " " + psiRecord;
    }
}
