package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "uvindex", strict = false)
public class UVIndexResponse {
    @Element(name = "data")
    private UVIndexData data;

    public UVIndexData getUVIndexData() {
        return data;
    }
}
