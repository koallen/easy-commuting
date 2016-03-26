package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * A class holding PSI info for a region
 */
@Root(name = "item", strict = false)
public class PSIData {

    @Element(name = "id")
    private String mId;

    @Element(name = "latitude")
    private double mLat;

    @Element(name = "longitude")
    private double mLon;

    @Attribute(name = "value")
    @Path("record/reading[1]")
    private int mReading;

    /**
     * Returns the region id
     * @return Region id
     */
    public String getId() {
        return mId;
    }

    /**
     * Returns the latitude of a region
     * @return Region latitude
     */
    public double getLat() {
        return mLat;
    }

    /**
     * Returns the longitude of a region
     * @return Region longitude
     */
    public double getLon() {
        return mLon;
    }

    /**
     * Returns the reading of a region
     * @return Region reading
     */
    public int getReading() {
        return mReading;
    }
}
