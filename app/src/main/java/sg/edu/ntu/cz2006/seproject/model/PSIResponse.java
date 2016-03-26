package sg.edu.ntu.cz2006.seproject.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * A class for storing API response
 */
@Root(name = "channel", strict = false)
public class PSIResponse {
    @ElementList(entry = "region", inline = true)
    @Path("item")
    private List<PSIData> mPSIData;

    /**
     * Return a list of PSI data
     * @return List of PSI data
     */
    public List<PSIData> getPsiReading() {
        return mPSIData;
    }
}
