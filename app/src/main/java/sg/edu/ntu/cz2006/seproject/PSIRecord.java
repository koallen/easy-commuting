package sg.edu.ntu.cz2006.seproject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by koAllen on 17/3/16.
 */
@Root(name = "record", strict = false)
public class PSIRecord {
    @ElementList(entry = "reading", inline = true)
    private List<PSIReading> psiReadings;

    public List<PSIReading> getPsiReadings() {
        return psiReadings;
    }
}
