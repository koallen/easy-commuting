package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 22/3/16.
 */
@Root(name = "item", strict = false)
public class PSIData {

    @Element(name = "latitude")
    private double mLat;

    @Element(name = "longitude")
    private double mLon;

    @Attribute(name = "value")
    @Path("record/reading[1]")
    private int mReading;

    public double getLat() {
        return mLat;
    }

    public double getLon() {
        return mLon;
    }

    public int getReading() {
        return mReading;
    }

    public String toString() {
        return mLat + ", " + mLon + ", " + mReading;
    }
}
