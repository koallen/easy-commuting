package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "channel", strict = false)
public class PSIResponse {
    @Attribute(name = "value")
    @Path("item/region[1]/record/reading[1]")
    private int psiReading;

    public int getPsiReading() {
        return psiReading;
    }
}
