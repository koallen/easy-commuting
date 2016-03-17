package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "item", strict = false)
public class PSIData {
    @ElementList(entry = "region", inline = true)
    private List<PSIRegion> psiRegions;

    public List<PSIRegion> getPsiRegions() {
        return psiRegions;
    }
}
