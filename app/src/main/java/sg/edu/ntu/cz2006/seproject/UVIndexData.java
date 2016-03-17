package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name =  "data")
public class UVIndexData {
    @ElementList(entry = "uv", inline = true)
    private List<String> uvIndexes;

    public List<String> getUV() {
        return uvIndexes;
    }
}
