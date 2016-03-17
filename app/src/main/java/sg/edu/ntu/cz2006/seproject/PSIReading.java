package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "reading")
public class PSIReading {
    @Attribute(name = "value")
    private String value;

    @Attribute(name = "type")
    private String type;

    public String toString() {
        return type + " " + value;
    }
}
