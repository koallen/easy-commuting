package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 16/3/16.
 */
@Root(name = "data")
public class UVIndex {
    @Attribute(name = "hr")
    private String time;
    @Element(name = "uv")
    private int index;

    public String toString() {
        return time + index;
    }
}
